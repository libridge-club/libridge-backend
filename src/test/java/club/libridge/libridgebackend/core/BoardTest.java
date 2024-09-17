package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import club.libridge.libridgebackend.core.comparators.CardInsideHandComparator;
import scalabridge.Card;
import scalabridge.Direction;

public class BoardTest {

    private Direction dealer;
    private Board board;
    private Map<Direction, Hand> hands = new HashMap<Direction, Hand>();

    @BeforeEach
    public void createNorthBoard() {
        dealer = Direction.getNorth();
        hands.clear();
        for (Direction direction : Direction.values()) {
            Direction finalDirection = direction.next(4);
            Hand currentMock = mock(Hand.class);
            hands.put(finalDirection, currentMock);
        }

        board = new Board(hands, dealer);
    }

    @Test
    public void shouldBeConstructedWith4HandsAndADealer() {
        assertNotNull(this.board);
    }

    // @Test
    // public void shouldSortAllHands() { // FIXME when this moves to scalabridge
    // Comparator<Card> comparator = mock(CardInsideHandComparator.class);

    // this.board.sortAllHands(comparator);

    // for (Direction direction : Direction.values()) {
    // verify(this.hands.get(direction), only()).sort(comparator);
    // }
    // }

    @Test
    public void shouldGetCorrectDealer() {
        assertEquals(this.dealer, this.board.getDealer());
    }

    @Test
    public void shouldGetHandOfAllPossibleDirections() {
        for (Direction direction : Direction.values()) {
            assertEquals(this.hands.get(direction), this.board.getHandOf(direction));
        }
    }

}
