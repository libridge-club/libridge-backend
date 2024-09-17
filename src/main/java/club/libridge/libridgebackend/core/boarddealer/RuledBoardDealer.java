package club.libridge.libridgebackend.core.boarddealer;

import java.util.Deque;

import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.boardrules.BoardRule;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import scalabridge.Card;
import scalabridge.Direction;

@AllArgsConstructor
public class RuledBoardDealer implements BoardDealer {

    @NonNull
    private final BoardRule boardRule;

    private static final ShuffledBoardDealer SHUFFLED_BOARD_DEALER = new ShuffledBoardDealer();

    @Override
    public Board dealBoard(Direction dealer, Deque<Card> deck) {
        Board currentBoard;
        do {
            currentBoard = this.createShuffledBoard(dealer, deck);
        } while (!this.boardRule.isValid(currentBoard));
        return currentBoard;
    }

    private Board createShuffledBoard(Direction dealer, Deque<Card> deck) {
        return SHUFFLED_BOARD_DEALER.dealBoard(dealer, deck);
    }

}
