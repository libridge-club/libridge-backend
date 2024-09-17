package club.libridge.libridgebackend.core.boarddealer;

import java.util.Deque;

import club.libridge.libridgebackend.core.Board;
import lombok.AllArgsConstructor;
import scalabridge.Card;
import scalabridge.Direction;

@AllArgsConstructor
public class ShuffledBoardDealerWithSeed implements BoardDealer {

    private final long seed;

    @Override
    public Board dealBoard(Direction dealer, Deque<Card> deck) {
        return new ShuffledBoardDealer().dealBoard(dealer, deck, seed);
    }

}
