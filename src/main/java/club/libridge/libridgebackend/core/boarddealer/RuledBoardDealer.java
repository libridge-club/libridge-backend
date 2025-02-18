package club.libridge.libridgebackend.core.boarddealer;

import java.util.Deque;

import club.libridge.libridgebackend.core.boardrules.BoardRule;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;

@AllArgsConstructor
public class RuledBoardDealer implements BoardDealer {

    @NonNull
    private final BoardRule boardRule;

    private static final ShuffledBoardDealer SHUFFLED_BOARD_DEALER = new ShuffledBoardDealer();

    @Override
    public DuplicateBoard dealBoard(Direction dealer, Deque<Card> deck) {
        DuplicateBoard currentBoard;
        do {
            currentBoard = this.createShuffledBoard(dealer, deck);
        } while (!this.boardRule.isValid(currentBoard));
        return currentBoard;
    }

    private DuplicateBoard createShuffledBoard(Direction dealer, Deque<Card> deck) {
        return SHUFFLED_BOARD_DEALER.dealBoard(dealer, deck);
    }

}
