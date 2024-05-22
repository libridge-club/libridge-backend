package club.libridge.libridgebackend.core.boarddealer;

import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;

import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.Card;
import club.libridge.libridgebackend.core.Direction;
import club.libridge.libridgebackend.core.Hand;
import club.libridge.libridgebackend.core.HandEvaluations;
import club.libridge.libridgebackend.core.exceptions.ImpossibleBoardException;

public class MinibridgeBoardDealer implements BoardDealer {

    private static final int MAXIMUM_NUMBER_OF_TRIES = 1000000;
    private Board board;
    private ShuffledBoardDealer shuffledBoardDealer;

    public MinibridgeBoardDealer() {
        this.shuffledBoardDealer = new ShuffledBoardDealer();
    }

    /**
     * Deals a Board where the partnership of the dealer has strictly more points
     * than the other partnership and the dealer has more or equal HCP than its
     * partner.
     */
    @Override
    public Board dealBoard(Direction dealer, Deque<Card> deck) {
        int dealerPartnershipHCP;
        int nonDealerPartnershipHCP;
        int numberOfTries = 0;

        do {
            dealerPartnershipHCP = 0;
            nonDealerPartnershipHCP = 0;
            this.board = shuffledBoardDealer.dealBoard(dealer, deck);
            for (Direction direction : Direction.values()) {
                HandEvaluations handEvaluations = this.board.getHandOf(direction).getHandEvaluations();
                int hcp = handEvaluations.getHCP();
                if (direction.isNorthSouth() == dealer.isNorthSouth()) {
                    dealerPartnershipHCP += hcp;
                } else {
                    nonDealerPartnershipHCP += hcp;
                }
            }
            if (numberOfTries > MAXIMUM_NUMBER_OF_TRIES) {
                throw new ImpossibleBoardException();
            } else {
                numberOfTries++;
            }
        } while (dealerPartnershipHCP == nonDealerPartnershipHCP);

        if (dealerPartnershipHCP < nonDealerPartnershipHCP) {
            this.board = this.rotateHands(this.board, 1);
        }

        HandEvaluations dealerHandEvaluations = this.board.getHandOf(dealer).getHandEvaluations();
        HandEvaluations dealerPartnerHandEvaluations = this.board.getHandOf(dealer.next(2)).getHandEvaluations();
        if (dealerHandEvaluations.getHCP() < dealerPartnerHandEvaluations.getHCP()) {
            this.board = this.rotateHands(this.board, 2);
        }

        return this.board;
    }

    private Board rotateHands(Board board, int i) {
        Map<Direction, Hand> hands = new EnumMap<Direction, Hand>(Direction.class);
        for (Direction direction : Direction.values()) {
            hands.put(direction.next(i), board.getHandOf(direction));
        }
        return new Board(hands, board.getDealer());
    }

}
