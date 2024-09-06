package club.libridge.libridgebackend.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scalabridge.Card;
import scalabridge.Rank;
import scalabridge.Suit;

public class HandBuilder {

    static final Map<String, Rank> SYMBOL_TO_RANK_MAP;

    static {
        SYMBOL_TO_RANK_MAP = new HashMap<String, Rank>();
        Arrays.stream(Rank.values()).forEach(rank -> SYMBOL_TO_RANK_MAP.put(rank.getSymbol(), rank));
    }

    /**
     * Examples: q5.kt85.qjt8632. 8.aqj96.ak9.jt93 kt97643.4..ak652 aj2.732.754.q874
     *
     */
    public Hand buildFromDotSeparatedString(String dotSeparatedString) {
        Hand returnValue = new Hand();
        int enforceFourSuitsLimit = 4;
        String[] suits = dotSeparatedString.toUpperCase().split("\\.", enforceFourSuitsLimit);
        getCardsFromSuitString(Suit.getSPADES(), suits[0]).stream().forEach(card -> returnValue.addCard(card));
        getCardsFromSuitString(Suit.getHEARTS(), suits[1]).stream().forEach(card -> returnValue.addCard(card));
        getCardsFromSuitString(Suit.getDIAMONDS(), suits[2]).stream().forEach(card -> returnValue.addCard(card));
        getCardsFromSuitString(Suit.getCLUBS(), suits[3]).stream().forEach(card -> returnValue.addCard(card));
        return returnValue;
    }

    private List<Card> getCardsFromSuitString(Suit suit, String string) {
        List<Card> returnValue = new ArrayList<Card>();
        for (int i = 0; i < string.length(); i++) {
            String rankSymbol = String.valueOf(string.charAt(i));
            returnValue.add(new Card(suit, SYMBOL_TO_RANK_MAP.get(rankSymbol)));
        }
        return returnValue;
    }

}
