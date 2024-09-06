package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import scalabridge.Direction;

class PlasticBoardTest {

    @Test
    void getDealerFromBoardNumber_oneShouldReturnNorth() {
        BoardNumber boardNumberOne = new BoardNumber(1);

        assertEquals(Direction.getNorth(), PlasticBoard.getDealerFromBoardNumber(boardNumberOne));
    }

    @Test
    void getDealerFromBoardNumber_twoShouldReturnEast() {
        BoardNumber boardNumberTwo = new BoardNumber(2);

        assertEquals(Direction.getEast(), PlasticBoard.getDealerFromBoardNumber(boardNumberTwo));
    }

    @Test
    void getDealerFromBoardNumber_threeShouldReturnSouth() {
        BoardNumber boardNumberThree = new BoardNumber(3);

        assertEquals(Direction.getSouth(), PlasticBoard.getDealerFromBoardNumber(boardNumberThree));
    }

    @Test
    void getDealerFromBoardNumber_fourShouldReturnWest() {
        BoardNumber boardNumberFour = new BoardNumber(4);

        assertEquals(Direction.getWest(), PlasticBoard.getDealerFromBoardNumber(boardNumberFour));
    }

    @Test
    void getDealerFromBoardNumber_shouldReturnClockwiseForIncreasingNumbers() {
        int currentNumber = 0;
        Direction currentDirection = Direction.getWest();

        for (int i = 1; i <= 64; i++) {
            currentNumber++;
            currentDirection = currentDirection.next();
            BoardNumber currentBoardNumber = new BoardNumber(currentNumber);
            assertEquals(currentDirection, PlasticBoard.getDealerFromBoardNumber(currentBoardNumber));
        }

    }

}
