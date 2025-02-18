package club.libridge.libridgebackend.core.boardrules.bridgeopenings;

import club.libridge.libridgebackend.core.boardrules.BoardRule;
import scalabridge.DuplicateBoard;

public class AlwaysValidBoardRule extends SingletonEqualsAndHashcode implements BoardRule {

    @Override
    public boolean isValid(DuplicateBoard board) {
        return true;
    }

}
