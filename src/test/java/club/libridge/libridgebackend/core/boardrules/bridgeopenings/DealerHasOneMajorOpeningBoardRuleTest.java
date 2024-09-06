package club.libridge.libridgebackend.core.boardrules.bridgeopenings;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import club.libridge.libridgebackend.TestWithMocks;
import club.libridge.libridgebackend.core.Board;
import scalabridge.Direction;
import club.libridge.libridgebackend.core.Hand;
import club.libridge.libridgebackend.core.HandEvaluations;

public class DealerHasOneMajorOpeningBoardRuleTest extends TestWithMocks {

    @Mock
    private Board board;
    @Mock
    private Hand hand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasOneMajorOpeningBoardRule subject = new DealerHasOneMajorOpeningBoardRule();

    private boolean hasFiveCardMajorSuit = true;
    private boolean doesNotHaveFiveCardMajorSuit = false;

    @BeforeEach
    public void setup() {
        dealer = Direction.getSouth();
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp, boolean hasFiveCardMajorSuit) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
        when(handEvaluations.hasFiveOrMoreCardsInAMajorSuit()).thenReturn(hasFiveCardMajorSuit);
    }

    @Test
    public void shouldNotOpenOneMajorWithLessThanTwelveHCP() {
        int hcp = 10;
        this.configureParameterizedMocks(hcp, hasFiveCardMajorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenOneMajorWithMoreThanTwentyOneHCP() {
        int hcp = 22;
        this.configureParameterizedMocks(hcp, hasFiveCardMajorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenOneMajorWithLessThanFiveCardInMajorSuit() {
        int hcp = 12;
        this.configureParameterizedMocks(hcp, doesNotHaveFiveCardMajorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenOneMajorWithCorrectHCPAndDistribution() {
        int hcp = 12;
        this.configureParameterizedMocks(hcp, hasFiveCardMajorSuit);

        assertTrue(subject.isValid(board));
    }

}
