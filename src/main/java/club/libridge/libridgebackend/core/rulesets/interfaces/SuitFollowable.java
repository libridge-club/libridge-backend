package club.libridge.libridgebackend.core.rulesets.interfaces;

import lombok.NonNull;
import scalabridge.Card;
import scalabridge.Hand;
import scalabridge.Trick;

public interface SuitFollowable {

    boolean followsSuit(@NonNull Trick trick, @NonNull Hand hand, @NonNull Card card);

}
