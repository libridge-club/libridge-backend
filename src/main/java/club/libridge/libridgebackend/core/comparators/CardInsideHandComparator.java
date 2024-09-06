package club.libridge.libridgebackend.core.comparators;

import java.util.Comparator;
import java.util.EnumMap;

import scalabridge.Card;
import scalabridge.Suit;

public class CardInsideHandComparator implements Comparator<Card> {

    private static EnumMap<Suit, Integer> suitOrder;

    static {
        suitOrder = new EnumMap<Suit, Integer>(Suit.class);
        suitOrder.put(Suit.getDIAMONDS(), 0);
        suitOrder.put(Suit.getCLUBS(), 1);
        suitOrder.put(Suit.getHEARTS(), 2);
        suitOrder.put(Suit.getSPADES(), 3);
    }

    private int compareSuit(Card card1, Card card2) {
        return suitOrder.get(card1.getSuit()) - suitOrder.get(card2.getSuit());
    }

    @Override
    public int compare(Card card1, Card card2) {
        int suitDifference = compareSuit(card1, card2);
        if (suitDifference != 0) {
            return -suitDifference;
        } else {
            int rankDifference = card1.compareRank(card2);
            return -rankDifference;
        }
    }

}
