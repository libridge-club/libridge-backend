package club.libridge.libridgebackend.networking.server;

import static club.libridge.libridgebackend.logging.SBKingLogger.LOGGER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import club.libridge.libridgebackend.app.PlayerController;
import club.libridge.libridgebackend.app.TableController;
import club.libridge.libridgebackend.app.persistence.BoardEntity;
import club.libridge.libridgebackend.app.persistence.BoardRepository;
import club.libridge.libridgebackend.app.persistence.DoubleDummyTableEntity;
import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.Card;
import club.libridge.libridgebackend.core.Deal;
import club.libridge.libridgebackend.core.Direction;
import club.libridge.libridgebackend.core.Player;
import club.libridge.libridgebackend.core.Strain;
import club.libridge.libridgebackend.dds.DoubleDummyTable;
import club.libridge.libridgebackend.dto.BoardDTO;
import club.libridge.libridgebackend.dto.LobbyScreenTableDTO;
import club.libridge.libridgebackend.networking.messages.GameNameFromGameServerIdentifier;
import club.libridge.libridgebackend.networking.server.gameserver.GameServer;
import club.libridge.libridgebackend.networking.server.gameserver.MinibridgeGameServer;
import club.libridge.libridgebackend.networking.websockets.PlayerDTO;
import club.libridge.libridgebackend.networking.websockets.PlayerListDTO;
import club.libridge.libridgebackend.pbn.PBNUtils;
import club.libridge.libridgebackend.utils.FileUtils;

/**
 * This class has two responsibilities: 1: receiving method calls from the
 * GameServer and sending to client(s) over the network layer
 * (Controllers). 2: receiving method calls from the network layer and
 * act on the GameServer (via Table).
 */
public class SBKingServer {

  private PlayerController playerController;

  private Map<UUID, Player> identifierToPlayerMap = new HashMap<UUID, Player>();
  private Map<UUID, Table> tables;
  private Map<Player, Table> playersTable;

  private static final int MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS = 10;
  private ExecutorService pool;
  private WebSocketTableMessageServerSender webSocketTableMessageServerSender;

  public SBKingServer(PlayerController playerController, TableController tableController) {
    this.tables = new HashMap<UUID, Table>();
    this.playersTable = new HashMap<Player, Table>();
    this.identifierToPlayerMap = new HashMap<UUID, Player>();
    this.pool = Executors.newFixedThreadPool(MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS);
    this.playerController = playerController;
    this.webSocketTableMessageServerSender = new WebSocketTableMessageServerSender(tableController);
  }

  public void addUnnammedPlayer(UUID identifier) {
    Player player = new Player(identifier, "SBKingServer Unnamed");
    this.identifierToPlayerMap.put(identifier, player);
  }

  private Direction getDirectionFromIdentifier(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (table != null) {
      return table.getDirectionFrom(player);
    }
    return null;
  }

  public void play(Card card, UUID playerIdentifier) {
    Direction from = this.getDirectionFromIdentifier(playerIdentifier);
    Player player = this.identifierToPlayerMap.get(playerIdentifier);
    Table table = this.playersTable.get(player);
    if (table == null) {
      return;
    }
    table.getGameServer().notifyPlayCard(card, from); // FIXME Law of Demeter
  }

  public void moveToSeat(Direction direction, UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    table.moveToSeat(player, direction);
  }

  public void chooseStrain(String strainString, UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    GameServer gameServer = table.getGameServer();
    Strain strain = Strain.fromName(strainString);
    Direction directionFromIdentifier = this.getDirectionFromIdentifier(playerIdentifier);
    if (strain != null && directionFromIdentifier != null) {
      if (gameServer instanceof MinibridgeGameServer) {
        MinibridgeGameServer minibridgeGameServer = (MinibridgeGameServer) table.getGameServer();
        minibridgeGameServer.notifyChooseStrain(strain, directionFromIdentifier);
      }
    }
  }

  // TODO This should be optimized when we have websockets messages directly for
  // players.
  public void sendDirectionTo(Direction direction, UUID playerIdentifier) {
    this.sendUpdatePlayerList();
  }

  // TODO This should be optimized when we have websockets messages directly for
  // players.
  public void sendIsSpectatorTo(UUID playerIdentifier) {
    this.sendUpdatePlayerList();
  }

  // TODO This should be optimized when we have websockets messages directly for
  // players.
  public void sendIsNotSpectatorTo(UUID playerIdentifier) {
    this.sendUpdatePlayerList();
  }

  public void sendDealToTable(Deal deal, Table table) {
    this.webSocketTableMessageServerSender.sendDealToTable(deal, table);
  }

  public void sendFinishDealToTable(Table table) {
    this.webSocketTableMessageServerSender.sendFinishDealToTable(table);
  }

  public void sendInitializeDealToTable(Table table) {
    this.webSocketTableMessageServerSender.sendInitializeDealToTable(table);
  }

  public void sendInvalidRulesetToTable(Table table) {
    this.webSocketTableMessageServerSender.sendInvalidRulesetToTable(table);
  }

  public void sendValidRulesetToTable(Table table) {
    this.webSocketTableMessageServerSender.sendValidRulesetToTable(table);
  }

  public void sendStrainChooserToTable(Direction direction, Table table) {
    this.webSocketTableMessageServerSender.sendStrainChooserToTable(direction, table);
  }

  public void setNickname(UUID identifier, String nickname) {
    LOGGER.debug("Setting nickname for player {}", identifier);
    Player player = identifierToPlayerMap.get(identifier);
    player.setNickname(nickname);
  }

