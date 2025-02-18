package club.libridge.libridgebackend.core.boardrules;

import scalabridge.DuplicateBoard;

public interface BoardRule {

    boolean isValid(DuplicateBoard board);

}
