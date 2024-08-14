package club.libridge.libridgebackend.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import scalabridge.OddTricks;
import scalabridge.Strain;

@AllArgsConstructor
public final class Contract {
    @NonNull
    private final OddTricks oddTricks;
    @Getter
    @NonNull
    private final Strain strain;
    @NonNull
    private final PenaltyStatus penaltyStatus;
    @Getter
    private final boolean vulnerable;

    public int getLevel() {
        return this.oddTricks.getLevel();
    }

    public boolean isDoubled() {
        return PenaltyStatus.DOUBLED == this.penaltyStatus;
    }

    public boolean isRedoubled() {
        return PenaltyStatus.REDOUBLED == this.penaltyStatus;
    }

    @Override
    public String toString() {
        String response = this.getLevel() + this.getStrain().getSymbol();
        if (this.isDoubled()) {
            response += "X";
        } else if (this.isRedoubled()) {
            response += "XX";
        }
        return response;
    }

}
