package club.libridge.libridgebackend.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import scalabridge.Direction;

@Getter
@EqualsAndHashCode
public class Score {

    private int northSouthTricks = 0;
    private int eastWestTricks = 0;

    public void addTrickToDirection(@NonNull Trick trick, @NonNull Direction winner) {
        if (winner.isNorthSouth()) {
            this.northSouthTricks++;
        } else {
            this.eastWestTricks++;
        }
    }

    public void subtractTrickFromDirection(@NonNull Trick trick, @NonNull Direction winner) {
        if (winner.isNorthSouth()) {
            this.northSouthTricks--;
        } else {
            this.eastWestTricks--;
        }
    }

    public int getAlreadyPlayedTricks() {
        return this.eastWestTricks + this.northSouthTricks;
    }

    public void finishScore(@NonNull Direction winner, int totalPoints) {
        int remainingPoints = totalPoints - this.getAlreadyPlayedTricks();
        if (winner.isNorthSouth()) {
            this.northSouthTricks += remainingPoints;
        } else {
            this.eastWestTricks += remainingPoints;
        }
    }

}
