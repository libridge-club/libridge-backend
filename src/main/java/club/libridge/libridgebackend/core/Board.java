package club.libridge.libridgebackend.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import club.libridge.libridgebackend.core.comparators.CardInsideHandComparator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import scalabridge.Card;
import scalabridge.Direction;

@EqualsAndHashCode
public class Board {

    /**
     * @deprecated Kryo needs a no-arg constructor FIXME kryo is not used anymore. Does jackson or spring web needs this?
     */
    @Deprecated
    @SuppressWarnings("unused")
    private Board() {
    }

    private Map<Direction, Hand> hands = new EnumMap<Direction, Hand>(Direction.class);
    @Getter
    private Direction dealer;

    public Board(@NonNull Map<Direction, Hand> hands, @NonNull Direction dealer) {
        this.hands = hands;

        this.sortAllHands(new CardInsideHandComparator());
        this.dealer = dealer;
    }

    public Hand getHandOf(Direction direction) {
        return this.hands.get(direction);
    }

    public void sortAllHands(@NonNull Comparator<Card> comparator) {
        for (Direction direction : Direction.values()) {
            this.getHandOf(direction).sort(comparator);
        }
    }

    public List<Card> removeOneCardFromEachHand() {
        List<Card> removedCards = new ArrayList<Card>();
        for (Direction direction : Direction.values()) {
            Hand currentHand = this.getHandOf(direction);
            Card removedCard = currentHand.removeOneRandomCard();
            removedCards.add(removedCard);
        }
        return removedCards;
    }

    public void putCardInHand(Map<Card, Direction> cardDirectionMap) {
        for (Map.Entry<Card, Direction> cardDirection : cardDirectionMap.entrySet()) {
            this.hands.get(cardDirection.getValue()).addCard(cardDirection.getKey());
        }
    }

}
