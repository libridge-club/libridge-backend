package club.libridge.libridgebackend.app.persistence;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.core.PavlicekNumber;
import club.libridge.libridgebackend.core.boarddealer.BoardDealer;
import club.libridge.libridgebackend.core.boarddealer.CardDeck;
import club.libridge.libridgebackend.core.boarddealer.Complete52CardDeck;
import club.libridge.libridgebackend.core.boarddealer.ShuffledBoardDealer;
import club.libridge.libridgebackend.core.exceptions.ImpossibleBoardException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.Hand;

@Validated
@Component
@AllArgsConstructor
public class BoardFactory {

    private static final int CARDS_IN_A_FULL_HAND = 13;
    private static final int CARDS_IN_A_FULL_DECK = 52;

    @NotNull
    @NonNull
    private final PavlicekNumber pavlicekNumberGenerator;

    public Board fromEntity(BoardEntity boardEntity) {
        return this.fromPavlicekNumber(boardEntity.getPavlicekNumber());
    }

    public Board getRandom() {
        BoardDealer boardDealer = new ShuffledBoardDealer();
        CardDeck anyCardDeck = new Complete52CardDeck();
        return boardDealer.dealBoard(Direction.getNorth(), anyCardDeck.getDeck());
    }

    public Board fromPavlicekNumber(String pavlicekNumber) {
        return pavlicekNumberGenerator.getBoardFromNumber(new BigInteger(pavlicekNumber));
    }

    /**
     * @param hand Provided hand. Must be a complete hand with 13 cards.
     * @param direction Direction of the provided hand. It will also be made dealer.
     * @return A board with the provided hand as dealer, in the direction provided, with no restriction to the position of the other cards.
     */
    public Board fromHandAndDirection(Hand hand, Direction direction) {
        if (hand.getCards().size() != CARDS_IN_A_FULL_HAND) {
            throw new ImpossibleBoardException();
        }
        Deque<Card> deque = new Complete52CardDeck().getDeck();
        deque.removeAll(hand.getCards());
        assert (deque.size() == (CARDS_IN_A_FULL_DECK - CARDS_IN_A_FULL_HAND));

        Map<Direction, Hand> hands = new HashMap<Direction, Hand>();
        hands.put(direction, hand);
        Direction currentDirection = direction.next();
        while (!deque.isEmpty()) {
            List<Card> cardsToAdd = new ArrayList<Card>();
            for (int i = 0; i < CARDS_IN_A_FULL_HAND; i++) {
                cardsToAdd.add(deque.removeFirst());
            }
            Hand currentHand = new Hand(cardsToAdd);
            hands.put(currentDirection, currentHand);
            currentDirection = currentDirection.next();
        }

        return new Board(hands, direction);
    }

}
