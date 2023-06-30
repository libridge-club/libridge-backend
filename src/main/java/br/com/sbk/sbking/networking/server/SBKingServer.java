package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import br.com.sbk.sbking.app.PlayerController;
import br.com.sbk.sbking.app.TableController;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.core.rulesets.RulesetFromShortDescriptionIdentifier;
import br.com.sbk.sbking.core.rulesets.abstractrulesets.Ruleset;
import br.com.sbk.sbking.dto.LobbyScreenTableDTO;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.kryonet.KryonetSBKingServer;
import br.com.sbk.sbking.networking.kryonet.messages.GameNameFromGameServerIdentifier;
import br.com.sbk.sbking.networking.server.gameserver.GameServer;
import br.com.sbk.sbking.networking.server.gameserver.KingGameServer;
import br.com.sbk.sbking.networking.server.gameserver.MinibridgeGameServer;
import br.com.sbk.sbking.networking.server.gameserver.PositiveKingGameServer;
import br.com.sbk.sbking.networking.websockets.PlayerDTO;
import br.com.sbk.sbking.networking.websockets.PlayerListDTO;
import br.com.sbk.sbking.networking.websockets.TableMessageDTO;

/**
 * This class has two responsibilities: 1: receiving method calls from the
 * GameServer and sending to client(s) over the network layer
 * (KryonetSBKingServer). 2: receiving method calls from the network layer and
 * act on the GameServer (via Table).
 */
public class SBKingServer {

  private KryonetSBKingServer kryonetSBKingServer = null;
  private PlayerController playerController;
  private TableController tableController;

  private Map<UUID, Player> identifierToPlayerMap = new HashMap<UUID, Player>();
  private Map<UUID, Table> tables;
  private Map<Player, Table> playersTable;

  private static final int MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS = 10;
  private ExecutorService pool;

  public SBKingServer(PlayerController playerController, TableController tableController) {
    this.tables = new HashMap<UUID, Table>();
    this.playersTable = new HashMap<Player, Table>();
    this.identifierToPlayerMap = new HashMap<UUID, Player>();
    this.pool = Executors.newFixedThreadPool(MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS);
    this.playerController = playerController;
    this.tableController = tableController;
  }

  public void setKryonetSBKingServer(KryonetSBKingServer kryonetSBKingServer) {
    this.kryonetSBKingServer = kryonetSBKingServer;
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

  public void choosePositive(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    KingGameServer kingGameServer = (KingGameServer) table.getGameServer();
    PositiveOrNegative positiveOrNegative = new PositiveOrNegative();
    positiveOrNegative.setPositive();
    kingGameServer.notifyChoosePositiveOrNegative(positiveOrNegative,
        this.getDirectionFromIdentifier(playerIdentifier));
  }

  public void chooseNegative(UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    KingGameServer kingGameServer = (KingGameServer) table.getGameServer();
    PositiveOrNegative positiveOrNegative = new PositiveOrNegative();
    positiveOrNegative.setNegative();
    kingGameServer.notifyChoosePositiveOrNegative(positiveOrNegative,
        this.getDirectionFromIdentifier(playerIdentifier));
  }

  public void chooseGameModeOrStrain(String gameModeOrStrainString, UUID playerIdentifier) {
    Player player = identifierToPlayerMap.get(playerIdentifier);
    Table table = playersTable.get(player);
    if (player == null || table == null) {
      return;
    }
    GameServer gameServer = table.getGameServer();
    Ruleset gameModeOrStrain = RulesetFromShortDescriptionIdentifier.identify(gameModeOrStrainString);
    Direction directionFromIdentifier = this.getDirectionFromIdentifier(playerIdentifier);
    if (gameModeOrStrain != null && directionFromIdentifier != null) {
      if (gameServer instanceof MinibridgeGameServer) {
        MinibridgeGameServer minibridgeGameServer = (MinibridgeGameServer) table.getGameServer();
        minibridgeGameServer.notifyChooseGameModeOrStrain(gameModeOrStrain, directionFromIdentifier);
      } else if (gameServer instanceof KingGameServer) {
        KingGameServer kingGameServer = (KingGameServer) table.getGameServer();
        kingGameServer.notifyChooseGameModeOrStrain(gameModeOrStrain, directionFromIdentifier);
      } else if (gameServer instanceof PositiveKingGameServer) {
        PositiveKingGameServer positiveKingGameServer = (PositiveKingGameServer) table.getGameServer();
        positiveKingGameServer.notifyChooseGameModeOrStrain(gameModeOrStrain, directionFromIdentifier);
      }
    }
  }

  private Iterable<UUID> getAllPlayersUUIDsOnTable(Table table) {
    Collection<UUID> allPlayersOnTable = new LinkedList<UUID>();
    if (table == null) {
      return allPlayersOnTable;
    }
    for (Map.Entry<Player, Table> pair : playersTable.entrySet()) {
      if (table.equals(pair.getValue())) {
        allPlayersOnTable.add(pair.getKey().getIdentifier());
      }
    }
    return allPlayersOnTable;
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
    TableMessageDTO tableDealDTO = createTableMessageDTO("deal", table.getId().toString(), deal);
    this.tableController.getDeal(tableDealDTO);
  }

  public void sendFinishDealToTable(Table table) {
    TableMessageDTO tableDealDTO = createTableMessageDTO("finishDeal", table.getId().toString(), null);
    this.tableController.sendFinishDeal(tableDealDTO);
  }

  private TableMessageDTO createTableMessageDTO(String message, String tableId, Deal deal) {
    TableMessageDTO tableDealDTO = new TableMessageDTO();
    tableDealDTO.setMessage(message);
    tableDealDTO.setTableId(tableId);
    tableDealDTO.setDeal(deal);
    return tableDealDTO;
  }

  public void sendGameModeOrStrainChooserToTable(Direction direction, Table table) {
    this.kryonetSBKingServer.sendGameModeOrStrainChooserTo(direction, this.getAllPlayersUUIDsOnTable(table));
  }

  public void sendPositiveOrNegativeChooserToTable(Direction direction, Table table) {
    this.kryonetSBKingServer.sendPositiveOrNegativeChooserTo(direction, this.getAllPlayersUUIDsOnTable(table));
  }

  public void sendPositiveOrNegativeToTable(PositiveOrNegative positiveOrNegative, Table table) {
    String positiveOrNegativeString = positiveOrNegative.toString().toUpperCase();
    this.kryonetSBKingServer.sendPositiveOrNegativeTo(positiveOrNegativeString, this.getAllPlayersUUIDsOnTable(table));
  }

  public void sendInitializeDealToTable(Table table) {
    this.kryonetSBKingServer.sendInitializeDealTo(this.getAllPlayersUUIDsOnTable(table));
  }

  public void sendInvalidRulesetToTable(Table table) {
    this.kryonetSBKingServer.sendInvalidRulesetTo(this.getAllPlayersUUIDsOnTable(table));
  }

  public void sendValidRulesetToTable(Table table) {
    this.kryonetSBKingServer.sendValidRulesetTo(this.getAllPlayersUUIDsOnTable(table));
  }

  public void setNickname(UUID identifier, String nickname) {
    LOGGER.info("Setting nickname {} for player {}", nickname, identifier);
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

}
