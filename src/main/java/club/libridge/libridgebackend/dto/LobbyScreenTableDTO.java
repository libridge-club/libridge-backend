package club.libridge.libridgebackend.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import scalabridge.Direction;
import club.libridge.libridgebackend.core.Player;
import club.libridge.libridgebackend.networking.server.Table;
import lombok.Getter;
import lombok.NonNull;

public class LobbyScreenTableDTO {

    @Getter
    private UUID id;
    @Getter
    private Map<String, String> playersDirections;
    @Getter
    private String gameName;
    @Getter
    private int numberOfSpectators;

    /**
     * @deprecated Kryo needs a no-arg constructor
     * FIXME kryo is not used anymore. Does jackson or spring web needs this?
     */
    @Deprecated
    @SuppressWarnings("unused")
    private LobbyScreenTableDTO() {
    }

    public LobbyScreenTableDTO(@NonNull Table table) {
        this.id = table.getId();
        Map<Direction, Player> tempMap = table.getPlayersDirections();
        this.playersDirections = new HashMap<String, String>();
        for (Direction direction : tempMap.keySet()) {
            this.playersDirections.put(direction.getCompleteName(), tempMap.get(direction).getNickname());
        }
        this.gameName = table.getGameName();
        this.numberOfSpectators = table.getNumberOfSpectators();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LobbyScreenTableDTO) {
            LobbyScreenTableDTO other = (LobbyScreenTableDTO) obj;
            return this.getId().equals(other.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

}
