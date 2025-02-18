package club.libridge.libridgebackend.core.boardrules.bridgeopenings;

import club.libridge.libridgebackend.core.boardrules.BoardRule;
import scalabridge.CompleteHand;
import scalabridge.DuplicateBoard;
import scalabridge.HandEvaluations;

public class DealerHasOneMajorOpeningBoardRule extends SingletonEqualsAndHashcode implements BoardRule {

    @Override
    public boolean isValid(DuplicateBoard board) {
        CompleteHand dealerHand = board.getHandOf(board.getDealer());
        HandEvaluations handEvaluations = dealerHand.hand().getHandEvaluations();
        return hasCorrectHCPRange(handEvaluations) && hasCorrectDistribution(handEvaluations);
    }

    private boolean hasCorrectHCPRange(HandEvaluations handEvaluations) {
        return handEvaluations.getHCP() >= 12 && handEvaluations.getHCP() <= 21;
    }

    private boolean hasCorrectDistribution(HandEvaluations handEvaluations) {
        return handEvaluations.hasFiveOrMoreCardsInAMajorSuit();
    }

}
