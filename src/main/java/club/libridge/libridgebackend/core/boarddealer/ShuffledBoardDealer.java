package club.libridge.libridgebackend.core.boarddealer;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import club.libridge.libridgebackend.core.ShuffledDeck;
import club.libridge.libridgebackend.pbn.PBNUtils;
import scala.Tuple2;
import scala.collection.mutable.Buffer;
import scala.jdk.javaapi.CollectionConverters;
import scalabridge.Card;
import scalabridge.CompleteDeckInFourHands;
import scalabridge.CompleteHand;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;
import scalabridge.Hand;
import scalabridge.nonpure.DuplicateBoardBuilder;

public class ShuffledBoardDealer implements BoardDealer {

    @Override
    public DuplicateBoard dealBoard(Direction dealer, Deque<Card> deck) {
        return this.dealBoard(dealer, new ShuffledDeck(deck));
    }

    public DuplicateBoard dealBoard(Direction dealer, Deque<Card> deck, long seed) {
        return this.dealBoard(dealer, new ShuffledDeck(deck, seed));
    }

    private DuplicateBoard dealBoard(Direction dealer, ShuffledDeck shuffledDeck) {
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

        int positiveInteger = 0;
        if (Direction.NORTH == dealer) {
            positiveInteger = 1;
        } else if (Direction.EAST == dealer) {
            positiveInteger = 2;
        } else if (Direction.SOUTH == dealer) {
            positiveInteger = 3;
        } else if (Direction.WEST == dealer) {
            positiveInteger = 4;
        }

        List<Tuple2<Direction, CompleteHand>> list = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            CompleteHand completeHand = new CompleteHand(hands.get(direction));
            Tuple2<Direction, CompleteHand> current = new Tuple2<Direction, CompleteHand>(direction, completeHand);
            list.add(current);
        }
        Buffer<Tuple2<Direction, CompleteHand>> scalaBuffer = CollectionConverters.asScala(list);
        scala.collection.immutable.Map<Direction, CompleteHand> scalaImmutableMap = scala.collection.immutable.Map.from(scalaBuffer);
        CompleteDeckInFourHands completeDeckInFourHands = new CompleteDeckInFourHands(scalaImmutableMap);

        String dealTagStringFromBoard = PBNUtils.dealTagStringFromBoard(completeDeckInFourHands);

        return DuplicateBoardBuilder.build(positiveInteger, dealTagStringFromBoard);
    }

}
