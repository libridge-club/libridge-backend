package club.libridge.libridgebackend.pbn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.boarddealer.Complete52CardDeck;
import club.libridge.libridgebackend.core.boarddealer.ShuffledBoardDealer;
import club.libridge.libridgebackend.core.boarddealer.ShuffledBoardDealerWithSeed;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.Hand;
import scalabridge.Rank;
import scalabridge.Suit;

public class PBNUtilsTest {

    @Test
    void testPbnStringFromBoard() {
        ShuffledBoardDealer shuffledBoardDealer = new ShuffledBoardDealer();
        Board dealBoard = shuffledBoardDealer.dealBoard(Direction.getNorth(), new Complete52CardDeck().getDeck());

        String response = PBNUtils.dealTagStringFromBoard(dealBoard);

        assertEquals('N', response.charAt(0));

    }

    @Test
    void testDealTagStringFromBoardAndDirection() {

        long arbitrarySeed = 123L;
        ShuffledBoardDealerWithSeed shuffledBoardDealerWithSeed = new ShuffledBoardDealerWithSeed(arbitrarySeed);
        Board boardWithSeed = shuffledBoardDealerWithSeed.dealBoard(Direction.getNorth(), new Complete52CardDeck().getDeck());
        String expectedString = "E:86.KT2.Q9742.K85 KJT932.97.86.942 54.8653.3.AQJT73 AQ7.AQJ4.AKJT5.6";

        String response = PBNUtils.dealTagStringFromBoardAndDirection(boardWithSeed, Direction.getEast());

        assertEquals(expectedString, response);

    }

    @Test
    void getBoardFromDealTag_shouldReturnABoardWithTheRightHandsAndDealer() {

        String inputString = "E:86.KT2.K85.Q9742 KJT932.97.942.86 54.8653.AQJT73.3 AQ7.AQJ4.6.AKJT5";

        Board response = PBNUtils.getBoardFromDealTag(inputString);

        assertEquals(response.getDealer(), Direction.getEast());

        Hand east = response.getHandOf(Direction.getEast());
        assertTrue(east.containsCard(new Card(Suit.getSPADES(), Rank.getEIGHT())));
        assertFalse(east.containsCard(new Card(Suit.getSPADES(), Rank.getACE())));
        assertTrue(east.containsCard(new Card(Suit.getHEARTS(), Rank.getKING())));

        Hand north = response.getHandOf(Direction.getNorth());
        assertTrue(north.containsCard(new Card(Suit.getSPADES(), Rank.getQUEEN())));
        assertFalse(north.containsCard(new Card(Suit.getDIAMONDS(), Rank.getACE())));
        assertTrue(north.containsCard(new Card(Suit.getCLUBS(), Rank.getJACK())));
    }

}
