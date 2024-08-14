package club.libridge.libridgebackend.core;

import lombok.NonNull;
import scalabridge.Direction;

public final class PlasticBoard {

    public static Direction getDealerFromBoardNumber(@NonNull BoardNumber number) {
        int mod = number.getNumber() % 4;
        return Direction.WEST.next(mod);
    }

}
