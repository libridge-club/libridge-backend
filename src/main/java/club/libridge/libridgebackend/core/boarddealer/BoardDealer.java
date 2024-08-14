package club.libridge.libridgebackend.core.boarddealer;

import java.util.Deque;

import club.libridge.libridgebackend.core.Board;
import scalabridge.Card;
import scalabridge.Direction;

public interface BoardDealer {

    Board dealBoard(Direction dealer, Deque<Card> deck);

}
