package club.libridge.libridgebackend.core.rulesets.implementations;

import scalabridge.Direction;
import scalabridge.Suit;
import club.libridge.libridgebackend.core.Trick;
import club.libridge.libridgebackend.core.rulesets.interfaces.Winnable;
import lombok.NonNull;

public class TrumpSuitWinnable implements Winnable {

    /**
     * @deprecated Kryo needs a no-arg constructor
     * FIXME kryo is not used anymore. Does jackson or spring web needs this?
     */
    @Deprecated
    @SuppressWarnings("unused")
    private TrumpSuitWinnable() {
    }

    private Suit trumpSuit;

    public TrumpSuitWinnable(@NonNull Suit trumpSuit) {
        this.trumpSuit = trumpSuit;
    }

    @Override
    public Direction getWinner(@NonNull Trick trick) {
        return trick.getWinnerWithTrumpSuit(this.trumpSuit);
    }

}
