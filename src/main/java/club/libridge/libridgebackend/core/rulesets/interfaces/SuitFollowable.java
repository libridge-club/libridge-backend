package club.libridge.libridgebackend.core.rulesets.interfaces;

import club.libridge.libridgebackend.core.Trick;
import lombok.NonNull;
import scalabridge.Card;
import scalabridge.Hand;

public interface SuitFollowable {

    boolean followsSuit(@NonNull Trick trick, @NonNull Hand hand, @NonNull Card card);

}
