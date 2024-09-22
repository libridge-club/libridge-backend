package club.libridge.libridgebackend.core.rulesets.implementations;

import club.libridge.libridgebackend.core.rulesets.interfaces.SuitFollowable;
import lombok.NonNull;
import scalabridge.Card;
import scalabridge.Hand;
import scalabridge.Suit;
import scalabridge.Trick;

public class DefaultSuitFollowable implements SuitFollowable {

    @Override
    public boolean followsSuit(@NonNull Trick trick, @NonNull Hand hand, @NonNull Card card) {
        if (trick.isEmpty()) {
            return true;
        }
        Suit leadSuitOfTrick = trick.getLeadSuit().get();
        if (!hand.hasSuit(leadSuitOfTrick) || card.getSuit() == leadSuitOfTrick) {
            return true;
        }
        return false;
    }

}
