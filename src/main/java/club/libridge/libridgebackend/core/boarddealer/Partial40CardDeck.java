package club.libridge.libridgebackend.core.boarddealer;

import java.util.ArrayDeque;
import java.util.Deque;

import lombok.Getter;
import scalabridge.Card;
import scalabridge.Rank;
import scalabridge.Suit;

public class Partial40CardDeck implements CardDeck {

    @Getter
    private final Deque<Card> deck;

    public Partial40CardDeck() {
        this.deck = new ArrayDeque<Card>();
        Rank.getACE().compareTo(Rank.getACE());
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                if (rank.compareTo(Rank.getFIVE()) >= 0) { // Rank is greater or equal than five
                    Card card = new Card(suit, rank);
                    this.deck.add(card);
                }
            }
        }
    }

}
