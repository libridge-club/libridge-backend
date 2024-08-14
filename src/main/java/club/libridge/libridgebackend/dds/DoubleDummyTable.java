package club.libridge.libridgebackend.dds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import club.libridge.libridgebackend.core.NumberOfTricks;
import scalabridge.Direction;
import scalabridge.Strain;

public class DoubleDummyTable {

    private static final List<Strain> STRAIN_ORDER_FROM_DDS;
    private static final List<Direction> DIRECTION_ORDER_FROM_DDS;
    private final Map<StrainAndDirectionCombination, NumberOfTricks> tricksAvailable;

    static {
        STRAIN_ORDER_FROM_DDS = Collections.unmodifiableList(List.of(Strain.SPADES, Strain.HEARTS, Strain.DIAMONDS, Strain.CLUBS, Strain.NOTRUMPS));
        DIRECTION_ORDER_FROM_DDS = Collections.unmodifiableList(List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST));
    }

    public DoubleDummyTable(List<Integer> list) { // This is the format received from DDS
        this.tricksAvailable = new HashMap<>();

        int currentIndex = 0;
        for (Strain strain : STRAIN_ORDER_FROM_DDS) {
            for (Direction direction : DIRECTION_ORDER_FROM_DDS) {
                StrainAndDirectionCombination combination = new StrainAndDirectionCombination(strain, direction);
                this.tricksAvailable.put(combination, new NumberOfTricks(list.get(currentIndex)));
                currentIndex++;
            }
        }
    }

    public NumberOfTricks getTricksAvailableFor(Strain strain, Direction direction) {
        return this.tricksAvailable.get(new StrainAndDirectionCombination(strain, direction));
    }

    public List<Integer> toDDSIntegerList() {
        ArrayList<Integer> returnValue = new ArrayList<Integer>();
        for (Strain strain : STRAIN_ORDER_FROM_DDS) {
            for (Direction direction : DIRECTION_ORDER_FROM_DDS) {
                NumberOfTricks numberOfTricks = this.tricksAvailable.get(new StrainAndDirectionCombination(strain, direction));
                returnValue.add(numberOfTricks.getInt());
            }
        }
        return returnValue;
    }

    public Map<Direction, Map<Strain, Integer>> getMapFormatted() {
        Map<Direction, Map<Strain, Integer>> returnValue = new EnumMap<Direction, Map<Strain, Integer>>(Direction.class);
        for (Direction direction : DIRECTION_ORDER_FROM_DDS) {
            Map<Strain, Integer> allStrainsInADirection = new EnumMap<Strain, Integer>(Strain.class);
            for (Strain strain : STRAIN_ORDER_FROM_DDS) {
                NumberOfTricks numberOfTricks = this.tricksAvailable.get(new StrainAndDirectionCombination(strain, direction));
                allStrainsInADirection.put(strain, numberOfTricks.getInt());
            }
            returnValue.put(direction, allStrainsInADirection);
        }
        return returnValue;
    }

}
