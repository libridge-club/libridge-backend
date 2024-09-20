package club.libridge.libridgebackend.core;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.Hand;

@EqualsAndHashCode
public class Board {

    /**
     * @deprecated Kryo needs a no-arg constructor FIXME kryo is not used anymore. Does jackson or spring web needs this?
     */
    @Deprecated
    @SuppressWarnings("unused")
    private Board() {
    }

    private Map<Direction, Hand> hands = new HashMap<Direction, Hand>();
    @Getter
    private Direction dealer;

    public Board(@NonNull Map<Direction, Hand> hands, @NonNull Direction dealer) {
        this.hands = hands;
        this.sortAllHands();
        this.dealer = dealer;
    }

    public Hand getHandOf(Direction direction) {
        return this.hands.get(direction);
    }

    protected void setHandOf(Direction direction, Hand hand) { // FIXME this is temporary for translating Hand into immutable class
        this.hands.put(direction, hand);
    }

    public void sortAllHands() {
        for (Entry<Direction, Hand> entry : this.hands.entrySet()) {
            hands.put(entry.getKey(), entry.getValue().withDefaultOrdering());
        }
    }

    public void sortAllHands(@NonNull Comparator<Card> comparator) {
        for (Entry<Direction, Hand> entry : this.hands.entrySet()) {
            hands.put(entry.getKey(), entry.getValue().withDefaultOrdering()); //FIXME use comparator
        }
    }

    public void removeOneCardFromEachHand() {
        for (Direction direction : Direction.values()) {
            Hand newHand = this.getHandOf(direction).removeOneRandomCard();
            this.setHandOf(direction, newHand);
        }
    }

    public void unplayCardsInHands(Map<Card, Direction> cardDirectionMap) {
        for (Map.Entry<Card, Direction> cardDirection : cardDirectionMap.entrySet()) {
            Hand newHand = this.unplayCardInHand(cardDirection.getKey(), cardDirection.getValue());
            this.setHandOf(cardDirection.getValue(), newHand);
        }
    }

    private Hand unplayCardInHand(Card card, Direction direction) {
        return this.hands.get(direction).unplayCard(card);
    }

}
