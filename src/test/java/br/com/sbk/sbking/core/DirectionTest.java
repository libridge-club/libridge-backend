package br.com.sbk.sbking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DirectionTest {

    private static final String NORTH_COMPLETE_NAME = "North";
    private static final String EAST_COMPLETE_NAME = "East";
    private static final String SOUTH_COMPLETE_NAME = "South";
    private static final String WEST_COMPLETE_NAME = "West";

    private static final char NORTH_ABBREVIATION = 'N';
    private static final char EAST_ABBREVIATION = 'E';
    private static final char SOUTH_ABBREVIATION = 'S';
    private static final char WEST_ABBREVIATION = 'W';

    private static Direction north;
    private static Direction east;
    private static Direction south;
    private static Direction west;

    @BeforeAll
    public static void setup() {
        north = Direction.NORTH;
        east = Direction.EAST;
        south = Direction.SOUTH;
        west = Direction.WEST;
    }

    @Test
    public void theSameDirectionShouldAlwaysBeTheSameObject() {
        Direction north1 = Direction.NORTH;
        Direction north2 = Direction.NORTH;
        assertTrue(north1 == north2);
    }

    @Test
    public void theSameDirectionShouldAlwaysBeEqual() {
        Direction north1 = Direction.NORTH;
        Direction north2 = Direction.NORTH;
        assertEquals(north1, north2);
    }

    @Test
    public void shouldKnowItsDirection() {
        assertTrue(north.isNorth());
        assertFalse(east.isNorth());

        assertTrue(east.isEast());
        assertFalse(south.isEast());

        assertTrue(south.isSouth());
        assertFalse(west.isSouth());

        assertTrue(west.isWest());
        assertFalse(north.isWest());
    }

    @Test
    public void shouldKnowItsOrientation() {
        assertTrue(north.isNorthSouth());
        assertFalse(north.isEastWest());
        assertTrue(south.isNorthSouth());
        assertFalse(south.isEastWest());

        assertTrue(east.isEastWest());
        assertFalse(east.isNorthSouth());
        assertTrue(west.isEastWest());
        assertFalse(west.isNorthSouth());
    }

    @Test
    public void shouldKnowItsImmediateNext() {
        assertTrue(north.next() == east);
        assertTrue(east.next() == south);
        assertTrue(south.next() == west);
        assertTrue(west.next() == north);
    }

    @Test
    public void shouldKnowItsNonImmediateNext() {
        assertTrue(north.next(1) == east);
        assertTrue(north.next(2) == south);
        assertTrue(north.next(3) == west);
        assertTrue(north.next(4) == north);
    }

    @Test
    public void shouldGetStrainChooserWhenDealer() {
        assertEquals(west, north.getStrainChooserWhenDealer());
        assertEquals(north, east.getStrainChooserWhenDealer());
        assertEquals(east, south.getStrainChooserWhenDealer());
        assertEquals(south, west.getStrainChooserWhenDealer());
    }

    @Test
    public void shouldGetCompleteName() {
        assertEquals(NORTH_COMPLETE_NAME, north.getCompleteName());
        assertEquals(EAST_COMPLETE_NAME, east.getCompleteName());
        assertEquals(SOUTH_COMPLETE_NAME, south.getCompleteName());
        assertEquals(WEST_COMPLETE_NAME, west.getCompleteName());
    }

    @Test
    public void shouldGetAbbreviation() {
        assertEquals(NORTH_ABBREVIATION, north.getAbbreviation());
        assertEquals(EAST_ABBREVIATION, east.getAbbreviation());
        assertEquals(SOUTH_ABBREVIATION, south.getAbbreviation());
        assertEquals(WEST_ABBREVIATION, west.getAbbreviation());
    }

    @Test
    public void differenceBetweenShouldReturnZeroWhenGivenTwoTimesTheSameDirection() {
        Direction leader = Direction.WEST;

        int result = Direction.differenceBetween(leader, leader);

        assertEquals(0, result);
    }

    @Test
    public void differenceBetweenShouldReturnDistanceBetweenTwoNeighbors() {
        Direction leader = Direction.WEST;
        Direction direction = Direction.SOUTH;
        Direction direction2 = Direction.NORTH;

        assertEquals(3, Direction.differenceBetween(leader, direction));
        assertEquals(1, Direction.differenceBetween(leader, direction2));
    }

}