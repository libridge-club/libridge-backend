package club.libridge.libridgebackend.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import club.libridge.libridgebackend.core.comparators.RankComparator;
import club.libridge.libridgebackend.core.exceptions.TrickAlreadyFullException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.GameConstants;
import scalabridge.Suit;

@EqualsAndHashCode
public class Trick { // FIXME to remove all of the king only features

    /**
     * @deprecated Kryo needs a no-arg constructor FIXME kryo is not used anymore. Does jackson or spring web needs this?
     */
    @Deprecated
    @SuppressWarnings("unused")
    private Trick() {
    }

    private List<Card> cards;
    @Getter
    private Direction leader;
    @Getter
    private boolean lastTwo;

    public Trick(@NonNull Direction leader) {
        this.leader = leader;
        this.cards = new ArrayList<Card>();
        this.lastTwo = false;
    }

    public void addCard(@NonNull Card card) {
        if (!this.isComplete()) {
            this.cards.add(card);
        } else {
            throw new TrickAlreadyFullException();
        }
    }

    public boolean isComplete() {
        return this.getNumberOfCards() == GameConstants.instance().COMPLETE_TRICK_NUMBER_OF_CARDS();
    }

    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(this.cards);
    }

    public Map<Card, Direction> getCardDirectionMap() {
        Map<Card, Direction> mapDirectionCard = new HashMap<Card, Direction>();
        for (Card card : this.cards) {
            mapDirectionCard.put(card, this.directionOfCard(card));
        }
        return mapDirectionCard;
    }

    public Suit getLeadSuit() {
        Card leadCard = this.getLeadCard();
        if (leadCard == null) {
            return null;
        }
        return leadCard.getSuit();
    }

    private Card getLeadCard() {
        if (this.cards.isEmpty()) {
            return null;
        }
        return this.cards.get(0);
    }

    public Direction getWinnerWithoutTrumpSuit() {
        Card card = this.highestCardOfSuit(this.getLeadSuit());
        return this.directionOfCard(card);
    }

    private Card highestCardOfSuit(@NonNull Suit suit) {
        SortedSet<Card> sortedCardsOfSuit = new TreeSet<Card>(new RankComparator());
        for (Card card : this.getCards()) {
            if (card.getSuit() == suit) {
                sortedCardsOfSuit.add(card);
            }
        }
        return sortedCardsOfSuit.last();
    }

    private Direction directionOfCard(@NonNull Card card) {
        Direction currentDirection = leader;
        Direction response = null;
        for (Card cardFromTrick : this.getCards()) {
            if (card.equals(cardFromTrick)) {
                response = currentDirection;
                break;
            } else {
                currentDirection = currentDirection.next();
            }
        }
        return response;
    }

    public Direction getWinnerWithTrumpSuit(@NonNull Suit trumpSuit) {
        Card card;
        if (this.hasSuit(trumpSuit)) {
            card = this.highestCardOfSuit(trumpSuit);
        } else {
            card = this.highestCardOfSuit(this.getLeadSuit());
        }
        return this.directionOfCard(card);
    }

    private boolean hasSuit(Suit suit) {
        for (Card card : this.getCards()) {
            if (card.getSuit() == suit) {
                return true;
            }
        }
        return false;
    }

    private int getNumberOfCards() {
        return this.getCards().size();
    }

    @Override
    public String toString() {
        return this.cards.toString();
    }

    public boolean hasCardOf(@NonNull Direction direction) {
        int stepsBetween = Direction.differenceBetween(this.getLeader(), direction);
        int minNumberOfCardsThatShouldBePlayedForDirectionToBeIncluded = stepsBetween + 1;
        int numberOfCardsInTable = this.getCards().size();
        return minNumberOfCardsThatShouldBePlayedForDirectionToBeIncluded <= numberOfCardsInTable;
    }

    public Map<Card, Direction> getCardsFromLastUpTo(@NonNull Direction direction) {
        Map<Card, Direction> cardsUpToDirection = new HashMap<Card, Direction>();
        int directionPosition = Direction.differenceBetween(this.leader, direction);
        for (int i = this.cards.size() - 1; i >= directionPosition; i--) {
            Direction currentDirection = this.directionOfCard(this.cards.get(i));
            cardsUpToDirection.put(this.cards.get(i), currentDirection);
        }
        return cardsUpToDirection;
    }

    public void removeCardsFromLastUpTo(@NonNull Direction direction) {
        int directionIndex = Direction.differenceBetween(this.getLeader(), direction);
        for (int i = this.cards.size() - 1; i >= directionIndex; i--) {
            this.cards.remove(i);
        }
    }

    public Direction getLastPlayer() {
        return this.leader.next(3);
    }

}
