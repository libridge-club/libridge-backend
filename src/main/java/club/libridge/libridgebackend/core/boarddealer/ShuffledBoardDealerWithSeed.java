package club.libridge.libridgebackend.core.boarddealer;

import java.util.Deque;

import lombok.AllArgsConstructor;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;

@AllArgsConstructor
public class ShuffledBoardDealerWithSeed implements BoardDealer {

    private final long seed;

    @Override
    public DuplicateBoard dealBoard(Direction dealer, Deque<Card> deck) {
        return new ShuffledBoardDealer().dealBoard(dealer, deck, seed);
    }

}
