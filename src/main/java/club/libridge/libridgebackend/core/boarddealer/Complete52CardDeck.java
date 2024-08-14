package club.libridge.libridgebackend.core.boarddealer;

import java.util.ArrayDeque;
import java.util.Deque;

import lombok.Getter;
import scalabridge.Card;
import scalabridge.Rank;
import scalabridge.Suit;

public class Complete52CardDeck implements CardDeck {

    @Getter
    private Deque<Card> deck;

    public static final int TOTAL_NUMBER_OF_CARDS = 52;

    public Complete52CardDeck() {
        this.deck = new ArrayDeque<Card>(TOTAL_NUMBER_OF_CARDS);
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(suit, rank);
                this.deck.add(card);
            }
        }
    }

}
