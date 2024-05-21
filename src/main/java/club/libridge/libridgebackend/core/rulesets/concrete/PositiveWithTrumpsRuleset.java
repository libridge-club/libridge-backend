package club.libridge.libridgebackend.core.rulesets.concrete;

import club.libridge.libridgebackend.core.Suit;
import club.libridge.libridgebackend.core.comparators.CardInsideHandWithSuitComparator;
import club.libridge.libridgebackend.core.rulesets.implementations.DefaultSuitFollowable;
import club.libridge.libridgebackend.core.rulesets.implementations.TrumpSuitWinnable;

public class PositiveWithTrumpsRuleset extends PositiveRuleset {

    /**
     * @deprecated Kryo needs a no-arg constructor
     * FIXME kryo is not used anymore. Does jackson or spring web needs this?
     */
    @Deprecated
    @SuppressWarnings("unused")
    private PositiveWithTrumpsRuleset() {
    }

    private Suit trumpSuit;

    public PositiveWithTrumpsRuleset(Suit trumpSuit) {
        super();
        this.suitFollowable = new DefaultSuitFollowable();
        this.winnable = new TrumpSuitWinnable(trumpSuit);
        this.trumpSuit = trumpSuit;
        this.cardComparator = new CardInsideHandWithSuitComparator(this.trumpSuit);
    }

    public Suit getTrumpSuit() {
        return trumpSuit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((trumpSuit == null) ? 0 : trumpSuit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PositiveWithTrumpsRuleset other = (PositiveWithTrumpsRuleset) obj;
        if (trumpSuit != other.trumpSuit) {
            return false;
        }
        return true;
    }

}