  public String getNicknameFromIdentifier(UUID identifier) {
    Player player = identifierToPlayerMap.get(identifier);
    if (player != null) {
      return player.getNickname();
    } else {
      return null;
    }
  }

  public void addSpectator(Player player, Table table) {
    Table currentTable = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    if (currentTable != null) {
      currentTable.removePlayer(player.getIdentifier());
    }
    table.addSpectator(player);
    table.sendDealAll();
  }

  public void joinTable(UUID playerIdentifier, UUID tableIdentifier) {
    Table table = this.tables.get(tableIdentifier);
    Player player = this.identifierToPlayerMap.get(playerIdentifier);
    if (table != null && player != null) {
      this.addSpectator(player, table);
      this.playersTable.put(player, table);
      this.sendUpdatePlayerList();
    }
  }

  public void undo(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    table.undo(player);
  }

  public void removePlayer(UUID playerIdentifier) {
    this.leaveTable(playerIdentifier);
    identifierToPlayerMap.remove(playerIdentifier);
  }

  public Table getTable(UUID tableIdentifier) {
    return this.tables.get(tableIdentifier);
  }

  public UUID createTable(Class<? extends GameServer> gameServerClass) {
    GameServer gameServer;
    try {
      gameServer = gameServerClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      LOGGER.fatal("Could not initialize GameServer with received gameServerClass.");
      return null;
    }
    Table table = new Table(gameServer);
    gameServer.setSBKingServer(this);
    tables.put(table.getId(), table);
    pool.execute(gameServer);
    LOGGER.info("Created new table and executed its gameServer!");
    return table.getId();
  }

  public List<LobbyScreenTableDTO> getTablesDTO() {
    return this.tables.values().stream().map(LobbyScreenTableDTO::new).collect(Collectors.toList());
  }

  public void leaveTable(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }

    table.removePlayer(playerIdentifier);
    playersTable.remove(player);
    if (table.isEmpty()) {
      this.tables.remove(table.getId());
      table.dismantle();
    }
    this.sendUpdatePlayerList();
  }

  public void claim(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    table.claim(player);
  }

  public void acceptClaim(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    table.acceptClaim(player);
  }

  public void rejectClaim(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    table.rejectClaim(player);
  }

  public List<String> getSpectatorList(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (table != null) {
      return table.getSpectatorNames();
    }
    return new ArrayList<String>();
  }

  private void sendUpdatePlayerList() {
    List<PlayerDTO> list = new ArrayList<>();
    for (Map.Entry<UUID, Player> pair : identifierToPlayerMap.entrySet()) {
      UUID playerIdentifier = pair.getKey();
      Player player = pair.getValue();
      Table table = playersTable.get(player);
      Boolean isSpectator = true;
      Direction direction = null;
      UUID tableIdentifier = null;
      String gameName = this.getGameNameFrom(table);
      if (table != null) {
        tableIdentifier = table.getId();
        isSpectator = table.isSpectator(player);
        direction = table.getDirectionFrom(player);
      }
      PlayerDTO playerDTO = new PlayerDTO(playerIdentifier, tableIdentifier, isSpectator, direction, gameName);
      list.add(playerDTO);
      LOGGER.debug("Added player: {} {} {} {}", playerIdentifier, tableIdentifier, isSpectator, direction);
    }

    PlayerListDTO playerList = new PlayerListDTO();
    playerList.setList(list);
    try {
      this.playerController.getPlayers(playerList);
    } catch (Exception e) {
      LOGGER.error(e);
    }
  }

  private String getGameNameFrom(Table table) {
    if (table == null) {
      return null;
    }
    GameServer gameServer = table.getGameServer();
    return GameNameFromGameServerIdentifier.identify(gameServer.getClass());
  }

  public void refreshTable(UUID tableId) {
    this.getTable(tableId).sendDealAll();
  }

  public BoardDTO getRandomBoard(BoardRepository repository) {
      BoardEntity randomBoardEntity = repository.getRandomBoard();
      BoardDTO boardDTO = new BoardDTO(randomBoardEntity.getBoard(), randomBoardEntity.getPavlicekNumber());
      boardDTO.setDoubleDummyTable(randomBoardEntity.getDoubleDummyTableEntity().getDoubleDummyTable());
      return boardDTO;
  }

  public void magicNumberCreateTablesFromFile(BoardRepository repository) {
    // read files with boards
    // save them to database
    String wholeFile = "";
    try {
      wholeFile = FileUtils.readFromFilename("/hands-with-table.txt", false);
    } catch (Exception e) {
      // TODO: handle exception
    }

    System.out.println(wholeFile);
    String[] lines = wholeFile.split("\n");
    for (int i = 0; i < 2000 && ((i + 1) < lines.length); i += 2) {
      String[] pbn = lines[i].split("\"");
      String[] table = lines[i + 1].split(" ");
      String finalPbn = pbn[1];
      List<Integer> finalTable = new ArrayList<Integer>();
      for (int j = 1; j <= 20; j++) {
          finalTable.add(Integer.parseInt(table[j]));
        }
      Board boardFromDealTag = PBNUtils.getBoardFromDealTag(finalPbn);
      DoubleDummyTable doubleDummyTable = new DoubleDummyTable(finalTable);

      BoardEntity boardEntity = new BoardEntity();
      boardEntity.setBoard(boardFromDealTag);

      DoubleDummyTableEntity doubleDummyTableEntity = new DoubleDummyTableEntity();
      doubleDummyTableEntity.setDoubleDummyTable(doubleDummyTable);

      boardEntity.setDoubleDummyTableEntity(doubleDummyTableEntity);
      doubleDummyTableEntity.setBoardEntity(boardEntity);

      repository.save(boardEntity);
    }

  }

}
