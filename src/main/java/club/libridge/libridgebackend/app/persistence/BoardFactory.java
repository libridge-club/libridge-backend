package club.libridge.libridgebackend.app.persistence;

import java.math.BigInteger;
import java.util.Deque;
import java.util.EnumMap;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import club.libridge.libridgebackend.core.Board;
import scalabridge.Card;
import scalabridge.Direction;
import club.libridge.libridgebackend.core.Hand;
import club.libridge.libridgebackend.core.PavlicekNumber;
import club.libridge.libridgebackend.core.boarddealer.BoardDealer;
import club.libridge.libridgebackend.core.boarddealer.CardDeck;
import club.libridge.libridgebackend.core.boarddealer.Complete52CardDeck;
import club.libridge.libridgebackend.core.boarddealer.ShuffledBoardDealer;
import club.libridge.libridgebackend.core.exceptions.ImpossibleBoardException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NonNull;

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
        return boardDealer.dealBoard(Direction.NORTH, anyCardDeck.getDeck());
    }

    public Board fromPavlicekNumber(String pavlicekNumber) {
        return pavlicekNumberGenerator.getBoardFromNumber(new BigInteger(pavlicekNumber));
    }

    /**
     * @param hand Provided hand. Must be a complete hand with 13 cards.
     * @param direction Direction of the provided hand. It will also be made dealer.
     * @return A board with the provided hand as dealer, in the direction provided,
     * with no restriction to the position of the other cards.
     */
    public Board fromHandAndDirection(Hand hand, Direction direction) {
        if (hand.getCards().size() != CARDS_IN_A_FULL_HAND) {
            throw new ImpossibleBoardException();
        }
        Deque<Card> deque = new Complete52CardDeck().getDeck();
        deque.removeAll(hand.getCards());
        assert (deque.size() == (CARDS_IN_A_FULL_DECK - CARDS_IN_A_FULL_HAND));

        EnumMap<Direction, Hand> hands = new EnumMap<Direction, Hand>(Direction.class);
        hands.put(direction, hand);
        Direction currentDirection = direction.next();
        while (!deque.isEmpty()) {
            Hand currentHand = new Hand();
            for (int i = 0; i < CARDS_IN_A_FULL_HAND; i++) {
                currentHand.addCard(deque.removeFirst());
            }
            hands.put(currentDirection, currentHand);
            currentDirection = currentDirection.next();
        }

        return new Board(hands, direction);
    }

}
