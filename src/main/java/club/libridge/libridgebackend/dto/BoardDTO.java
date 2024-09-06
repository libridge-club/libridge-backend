package club.libridge.libridgebackend.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.Hand;
import club.libridge.libridgebackend.core.NumberOfTricks;
import club.libridge.libridgebackend.dds.DoubleDummyTable;
import lombok.Getter;
import lombok.Setter;
import scalabridge.Direction;
import scalabridge.Strain;

public class BoardDTO {

    @Setter
    private UUID id;

    @Getter
    private final Board board;
    @Getter
    private final List<String> hands;
    @Getter
    private final String pavlicekNumber;
    private Map<Direction, Map<Strain, Integer>> doubleDummyTable;

    public BoardDTO(Board board, String pavlicekNumber) {
        this.board = board;
        List<String> hands = new ArrayList<String>();
        for (Direction direction : Direction.values()) {
            Hand thisHand = board.getHandOf(direction);
            hands.add(thisHand.toString());
        }
        this.hands = hands;
        this.pavlicekNumber = pavlicekNumber;
    }

    public void setDoubleDummyTable(DoubleDummyTable doubleDummyTable) {
        this.doubleDummyTable = new HashMap<Direction, Map<Strain, Integer>>();
        for (Direction direction : Direction.values()) {
            EnumMap<Strain, Integer> strainMap = new EnumMap<Strain, Integer>(Strain.class);
            for (Strain strain : Strain.values()) {
                NumberOfTricks tricksAvailableFor = doubleDummyTable.getTricksAvailableFor(strain, direction);
                strainMap.put(strain, tricksAvailableFor.getInt());
            }
            this.doubleDummyTable.put(direction, Collections.unmodifiableMap(strainMap));
        }
        this.doubleDummyTable = Collections.unmodifiableMap(this.doubleDummyTable);
    }

}
