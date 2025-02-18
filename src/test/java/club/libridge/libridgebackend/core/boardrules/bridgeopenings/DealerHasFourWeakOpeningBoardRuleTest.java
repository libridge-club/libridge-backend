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

public class DealerHasFourWeakOpeningBoardRuleTest extends TestWithMocks {

    @Mock
    private DuplicateBoard board;
    @Mock
    private CompleteHand hand;
    @Mock
    private Hand oldHand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasFourWeakOpeningBoardRule subject = new DealerHasFourWeakOpeningBoardRule();

    private boolean hasEightCardInAnySuit = true;
    private boolean doesNotHaveEightCardInAnySuit = false;

    @BeforeEach
    public void setup() {
        dealer = Direction.getSouth();
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.hand()).thenReturn(oldHand);
        when(hand.hand().getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp, boolean hasEightCardInAnySuit) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
        when(handEvaluations.hasEightOrMoreCardsInAnySuit()).thenReturn(hasEightCardInAnySuit);
    }

    @Test
    public void shouldNotOpenFourWeakWithLessThanSixHCP() {
        int hcp = 5;
        this.configureParameterizedMocks(hcp, hasEightCardInAnySuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenFourWeakWithMoreThanTenHCP() {
        int hcp = 12;
        this.configureParameterizedMocks(hcp, hasEightCardInAnySuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenFourWeakWithLessThanEightCardInMajorSuit() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, doesNotHaveEightCardInAnySuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenFourWeakWithCorrectHCPAndDistribution() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, hasEightCardInAnySuit);

        assertTrue(subject.isValid(board));
    }

}
