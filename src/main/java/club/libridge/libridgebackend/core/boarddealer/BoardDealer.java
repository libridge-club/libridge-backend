package club.libridge.libridgebackend.core.boarddealer;

import java.util.Deque;

import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;

public interface BoardDealer {

    DuplicateBoard dealBoard(Direction dealer, Deque<Card> deck);

}
