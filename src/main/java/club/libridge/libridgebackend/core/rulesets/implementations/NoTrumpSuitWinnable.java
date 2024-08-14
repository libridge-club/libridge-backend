package club.libridge.libridgebackend.core.rulesets.implementations;

import scalabridge.Direction;
import club.libridge.libridgebackend.core.Trick;
import club.libridge.libridgebackend.core.rulesets.interfaces.Winnable;
import lombok.NonNull;

public class NoTrumpSuitWinnable implements Winnable {

    @Override
    public Direction getWinner(@NonNull Trick trick) {
        return trick.getWinnerWithoutTrumpSuit();
    }

}
