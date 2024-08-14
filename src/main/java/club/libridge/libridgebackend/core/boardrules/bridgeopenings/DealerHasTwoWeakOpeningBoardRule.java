package club.libridge.libridgebackend.core.boardrules.bridgeopenings;

import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.Hand;
import club.libridge.libridgebackend.core.HandEvaluations;
import scalabridge.Suit;
import club.libridge.libridgebackend.core.boardrules.BoardRule;

public class DealerHasTwoWeakOpeningBoardRule extends SingletonEqualsAndHashcode implements BoardRule {

    @Override
    public boolean isValid(Board board) {
        Hand dealerHand = board.getHandOf(board.getDealer());
        HandEvaluations handEvaluations = dealerHand.getHandEvaluations();
        return hasCorrectHCPRange(handEvaluations) && hasCorrectDistribution(handEvaluations);
    }

    private boolean hasCorrectHCPRange(HandEvaluations handEvaluations) {
        return handEvaluations.getHCP() >= 6 && handEvaluations.getHCP() <= 10;
    }

    private boolean hasCorrectDistribution(HandEvaluations handEvaluations) {
        Suit suit = handEvaluations.getLongestSuit();
        if (Suit.CLUBS.equals(suit)) { // There is no weak 2C bid.
            return false;
        }
        return handEvaluations.hasSixCardsInLongestSuit() && !handEvaluations.hasFourOrMoreCardsInMajorSuitExcludingLongestSuit()
                && handEvaluations.hasTwoOutOfThreeHigherCards(suit);
    }

}
