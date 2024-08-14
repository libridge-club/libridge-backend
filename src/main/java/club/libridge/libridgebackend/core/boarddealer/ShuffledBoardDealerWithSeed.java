package club.libridge.libridgebackend.core.boarddealer;

import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;

import club.libridge.libridgebackend.core.Board;
import scalabridge.Card;
import scalabridge.Direction;
import club.libridge.libridgebackend.core.Hand;
import club.libridge.libridgebackend.core.ShuffledDeck;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShuffledBoardDealerWithSeed implements BoardDealer {

    private final long seed;

    @Override
    public Board dealBoard(Direction dealer, Deque<Card> deck) {
        Map<Direction, Hand> hands;
        Direction currentDirection;
        Hand currentHand;
        ShuffledDeck currentDeck = new ShuffledDeck(deck, seed);
        hands = new EnumMap<Direction, Hand>(Direction.class);
        for (Direction direction : Direction.values()) {
            hands.put(direction, new Hand());
        }
        for (currentDirection = dealer; currentDeck.hasCard(); currentDirection = currentDirection.next()) {
            currentHand = hands.get(currentDirection);
            currentHand.addCard(currentDeck.dealCard());
        }
        return new Board(hands, dealer);
    }

}
