package club.libridge.libridgebackend.core.boarddealer;

import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;

import club.libridge.libridgebackend.core.Board;
import scalabridge.Card;
import scalabridge.Direction;
import club.libridge.libridgebackend.core.Hand;
import club.libridge.libridgebackend.core.ShuffledDeck;
import club.libridge.libridgebackend.core.boardrules.BoardRule;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class RuledBoardDealer implements BoardDealer {

    @NonNull
    private final BoardRule boardRule;

    @Override
    public Board dealBoard(Direction dealer, Deque<Card> deck) {
        Board currentBoard;
        do {
            currentBoard = this.createShuffledBoard(dealer, deck);
        } while (!this.boardRule.isValid(currentBoard));
        return currentBoard;
    }

    private Board createShuffledBoard(Direction dealer, Deque<Card> deck) {
        Map<Direction, Hand> hands = new EnumMap<Direction, Hand>(Direction.class);
        Direction currentDirection;
        ShuffledDeck currentDeck;

        currentDeck = new ShuffledDeck(deck);
        for (Direction direction : Direction.values()) {
            hands.put(direction, new Hand());
        }
        currentDirection = dealer;

        while (currentDeck.hasCard()) {
            hands.get(currentDirection).addCard(currentDeck.dealCard());
            currentDirection = currentDirection.next();
        }
        return new Board(hands, dealer);
    }

}
