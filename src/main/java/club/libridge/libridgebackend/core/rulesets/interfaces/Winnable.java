package club.libridge.libridgebackend.core.rulesets.interfaces;

import lombok.NonNull;
import scalabridge.Direction;
import scalabridge.Trick;

public interface Winnable {

    Direction getWinner(@NonNull Trick trick);

}
