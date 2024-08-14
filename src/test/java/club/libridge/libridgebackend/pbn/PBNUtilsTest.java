package club.libridge.libridgebackend.pbn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.Hand;
import club.libridge.libridgebackend.core.boarddealer.Complete52CardDeck;
import club.libridge.libridgebackend.core.boarddealer.ShuffledBoardDealer;
import club.libridge.libridgebackend.core.boarddealer.ShuffledBoardDealerWithSeed;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.Rank;
import scalabridge.Suit;

public class PBNUtilsTest {

    @Test
    void testPbnStringFromBoard() {
        ShuffledBoardDealer shuffledBoardDealer = new ShuffledBoardDealer();
        Board dealBoard = shuffledBoardDealer.dealBoard(Direction.NORTH, new Complete52CardDeck().getDeck());

        String response = PBNUtils.dealTagStringFromBoard(dealBoard);

        assertEquals('N', response.charAt(0));

    }

    @Test
    void testDealTagStringFromBoardAndDirection() {

        long seed = 123L;
        ShuffledBoardDealerWithSeed shuffledBoardDealerWithSeed = new ShuffledBoardDealerWithSeed(seed);
        Board boardWithSeed = shuffledBoardDealerWithSeed.dealBoard(Direction.NORTH, new Complete52CardDeck().getDeck());
        String expectedString = "E:86.KT2.K85.Q9742 KJT932.97.942.86 54.8653.AQJT73.3 AQ7.AQJ4.6.AKJT5";

        String response = PBNUtils.dealTagStringFromBoardAndDirection(boardWithSeed, Direction.EAST);

        assertEquals(expectedString, response);

    }

    @Test
    void getBoardFromDealTag_shouldReturnABoardWithTheRightHandsAndDealer() {

        String inputString = "E:86.KT2.K85.Q9742 KJT932.97.942.86 54.8653.AQJT73.3 AQ7.AQJ4.6.AKJT5";

        Board response = PBNUtils.getBoardFromDealTag(inputString);

        assertEquals(response.getDealer(), Direction.EAST);

        Hand east = response.getHandOf(Direction.EAST);
        assertTrue(east.containsCard(new Card(Suit.SPADES, Rank.EIGHT)));
        assertFalse(east.containsCard(new Card(Suit.SPADES, Rank.ACE)));
        assertTrue(east.containsCard(new Card(Suit.HEARTS, Rank.KING)));

        Hand north = response.getHandOf(Direction.NORTH);
        assertTrue(north.containsCard(new Card(Suit.SPADES, Rank.QUEEN)));
        assertFalse(north.containsCard(new Card(Suit.DIAMONDS, Rank.ACE)));
        assertTrue(north.containsCard(new Card(Suit.CLUBS, Rank.JACK)));
    }

}
