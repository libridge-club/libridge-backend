package club.libridge.libridgebackend.core.rulesets.implementations;

import club.libridge.libridgebackend.core.rulesets.interfaces.Winnable;
import lombok.NonNull;
import scalabridge.Direction;
import scalabridge.Trick;

public class NoTrumpSuitWinnable implements Winnable {

    @Override
    public Direction getWinner(@NonNull Trick trick) {
        return trick.getWinnerWithoutTrumpSuit().get();
    }

}
