package club.libridge.libridgebackend.core.boarddealer;

import java.util.Deque;

import club.libridge.libridgebackend.core.exceptions.ImpossibleBoardException;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;
import scalabridge.HandEvaluations;
import scalabridge.Side;

public class MinibridgeBoardDealer implements BoardDealer {

    private static final int MAXIMUM_NUMBER_OF_TRIES = 1000000;
    private final ShuffledBoardDealer shuffledBoardDealer;

    public MinibridgeBoardDealer() {
        this.shuffledBoardDealer = new ShuffledBoardDealer();
    }

    /**
     * Deals a Board where the partnership of the dealer has strictly more points than the other partnership and the dealer has more or equal HCP than
     * its partner.
     */
    @Override
    public DuplicateBoard dealBoard(Direction dealer, Deque<Card> deck) {
        int dealerPartnershipHCP;
        int nonDealerPartnershipHCP;
        int numberOfTries = 0;
        int dealerHCP;
        boolean isDealerMaximum;
        DuplicateBoard board;

        do {
            dealerPartnershipHCP = 0;
            nonDealerPartnershipHCP = 0;
            board = shuffledBoardDealer.dealBoard(dealer, deck);
            dealerHCP = board.getHandOf(dealer).hand().getHandEvaluations().getHCP();
            isDealerMaximum = true;
            for (Direction direction : Direction.values()) {
                HandEvaluations handEvaluations = board.getHandOf(direction).hand().getHandEvaluations();
                int hcp = handEvaluations.getHCP();
                if (Side.getFromDirection(direction) == Side.getFromDirection(dealer)) {
                    dealerPartnershipHCP += hcp;
                } else {
                    nonDealerPartnershipHCP += hcp;
                }
                if (direction != dealer && hcp > dealerHCP) {
                    isDealerMaximum = false;
                }
            }
            if (numberOfTries > MAXIMUM_NUMBER_OF_TRIES) {
                throw new ImpossibleBoardException();
            } else {
                numberOfTries++;
            }
        } while (dealerPartnershipHCP < nonDealerPartnershipHCP || !isDealerMaximum);

        return board;
    }

}
