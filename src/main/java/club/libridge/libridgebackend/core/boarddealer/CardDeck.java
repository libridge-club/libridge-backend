package club.libridge.libridgebackend.core.boarddealer;

import java.util.Deque;

import scalabridge.Card;

public interface CardDeck {

  Deque<Card> getDeck();

}
