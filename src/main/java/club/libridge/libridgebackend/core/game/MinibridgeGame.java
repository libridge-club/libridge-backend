package club.libridge.libridgebackend.core.game;

import java.util.Deque;

import scalabridge.Card;
import club.libridge.libridgebackend.core.Deal;
import scalabridge.Direction;
import club.libridge.libridgebackend.core.boarddealer.BoardDealer;
import club.libridge.libridgebackend.core.boarddealer.MinibridgeBoardDealer;
import club.libridge.libridgebackend.core.rulesets.concrete.PositiveRuleset;
import lombok.NonNull;

public class MinibridgeGame extends TrickGame {

    private BoardDealer boardDealer;

    public MinibridgeGame(@NonNull Deque<Card> gameDeck) {
        super(gameDeck);
        this.boardDealer = new MinibridgeBoardDealer();
    }

    @Override
    public void dealNewBoard() {
        this.currentBoard = boardDealer.dealBoard(this.dealer, this.gameDeck);
        this.currentDeal = new Deal(currentBoard, new PositiveRuleset(), this.getLeader(), true);
        this.currentDeal.setCurrentPlayer(this.dealer.next());
        this.currentDeal.setDummy(this.dealer.next(2));
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void finishDeal() {
        this.dealer = this.dealer.next();
    }

    public Direction getDeclarer() {
        return this.dealer;
    }

    public Direction getDummy() {
        return this.currentDeal.getDummy();
    }

    @Override
    public Direction getLeader() {
        return dealer.next();
    }

}
