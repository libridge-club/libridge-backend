package club.libridge.libridgebackend.dds;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scalabridge.Direction;
import scalabridge.Strain;
import scalabridge.TricksMade;

public class DoubleDummyTable {

    private static final List<Strain> STRAIN_ORDER_FROM_DDS;
    private static final List<Direction> DIRECTION_ORDER_FROM_DDS;
    private final Map<StrainAndDirectionCombination, TricksMade> tricksAvailable;

    static {
        STRAIN_ORDER_FROM_DDS = List.of(Strain.getSPADES(), Strain.getHEARTS(), Strain.getDIAMONDS(), Strain.getCLUBS(), Strain.getNOTRUMPS());
        DIRECTION_ORDER_FROM_DDS = List.of(Direction.getNorth(), Direction.getEast(), Direction.getSouth(), Direction.getWest());
    }

    public DoubleDummyTable(List<Integer> list) { // This is the format received from DDS
        this.tricksAvailable = new HashMap<>();

        int currentIndex = 0;
        for (Strain strain : STRAIN_ORDER_FROM_DDS) {
            for (Direction direction : DIRECTION_ORDER_FROM_DDS) {
                StrainAndDirectionCombination combination = new StrainAndDirectionCombination(strain, direction);
                this.tricksAvailable.put(combination, TricksMade.fromOrdinal(list.get(currentIndex)));
                currentIndex++;
            }
        }
    }

    public TricksMade getTricksAvailableFor(Strain strain, Direction direction) {
        return this.tricksAvailable.get(new StrainAndDirectionCombination(strain, direction));
    }

    public List<Integer> toDDSIntegerList() {
        ArrayList<Integer> returnValue = new ArrayList<Integer>();
        for (Strain strain : STRAIN_ORDER_FROM_DDS) {
            for (Direction direction : DIRECTION_ORDER_FROM_DDS) {
                TricksMade numberOfTricks = this.tricksAvailable.get(new StrainAndDirectionCombination(strain, direction));
                returnValue.add(numberOfTricks.tricks());
            }
        }
        return returnValue;
    }

    public Map<Direction, Map<Strain, Integer>> getMapFormatted() {
        Map<Direction, Map<Strain, Integer>> returnValue = new HashMap<Direction, Map<Strain, Integer>>();
        for (Direction direction : DIRECTION_ORDER_FROM_DDS) {
            Map<Strain, Integer> allStrainsInADirection = new EnumMap<Strain, Integer>(Strain.class);
            for (Strain strain : STRAIN_ORDER_FROM_DDS) {
                TricksMade numberOfTricks = this.tricksAvailable.get(new StrainAndDirectionCombination(strain, direction));
                allStrainsInADirection.put(strain, numberOfTricks.tricks());
            }
            returnValue.put(direction, allStrainsInADirection);
        }
        return returnValue;
    }

}
