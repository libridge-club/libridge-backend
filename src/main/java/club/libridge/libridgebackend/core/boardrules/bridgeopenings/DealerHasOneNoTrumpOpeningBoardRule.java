package club.libridge.libridgebackend.core.boardrules.bridgeopenings;

import club.libridge.libridgebackend.core.boardrules.BoardRule;
import scalabridge.CompleteHand;
import scalabridge.DuplicateBoard;
import scalabridge.HandEvaluations;

public class DealerHasOneNoTrumpOpeningBoardRule extends SingletonEqualsAndHashcode implements BoardRule {

    @Override
    public boolean isValid(DuplicateBoard board) {
        CompleteHand dealerHand = board.getHandOf(board.getDealer());
        HandEvaluations handEvaluations = dealerHand.hand().getHandEvaluations();
        return hasCorrectHCPRange(handEvaluations) && hasCorrectDistribution(handEvaluations);
    }

    private boolean hasCorrectHCPRange(HandEvaluations handEvaluations) {
        return handEvaluations.getHCP() >= 15 && handEvaluations.getHCP() <= 17;
    }

    private boolean hasCorrectDistribution(HandEvaluations handEvaluations) {
        return handEvaluations.isBalanced() && !handEvaluations.hasFiveOrMoreCardsInAMajorSuit();
    }

}
