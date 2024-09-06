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

public class DealerHasTwoClubsOpeningBoardRuleTest extends TestWithMocks {

    @Mock
    private Board board;
    @Mock
    private Hand hand;
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
        when(hand.getHandEvaluations()).thenReturn(handEvaluations);
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
