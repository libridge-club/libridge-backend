package club.libridge.libridgebackend.core.boarddealer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Deque;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import club.libridge.libridgebackend.core.Board;
import scalabridge.Card;
import scalabridge.Direction;

public class MinibridgeBoardDealerTest {

    private static MinibridgeBoardDealer subject;
    private static Direction anyDirection;
    private static Deque<Card> completeDeck;

    @BeforeAll
    public static void setup() {
        subject = new MinibridgeBoardDealer();
        anyDirection = Direction.SOUTH;
        completeDeck = new Complete52CardDeck().getDeck();
    }

    @Test
    public void dealBoardShouldDealABoardWithTheCorrectDealer() {
        Board minibridgeBoard = subject.dealBoard(anyDirection, completeDeck);

        assertEquals(anyDirection, minibridgeBoard.getDealer());
    }

    @Test
    public void dealBoardShouldDealABoardWithStrictlyMoreHCPForDealerPartnership() {
        Board minibridgeBoard = subject.dealBoard(anyDirection, completeDeck);

        int dealerPartnershipHCP = 0;
        int nonDealerPartnershipHCP = 0;
        for (Direction direction : Direction.values()) {
            int currentDirectionHCP = minibridgeBoard.getHandOf(direction).getHandEvaluations().getHCP();
            if (direction.isNorthSouth() == anyDirection.isNorthSouth()) {
                dealerPartnershipHCP += currentDirectionHCP;
            } else {
                nonDealerPartnershipHCP += currentDirectionHCP;
            }
        }
        assertTrue(dealerPartnershipHCP > nonDealerPartnershipHCP);
    }

    @Test
    public void dealBoardShouldDealABoardWithEqualOrMoreHCPForDealerThanTheirPartner() {
        Board minibridgeBoard = subject.dealBoard(anyDirection, completeDeck);

        int dealerHCP = minibridgeBoard.getHandOf(anyDirection).getHandEvaluations().getHCP();
        int dealerPartnerHCP = minibridgeBoard.getHandOf(anyDirection.next(2)).getHandEvaluations().getHCP();
        assertTrue(dealerHCP >= dealerPartnerHCP);
    }

}
