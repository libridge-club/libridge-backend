package club.libridge.libridgebackend.app.controller;

import static club.libridge.libridgebackend.logging.LibridgeLogger.LOGGER;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import club.libridge.libridgebackend.dto.RequestCard;
import club.libridge.libridgebackend.dto.RequestWithString;
import club.libridge.libridgebackend.networking.server.LibridgeServer;
import club.libridge.libridgebackend.networking.websockets.PlayerDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import scalabridge.Direction;

@RestController
@AllArgsConstructor
public class AppController {

    private static final String ACCEPT_CANNOT_BE_EMPTY = "Accept cannot be empty. It must be true or false.";

    @NonNull
    private final LibridgeServer libridgeServer;

    private UUID getUUID(String uuidString) {
        return UUID.fromString(uuidString);
    }

    @GetMapping("/connect")
    public ResponseEntity<UUID> connect() {
        LOGGER.info("connect");
        UUID playerId = UUID.randomUUID();
        // TODO remove this player at disconnection in the future
        this.libridgeServer.addUnnammedPlayer(playerId);
        LOGGER.info("ID: {}", playerId);
        return new ResponseEntity<>(playerId, HttpStatus.CREATED);
    }

    @PostMapping("/playcard")
    public void playCard(@RequestHeader("PlayerUUID") String playerUUID, @RequestBody RequestCard requestCard) {
        LOGGER.trace("playCard");
        this.libridgeServer.play(requestCard.getCard(), getUUID(playerUUID));
    }

    @PostMapping("/table/join/{tableId}")
    public void joinTable(@RequestHeader("PlayerUUID") String playerUUID, @PathVariable String tableId) {
        LOGGER.trace("joinTable");
        this.libridgeServer.joinTable(getUUID(playerUUID), UUID.fromString(tableId));
    }

    @PostMapping("/table/leave") // Each player can only be in one table for now
    public void leaveTable(@RequestHeader("PlayerUUID") String playerUUID) {
        LOGGER.trace("leaveTable");
        this.libridgeServer.leaveTable(getUUID(playerUUID));
    }

    @PostMapping("/moveToSeat/{directionAbbreviation}")
    public void moveToSeat(@RequestHeader("PlayerUUID") String playerUUID, @PathVariable String directionAbbreviation) {
        LOGGER.trace("moveToSeat");
        Direction direction = Direction.getFromAbbreviation(directionAbbreviation.charAt(0)).get();
        this.libridgeServer.moveToSeat(direction, getUUID(playerUUID));
    }

    @PutMapping("/player/nickname")
    public PlayerDTO setNickname(@RequestHeader("PlayerUUID") String playerUUID, @RequestBody RequestWithString requestWithString) {
        LOGGER.trace("setNickname");
        return this.libridgeServer.setNickname(getUUID(playerUUID), requestWithString.getContent());
    }

    @PostMapping("/claim")
    public void claim(@RequestHeader("PlayerUUID") String playerUUID) {
        LOGGER.trace("claim");
        this.libridgeServer.claim(getUUID(playerUUID));
    }

    @PostMapping("/claim/{accept}")
    public void handleClaim(@RequestHeader("PlayerUUID") String playerUUID, @PathVariable Boolean accept) {
        LOGGER.trace("handleClaim");
        LOGGER.trace(accept);
        if (accept == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ACCEPT_CANNOT_BE_EMPTY);
        }
        this.libridgeServer.claim(getUUID(playerUUID));
        if (accept) {
            this.libridgeServer.acceptClaim(getUUID(playerUUID));
        } else {
            this.libridgeServer.rejectClaim(getUUID(playerUUID));
        }
    }

    @PostMapping("/undo")
    public void undo(@RequestHeader("PlayerUUID") String playerUUID) {
        LOGGER.trace("undo");
        this.libridgeServer.undo(getUUID(playerUUID));
    }

    @PostMapping("/chooseStrain")
    public void chooseStrain(@RequestHeader("PlayerUUID") String playerUUID, @RequestBody RequestWithString requestWithString) {
        LOGGER.trace("chooseStrain");
        this.libridgeServer.chooseStrain(requestWithString.getContent(), getUUID(playerUUID));
    }

    @GetMapping("/spectators")
    public List<String> getSpectators(@RequestHeader("PlayerUUID") String playerUUID) {
        LOGGER.trace("getSpectators");
        return this.libridgeServer.getSpectatorList(getUUID(playerUUID));
    }

}
