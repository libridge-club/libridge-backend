package club.libridge.libridgebackend.core.boardrules.bridgeopenings;

import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.boardrules.BoardRule;
import scalabridge.Hand;
import scalabridge.HandEvaluations;
import scalabridge.Suit;

public class DealerHasThreeWeakOpeningBoardRule extends SingletonEqualsAndHashcode implements BoardRule {

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
        return handEvaluations.hasSevenCardsInLongestSuit() && !handEvaluations.hasFourOrMoreCardsInMajorSuitExcludingLongestSuit()
                && handEvaluations.hasThreeOutOfFiveHigherCards(suit);
    }

}
