package club.libridge.libridgebackend.core.game;

import java.util.Deque;

import club.libridge.libridgebackend.core.Deal;
import club.libridge.libridgebackend.core.Player;
import club.libridge.libridgebackend.core.rulesets.abstractrulesets.Ruleset;
import lombok.Getter;
import lombok.NonNull;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;

public abstract class TrickGame {

    @Getter
    protected DuplicateBoard currentBoard;
    @Getter
    protected Deal currentDeal;
    @Getter
    protected Direction dealer;
    protected final Deque<Card> gameDeck;

    protected TrickGame(@NonNull Deque<Card> gameDeck) {
        dealer = Direction.getNorth();
        this.gameDeck = gameDeck;
    }

    public abstract void dealNewBoard();

    public abstract boolean isFinished();

    public abstract void finishDeal();

    public abstract Direction getLeader();

    public void setPlayerOf(Direction direction, Player player) {
        this.currentDeal.setPlayerOf(direction, player);
    }

    public void setRuleset(Ruleset ruleset) {
        this.currentDeal.setRuleset(ruleset);
    }

}
