package club.libridge.libridgebackend.core.comparators;

import java.util.Comparator;

import scalabridge.Card;

public class CardInsideHandComparator implements Comparator<Card> {

    @Override
    public int compare(Card card1, Card card2) {
        int suitDifference = card1.compareSuit(card2);
        if (suitDifference != 0) {
            return -suitDifference;
        } else {
            int rankDifference = card1.compareRank(card2);
            return -rankDifference;
        }
    }

}
