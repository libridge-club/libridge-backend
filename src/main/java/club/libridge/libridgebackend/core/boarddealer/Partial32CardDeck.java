package club.libridge.libridgebackend.core.boarddealer;

import java.util.ArrayDeque;
import java.util.Deque;

import lombok.Getter;
import scalabridge.Card;
import scalabridge.Rank;
import scalabridge.Suit;

public class Partial32CardDeck implements CardDeck {

    @Getter
    private final Deque<Card> deck;

    public Partial32CardDeck() {
        this.deck = new ArrayDeque<Card>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                if (rank.compareTo(Rank.getSEVEN()) >= 0) { // Rank is greater or equal than seven
                    Card card = new Card(suit, rank);
                    this.deck.add(card);
                }
            }
        }
    }

}
