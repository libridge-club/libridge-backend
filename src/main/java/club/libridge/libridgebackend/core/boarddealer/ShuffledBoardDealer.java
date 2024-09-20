package club.libridge.libridgebackend.core.boarddealer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.ShuffledDeck;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.Hand;

public class ShuffledBoardDealer implements BoardDealer {

    @Override
    public Board dealBoard(Direction dealer, Deque<Card> deck) {
        return this.dealBoard(dealer, new ShuffledDeck(deck));
    }

    public Board dealBoard(Direction dealer, Deque<Card> deck, long seed) {
        return this.dealBoard(dealer, new ShuffledDeck(deck, seed));
    }

    private Board dealBoard(Direction dealer, ShuffledDeck shuffledDeck) {
        Map<Direction, List<Card>> temporaryMap = new HashMap<Direction, List<Card>>();
        Map<Direction, Hand> hands = new HashMap<Direction, Hand>();
        Direction currentDirection;
        for (Direction direction : Direction.values()) {
            temporaryMap.put(direction, new ArrayList<Card>());
        }

        currentDirection = dealer;

        while (shuffledDeck.hasCard()) {
            temporaryMap.get(currentDirection).add(shuffledDeck.dealCard());
            currentDirection = currentDirection.next();
        }
        for (Direction direction : Direction.values()) {
            List<Card> listOfCardsForDirection = temporaryMap.get(direction);
            hands.put(direction, new Hand(listOfCardsForDirection));
        }
        return new Board(hands, dealer);
    }

}
