package club.libridge.libridgebackend.core.rulesets.abstractrulesets;

import java.util.Comparator;

import scalabridge.Card;
import scalabridge.Direction;
import club.libridge.libridgebackend.core.Hand;
import club.libridge.libridgebackend.core.Trick;
import club.libridge.libridgebackend.core.comparators.CardInsideHandComparator;
import club.libridge.libridgebackend.core.rulesets.interfaces.CardComparable;
import club.libridge.libridgebackend.core.rulesets.interfaces.SuitFollowable;
import club.libridge.libridgebackend.core.rulesets.interfaces.Winnable;
import lombok.Getter;

public abstract class Ruleset implements SuitFollowable, Winnable, CardComparable {

    protected SuitFollowable suitFollowable;
    protected Winnable winnable;
    @Getter
    protected Comparator<Card> cardComparator;

    public Ruleset() {
        super();
        this.cardComparator = new CardInsideHandComparator();
    }

    @Override
    public boolean followsSuit(Trick trick, Hand hand, Card card) {
        return suitFollowable.followsSuit(trick, hand, card);
    }

    @Override
    public Direction getWinner(Trick trick) {
        return winnable.getWinner(trick);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<? extends Ruleset> myClass = this.getClass();
        return myClass.equals(obj.getClass());
    }

}
