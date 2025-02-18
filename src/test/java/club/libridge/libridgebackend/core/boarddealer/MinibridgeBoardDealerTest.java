package club.libridge.libridgebackend.core.boarddealer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Deque;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;
import scalabridge.PositiveInteger;
import scalabridge.Side;

public class MinibridgeBoardDealerTest {

    private static MinibridgeBoardDealer subject;
    private static Direction anyDirection;
    private static Deque<Card> completeDeck;

    @BeforeAll
    public static void setup() {
        subject = new MinibridgeBoardDealer();
        anyDirection = Direction.getSouth();
        completeDeck = new Complete52CardDeck().getDeck();
    }

    @Test
    public void dealBoardShouldDealABoardWithTheCorrectDealer() {
        DuplicateBoard minibridgeBoard = subject.dealBoard(anyDirection, completeDeck);

        assertEquals(anyDirection, minibridgeBoard.getDealer());
    }

    @Test
    public void dealBoardShouldDealABoardWithStrictlyMoreHCPForDealerPartnership() {
        DuplicateBoard minibridgeBoard = subject.dealBoard(anyDirection, completeDeck);

        int dealerPartnershipHCP = 0;
        int nonDealerPartnershipHCP = 0;
        for (Direction direction : Direction.values()) {
            int currentDirectionHCP = minibridgeBoard.getHandOf(direction).hand().getHandEvaluations().getHCP();
            if (Side.getFromDirection(direction) == Side.getFromDirection(anyDirection)) {
                dealerPartnershipHCP += currentDirectionHCP;
            } else {
                nonDealerPartnershipHCP += currentDirectionHCP;
            }
        }
        assertTrue(dealerPartnershipHCP > nonDealerPartnershipHCP);
    }

    @Test
    public void dealBoardShouldDealABoardWithEqualOrMoreHCPForDealerThanTheirPartner() {
        DuplicateBoard minibridgeBoard = subject.dealBoard(anyDirection, completeDeck);

        int dealerHCP = minibridgeBoard.getHandOf(anyDirection).hand().getHandEvaluations().getHCP();
        int dealerPartnerHCP = minibridgeBoard.getHandOf(anyDirection.next(new PositiveInteger(2))).hand().getHandEvaluations().getHCP();
        assertTrue(dealerHCP >= dealerPartnerHCP);
    }

}
