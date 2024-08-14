package club.libridge.libridgebackend.core.rulesets.interfaces;

import scalabridge.Card;
import club.libridge.libridgebackend.core.Hand;
import club.libridge.libridgebackend.core.Trick;
import lombok.NonNull;

public interface SuitFollowable {

    boolean followsSuit(@NonNull Trick trick, @NonNull Hand hand, @NonNull Card card);

}
