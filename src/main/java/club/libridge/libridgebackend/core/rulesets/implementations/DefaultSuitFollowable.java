package club.libridge.libridgebackend.core.rulesets.implementations;

import club.libridge.libridgebackend.core.Card;
import club.libridge.libridgebackend.core.Hand;
import club.libridge.libridgebackend.core.Suit;
import club.libridge.libridgebackend.core.Trick;
import club.libridge.libridgebackend.core.rulesets.interfaces.SuitFollowable;
import lombok.NonNull;

public class DefaultSuitFollowable implements SuitFollowable {

    @Override
    public boolean followsSuit(@NonNull Trick trick, @NonNull Hand hand, @NonNull Card card) {
        if (trick.isEmpty()) {
            return true;
        }
        Suit leadSuitOfTrick = trick.getLeadSuit();
        if (!hand.hasSuit(leadSuitOfTrick) || card.getSuit() == leadSuitOfTrick) {
            return true;
        }
        return false;
    }

}
