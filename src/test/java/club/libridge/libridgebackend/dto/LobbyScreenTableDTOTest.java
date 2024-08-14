package club.libridge.libridgebackend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import club.libridge.libridgebackend.TestWithMocks;
import scalabridge.Direction;
import club.libridge.libridgebackend.core.Player;
import club.libridge.libridgebackend.networking.server.Table;

public class LobbyScreenTableDTOTest extends TestWithMocks {

    private UUID tableId;
    private String gameName;
    private int numberOfSpectators;
    @Mock
    private Map<Direction, Player> playersDirections;

    LobbyScreenTableDTO subject;

    @BeforeEach
    public void setupMocks() {
        this.tableId = UUID.randomUUID();
        this.gameName = "Minibridge";
        this.numberOfSpectators = 4;
        Table table = mock(Table.class);
        when(table.getId()).thenReturn(tableId);
        when(table.getPlayersDirections()).thenReturn(playersDirections);
        when(table.getGameName()).thenReturn(gameName);
        when(table.getNumberOfSpectators()).thenReturn(numberOfSpectators);
        subject = new LobbyScreenTableDTO(table);
    }

    @Test
    public void getIdDirectionShouldReturnTableId() {
        assertEquals(tableId, subject.getId());
    }

    @Test
    public void getNumberOfSpectatorsShouldReturnNumberOfSpectators() {
        assertEquals(numberOfSpectators, subject.getNumberOfSpectators());
    }

    @Test
    public void getGameNameShouldReturnTheGameName() {
        assertEquals(gameName, subject.getGameName());
    }

}
