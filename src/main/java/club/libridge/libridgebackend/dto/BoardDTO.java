package club.libridge.libridgebackend.dto;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import club.libridge.libridgebackend.dds.DoubleDummyTable;
import lombok.Getter;
import lombok.Setter;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;
import scalabridge.Hand;
import scalabridge.Strain;
import scalabridge.TricksMade;

public class BoardDTO {

    @Setter
    private UUID id;

    @Getter
    private final Direction dealer;
    @Getter
    private final Map<Direction, String> hands;
    @Getter
    private final String pavlicekNumber;
    @Getter
    private final String pbnDealTag;
    private Map<Direction, Map<Strain, Integer>> doubleDummyTable;

    public BoardDTO(DuplicateBoard board, String pavlicekNumber, String pbnDealTag) {
        this.dealer = board.getDealer();
        Map<Direction, String> hands = new HashMap<Direction, String>();
        for (Direction direction : Direction.values()) {
            Hand thisHand = board.getHandOf(direction).hand();
            hands.put(direction, thisHand.toString());
        }
        this.hands = hands;
        this.pavlicekNumber = pavlicekNumber;
        this.pbnDealTag = pbnDealTag;
    }

    public void setDoubleDummyTable(DoubleDummyTable doubleDummyTable) {
        this.doubleDummyTable = new HashMap<Direction, Map<Strain, Integer>>();
        for (Direction direction : Direction.values()) {
            EnumMap<Strain, Integer> strainMap = new EnumMap<Strain, Integer>(Strain.class);
            for (Strain strain : Strain.values()) {
                TricksMade tricksAvailableFor = doubleDummyTable.getTricksAvailableFor(strain, direction);
                strainMap.put(strain, tricksAvailableFor.tricks());
            }
            this.doubleDummyTable.put(direction, Collections.unmodifiableMap(strainMap));
        }
        this.doubleDummyTable = Collections.unmodifiableMap(this.doubleDummyTable);
    }

}
