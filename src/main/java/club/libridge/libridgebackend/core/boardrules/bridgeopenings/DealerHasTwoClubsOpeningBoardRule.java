package club.libridge.libridgebackend.core.boardrules.bridgeopenings;

import club.libridge.libridgebackend.core.boardrules.BoardRule;
import scalabridge.CompleteHand;
import scalabridge.DuplicateBoard;
import scalabridge.HandEvaluations;

public class DealerHasTwoClubsOpeningBoardRule extends SingletonEqualsAndHashcode implements BoardRule {

    @Override
    public boolean isValid(DuplicateBoard board) {
        CompleteHand dealerHand = board.getHandOf(board.getDealer());
        HandEvaluations handEvaluations = dealerHand.hand().getHandEvaluations();
        return handEvaluations.getHCP() >= 22;
    }

}
