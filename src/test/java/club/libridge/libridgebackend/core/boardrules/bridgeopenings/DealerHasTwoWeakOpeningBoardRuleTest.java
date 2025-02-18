package club.libridge.libridgebackend.core.boardrules.bridgeopenings;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

public class DealerHasTwoWeakOpeningBoardRuleTest extends TestWithMocks {

    @Mock
    private DuplicateBoard board;
    @Mock
    private CompleteHand hand;
    @Mock
    private Hand oldHand;
    @Mock
    private HandEvaluations handEvaluations;
    private Direction dealer;
    private DealerHasTwoWeakOpeningBoardRule subject = new DealerHasTwoWeakOpeningBoardRule();

    private boolean hasSixCardInAnySuit = true;
    private boolean doesNotHaveSixCardInAnySuit = false;
    private boolean hasTwoOutOfThreeHigherCards = true;
    private boolean doesNotHaveTwoOutOfThreeHigherCards = false;
    private boolean hasFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards = true;
    private boolean doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards = false;

    @BeforeEach
    public void setup() {
        dealer = Direction.getSouth();
        when(board.getDealer()).thenReturn(dealer);
        when(board.getHandOf(dealer)).thenReturn(hand);
        when(hand.hand()).thenReturn(oldHand);
        when(hand.hand().getHandEvaluations()).thenReturn(handEvaluations);
    }

    private void configureParameterizedMocks(int hcp, boolean hasSixCardsInLongestSuit, boolean hasTwoOutOfThreeHigherCards,
            boolean hasFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards) {
        when(handEvaluations.getHCP()).thenReturn(hcp);
        when(handEvaluations.hasSixCardsInLongestSuit()).thenReturn(hasSixCardsInLongestSuit);
        when(handEvaluations.hasTwoOutOfThreeHigherCards(any())).thenReturn(hasTwoOutOfThreeHigherCards);
        when(handEvaluations.hasFourOrMoreCardsInMajorSuitExcludingLongestSuit()).thenReturn(hasFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);
    }

    @Test
    public void shouldNotOpenTwoWeakWithLessThanSixHCP() {
        int hcp = 5;
        this.configureParameterizedMocks(hcp, hasSixCardInAnySuit, hasTwoOutOfThreeHigherCards,
                doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenTwoWeakWithMoreThanTenHCP() {
        int hcp = 12;
        this.configureParameterizedMocks(hcp, hasSixCardInAnySuit, hasTwoOutOfThreeHigherCards,
                doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenTwoWeakWithFourCardsInTheMajorSuitExcludingLongestSuit() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, hasSixCardInAnySuit, hasTwoOutOfThreeHigherCards,
                hasFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenTwoWeakWithoutTwoHighCardsInLongestSuit() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, hasSixCardInAnySuit, doesNotHaveTwoOutOfThreeHigherCards,
                doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldNotOpenTwoWeakWithoutSixCardsInTheLongestSuit() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, doesNotHaveSixCardInAnySuit, hasTwoOutOfThreeHigherCards,
                doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertFalse(subject.isValid(board));
    }

    @Test
    public void shouldOpenTwoWeakWithCorrectHCPAndDistribution() {
        int hcp = 9;
        this.configureParameterizedMocks(hcp, hasSixCardInAnySuit, hasTwoOutOfThreeHigherCards,
                doesNotHaveFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards);

        assertTrue(subject.isValid(board));
    }

}
