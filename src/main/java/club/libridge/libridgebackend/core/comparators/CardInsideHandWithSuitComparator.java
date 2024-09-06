package club.libridge.libridgebackend.core.comparators;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

import scalabridge.Card;
import scalabridge.Suit;
import lombok.NonNull;

public class CardInsideHandWithSuitComparator implements Comparator<Card> {

    private static Map<Suit, Map<Suit, Integer>> suitOrders;

    static {
        Map<Suit, Integer> whenSpades = new EnumMap<Suit, Integer>(Suit.class);
        whenSpades.put(Suit.getSPADES(), 0);
        whenSpades.put(Suit.getHEARTS(), 1);
        whenSpades.put(Suit.getCLUBS(), 2);
        whenSpades.put(Suit.getDIAMONDS(), 3);

        Map<Suit, Integer> whenHearts = new EnumMap<Suit, Integer>(Suit.class);
        whenHearts.put(Suit.getHEARTS(), 0);
        whenHearts.put(Suit.getSPADES(), 1);
        whenHearts.put(Suit.getDIAMONDS(), 2);
        whenHearts.put(Suit.getCLUBS(), 3);

        Map<Suit, Integer> whenDiamonds = new EnumMap<Suit, Integer>(Suit.class);
        whenDiamonds.put(Suit.getDIAMONDS(), 0);
        whenDiamonds.put(Suit.getSPADES(), 1);
        whenDiamonds.put(Suit.getHEARTS(), 2);
        whenDiamonds.put(Suit.getCLUBS(), 3);

        Map<Suit, Integer> whenClubs = new EnumMap<Suit, Integer>(Suit.class);
        whenClubs.put(Suit.getCLUBS(), 0);
        whenClubs.put(Suit.getHEARTS(), 1);
        whenClubs.put(Suit.getSPADES(), 2);
        whenClubs.put(Suit.getDIAMONDS(), 3);

        suitOrders = new EnumMap<>(Suit.class);
        suitOrders.put(Suit.getSPADES(), whenSpades);
        suitOrders.put(Suit.getHEARTS(), whenHearts);
        suitOrders.put(Suit.getDIAMONDS(), whenDiamonds);
        suitOrders.put(Suit.getCLUBS(), whenClubs);
    }

    private Map<Suit, Integer> suitOrder;

    /**
     * @deprecated Kryo needs a no-arg constructor FIXME kryo is not used anymore. Does jackson or spring web needs this?
     */
    @Deprecated
    @SuppressWarnings("unused")
    private CardInsideHandWithSuitComparator() {
    }

    public CardInsideHandWithSuitComparator(@NonNull Suit suit) {
        this.suitOrder = suitOrders.get(suit);
    }

    @Override
    public int compare(Card card1, Card card2) {
        int suitDifference = suitOrder.get(card2.getSuit()) - suitOrder.get(card1.getSuit());
        if (suitDifference != 0) {
            return -suitDifference;
        } else {
            int rankDifference = card1.compareRank(card2);
            return -rankDifference;
        }
    }

}
