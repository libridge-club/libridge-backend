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

public class DealerHasTwoClubsOpeningBoardRuleTest extends TestWithMocks {

    @Mock
    private DuplicateBoard board;
    @Mock
    private CompleteHand hand;
    @Mock
    private Hand oldHand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasTwoClubsOpeningBoardRule subject = new DealerHasTwoClubsOpeningBoardRule();

    private int twentyTwoHCP = 22;

    @BeforeEach
    public void setup() {
        dealer = Direction.getSouth();
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.hand()).thenReturn(oldHand);
        when(hand.hand().getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
    }

    @Test
    public void shouldNotOpenTwoClubsWithLessThanTwentyTwoHCP() {
        int hcp = 21;
        this.configureParameterizedMocks(hcp);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenTwoClubsWithTwentyTwoHCP() {
        this.configureParameterizedMocks(twentyTwoHCP);

        assertTrue(subject.isValid(board));
    }

    @Test
    public void shouldOpenTwoClubsWithMoreThanTwentyTwoHCP() {
        int hcp = 23;
        this.configureParameterizedMocks(hcp);

        assertTrue(subject.isValid(board));
    }

}
