package club.libridge.libridgebackend.dto;

import club.libridge.libridgebackend.core.Card;
import club.libridge.libridgebackend.core.Rank;
import club.libridge.libridgebackend.core.Suit;
import lombok.Setter;

public class RequestCard {
    @Setter
    private String rank;
    @Setter
    private String suit;

    public Card getCard() {
        Rank finalRank = null;
        Suit finalSuit = null;
        for (Rank currentRank : Rank.values()) {
            if (currentRank.getSymbol().equals(this.rank)) { // FIXME this should be a static created Map<String,Rank>
                finalRank = currentRank;
                break;
            }
        }
        for (Suit currentSuit : Suit.values()) {
            if (currentSuit.getSymbol().equals(this.suit)) { // FIXME this should be a static created Map<String,Suit>
                finalSuit = currentSuit;
                break;
            }
        }
        if (finalRank == null || finalSuit == null) {
            throw new RuntimeException();
        }
        return new Card(finalSuit, finalRank);
    }

}
