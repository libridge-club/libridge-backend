package club.libridge.libridgebackend.core.boardrules.bridgeopenings;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import club.libridge.libridgebackend.TestWithMocks;
import scalabridge.CompleteHand;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;
import scalabridge.Hand;
import scalabridge.HandEvaluations;

public class DealerHasOneNoTrumpOpeningBoardRuleTest extends TestWithMocks {

    @Mock
    private DuplicateBoard board;
    @Mock
    private CompleteHand hand;
    @Mock
    private Hand oldHand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasOneNoTrumpOpeningBoardRule subject = new DealerHasOneNoTrumpOpeningBoardRule();

    private boolean balanced = true;
    private boolean unbalanced = false;
    private boolean hasFiveCardMajorSuit = true;
    private boolean doestNotHaveFiveCardMajorSuit = false;
    private int sixteenHCP = 16;
    private int fourteenHCP = 14;
    private int eighteenHCP = 18;

    @BeforeEach
    public void setup() {
        dealer = Direction.getSouth();
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.hand()).thenReturn(oldHand);
        when(hand.hand().getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp, boolean isBalanced, boolean hasFiveCardMajorSuit) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
        when(handEvaluations.isBalanced()).thenReturn(isBalanced);
        when(handEvaluations.hasFiveOrMoreCardsInAMajorSuit()).thenReturn(hasFiveCardMajorSuit);
    }

    @Test
    public void shouldNotOpenOneNoTrumpWithFourteenHCP() {
        this.configureParameterizedMocks(fourteenHCP, balanced, doestNotHaveFiveCardMajorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenOneNoTrumpWithEighteenHCP() {
        this.configureParameterizedMocks(eighteenHCP, balanced, doestNotHaveFiveCardMajorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenOneNoTrumpWithUnbalancedHand() {
        this.configureParameterizedMocks(sixteenHCP, unbalanced, doestNotHaveFiveCardMajorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenOneNoTrumpWithFiveCardMajor() {
        this.configureParameterizedMocks(sixteenHCP, balanced, hasFiveCardMajorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenOneNoTrumpWithCorrectHCPAndDistribution() {
        this.configureParameterizedMocks(sixteenHCP, balanced, doestNotHaveFiveCardMajorSuit);

        assertTrue(subject.isValid(board));
    }

}
