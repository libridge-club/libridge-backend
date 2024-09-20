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
import scalabridge.Hand;
import scalabridge.HandEvaluations;

public class DealerHasOneMinorOpeningBoardRuleTest extends TestWithMocks {

    @Mock
    private Board board;
    @Mock
    private Hand hand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasOneMinorOpeningBoardRule subject = new DealerHasOneMinorOpeningBoardRule();

    private boolean hasThreeCardMinorSuit = true;
    private boolean doesNotHaveThreeCardMinorSuit = false;

    @BeforeEach
    public void setup() {
        dealer = Direction.getSouth();
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp, boolean hasThreeCardMinorSuit) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
        when(handEvaluations.hasThreeOrMoreCardsInAMinorSuit()).thenReturn(hasThreeCardMinorSuit);
    }

    @Test
    public void shouldNotOpenOneMinorWithLessThanTwelveHCP() {
        int hcp = 10;
        this.configureParameterizedMocks(hcp, hasThreeCardMinorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenOneMinorWithMoreThanTwentyOneHCP() {
        int hcp = 22;
        this.configureParameterizedMocks(hcp, hasThreeCardMinorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenOneMinorWithLessThanThreeCardInMinorSuit() {
        int hcp = 12;
        this.configureParameterizedMocks(hcp, doesNotHaveThreeCardMinorSuit);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenOneMinorWithCorrectHCPAndDistribution() {
        int hcp = 12;
        this.configureParameterizedMocks(hcp, hasThreeCardMinorSuit);

        assertTrue(subject.isValid(board));
    }

}
