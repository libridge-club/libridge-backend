package club.libridge.libridgebackend.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import club.libridge.libridgebackend.core.Direction;
import club.libridge.libridgebackend.core.Strain;

/**
     * From DDS documentation:
     * Encodes the solution of a deal for combinations of denomination and declarer.
     * First index is denomination. Suit encoding.
     * Second index is declarer.  Hand encoding.
     * Each entry is a number of tricks.
     *
     * DDS returns this: [9, 3, 9, 3, 4, 7, 4, 7, 6, 6, 5, 7, 5, 8, 5, 7, 4 7 3 8]
     * Which represents this Double Dummy Table:
     *           N E S W
     * SPADES    9 3 9 3
     * HEARTS    4 7 4 7
     * DIAMONDS  6 6 5 7
     * CLUBS     5 8 5 7
     * NOTRUMPS  4 7 3 8
     *
     */
public class DoubleDummyTableTest {

    private List<Integer> usbcHandDDSResponse = List.of(9, 3, 9, 3, 4, 7, 4, 7, 6, 6, 5, 7, 5, 8, 5, 7, 4, 7, 3, 8);

    @Test
    public void toDDSIntegerList_shouldReturnTheSameInformationStored(){
        DoubleDummyTable doubleDummyTable = new DoubleDummyTable(usbcHandDDSResponse);

        List<Integer> ddsIntegerList = doubleDummyTable.toDDSIntegerList();
        assertEquals(usbcHandDDSResponse, ddsIntegerList);
    }

    @Test
    public void getTricksAvailableFor_shouldReturnTheSameInformationStored(){
        DoubleDummyTable doubleDummyTable = new DoubleDummyTable(usbcHandDDSResponse);

        assertEquals(usbcHandDDSResponse.get(0), getNumber(doubleDummyTable,Strain.SPADES, Direction.NORTH));
        assertEquals(usbcHandDDSResponse.get(1), getNumber(doubleDummyTable,Strain.SPADES, Direction.EAST));
        assertEquals(usbcHandDDSResponse.get(2), getNumber(doubleDummyTable,Strain.SPADES, Direction.SOUTH));
        assertEquals(usbcHandDDSResponse.get(3), getNumber(doubleDummyTable,Strain.SPADES, Direction.WEST));

        assertEquals(usbcHandDDSResponse.get(4), getNumber(doubleDummyTable,Strain.HEARTS, Direction.NORTH));
        assertEquals(usbcHandDDSResponse.get(5), getNumber(doubleDummyTable,Strain.HEARTS, Direction.EAST));
        assertEquals(usbcHandDDSResponse.get(6), getNumber(doubleDummyTable,Strain.HEARTS, Direction.SOUTH));
        assertEquals(usbcHandDDSResponse.get(7), getNumber(doubleDummyTable,Strain.HEARTS, Direction.WEST));

        assertEquals(usbcHandDDSResponse.get(8), getNumber(doubleDummyTable,Strain.DIAMONDS, Direction.NORTH));
        assertEquals(usbcHandDDSResponse.get(9), getNumber(doubleDummyTable,Strain.DIAMONDS, Direction.EAST));
        assertEquals(usbcHandDDSResponse.get(10), getNumber(doubleDummyTable,Strain.DIAMONDS, Direction.SOUTH));
        assertEquals(usbcHandDDSResponse.get(11), getNumber(doubleDummyTable,Strain.DIAMONDS, Direction.WEST));

        assertEquals(usbcHandDDSResponse.get(12), getNumber(doubleDummyTable,Strain.CLUBS, Direction.NORTH));
        assertEquals(usbcHandDDSResponse.get(13), getNumber(doubleDummyTable,Strain.CLUBS, Direction.EAST));
        assertEquals(usbcHandDDSResponse.get(14), getNumber(doubleDummyTable,Strain.CLUBS, Direction.SOUTH));
        assertEquals(usbcHandDDSResponse.get(15), getNumber(doubleDummyTable,Strain.CLUBS, Direction.WEST));

        assertEquals(usbcHandDDSResponse.get(16), getNumber(doubleDummyTable,Strain.NOTRUMPS, Direction.NORTH));
        assertEquals(usbcHandDDSResponse.get(17), getNumber(doubleDummyTable,Strain.NOTRUMPS, Direction.EAST));
        assertEquals(usbcHandDDSResponse.get(18), getNumber(doubleDummyTable,Strain.NOTRUMPS, Direction.SOUTH));
        assertEquals(usbcHandDDSResponse.get(19), getNumber(doubleDummyTable,Strain.NOTRUMPS, Direction.WEST));
    }

    private int getNumber(DoubleDummyTable doubleDummyTable, Strain strain, Direction direction){
        return doubleDummyTable.getTricksAvailableFor(strain, direction).getInt();
    }
}
