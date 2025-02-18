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

public class DealerHasTwoNoTrumpOpeningBoardRuleTest extends TestWithMocks {

    @Mock
    private DuplicateBoard board;
    @Mock
    private CompleteHand hand;
    @Mock
    private Hand oldHand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasTwoNoTrumpOpeningBoardRule subject = new DealerHasTwoNoTrumpOpeningBoardRule();

    private boolean balanced = true;
    private boolean unbalanced = false;
    private int twentyHCP = 20;
    private int twentyOneHCP = 21;

    @BeforeEach
    public void setup() {
        dealer = Direction.getSouth();
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.hand()).thenReturn(oldHand);
        when(hand.hand().getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp, boolean isBalanced) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
        when(handEvaluations.isBalanced()).thenReturn(isBalanced);
    }

    @Test
    public void shouldNotOpenTwoNoTrumpWithLessThanTwentyHCP() {
        int hcp = 19;
        this.configureParameterizedMocks(hcp, balanced);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenTwoNoTrumpWithMoreThanTwentyOneHCP() {
        int hcp = 23;
        this.configureParameterizedMocks(hcp, balanced);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenTwoNoTrumpWithUnbalancedHand() {
        this.configureParameterizedMocks(twentyHCP, unbalanced);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenTwoNoTrumpWithTwentyHCPAndBalancedDistribution() {
        this.configureParameterizedMocks(twentyHCP, balanced);

        assertTrue(subject.isValid(board));
    }

    @Test
    public void shouldOpenTwoNoTrumpWithTwentyOneHCPAndBalancedDistribution() {
        this.configureParameterizedMocks(twentyOneHCP, balanced);

        assertTrue(subject.isValid(board));
    }

}
