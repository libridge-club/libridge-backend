package club.libridge.libridgebackend.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import club.libridge.libridgebackend.core.comparators.CardInsideHandComparator;
import scala.collection.Iterator;
import scalabridge.Card;
import scalabridge.CompleteHand;
import scalabridge.Suit;

public final class Hand {

    private final List<Card> cards;
    private final List<Card> playedCards;

    public Hand(CompleteHand completeHand) {
        List<Card> cards = new ArrayList<Card>();
        Iterator<Card> iterator = completeHand.cards().iterator();
        while (iterator.hasNext()) {
            Card current = iterator.next();
            cards.add(current);
        }
        this.cards = Collections.unmodifiableList(new ArrayList<Card>(cards));
        this.playedCards = Collections.unmodifiableList(new ArrayList<Card>());
    }

    public Hand(List<Card> cards) {
        this(new ArrayList<Card>(cards), new ArrayList<Card>());
    }

    public Hand(List<Card> cards, List<Card> playedCards) {
        this.cards = Collections.unmodifiableList(new ArrayList<Card>(cards));
        this.playedCards = Collections.unmodifiableList(new ArrayList<Card>(playedCards));
    }

    public Collection<Card> getCards() {
        return Collections.unmodifiableCollection(this.getUnplayedCardsAsList());
    }

    public HandEvaluations getHandEvaluations() {
        return new HandEvaluations(this);
    }

    public Hand addCard(Card card) {
        List<Card> newCardsList = new ArrayList<Card>(this.cards);
        newCardsList.add(card);
        return new Hand(newCardsList, this.playedCards);
    }

    public Hand playCard(Card card) {
        List<Card> newListOfPlayedCards = new ArrayList<Card>();
        newListOfPlayedCards.add(card);
        newListOfPlayedCards.addAll(this.playedCards);
        return new Hand(this.cards, newListOfPlayedCards);
    }

    public Hand unplayCard(Card card) {
        List<Card> newListOfPlayedCards = new ArrayList<Card>();
        newListOfPlayedCards.addAll(this.playedCards);
        newListOfPlayedCards.remove(card);
        return new Hand(this.cards, newListOfPlayedCards);
    }

    public Hand removeOneRandomCard() {
        List<Card> cardsAsList = this.getUnplayedCardsAsList();
        int size = cardsAsList.size();
        int randomIndex = new RandomUtils().nextInt(size);
        Card cardToRemove = cardsAsList.get(randomIndex);
        List<Card> newCards = this.cards.stream().filter(card -> !card.equals(cardToRemove)).toList();
        return new Hand(newCards, this.playedCards);
    }

    public int size() {
        return this.getUnplayedCardsAsList().size();
    }

    public Hand sort(Comparator<Card> comparator) {
        ArrayList<Card> sortedCards = new ArrayList<Card>(this.cards);
        Collections.sort(sortedCards, comparator);
        return new Hand(sortedCards, this.playedCards);
    }

    public boolean containsCard(Card card) {
        return this.getUnplayedCardsAsList().contains(card);
    }

    public boolean hasSuit(Suit suit) {
        return this.getUnplayedCardsAsList().stream().anyMatch(card -> card.getSuit() == suit);
    }

    private List<Card> getUnplayedCardsAsList() {
        HashSet<Card> playedCardsSet = new HashSet<Card>(this.playedCards);
        List<Card> nonPlayedCards = this.cards.stream().filter(card -> !playedCardsSet.contains(card)).toList();
        return Collections.unmodifiableList(nonPlayedCards);
    }

    /**
     * Implemented from PBN Standard 2.1 - Defined at section 3.4.11 The Deal tag
     */
    @Override
    public String toString() {
        StringBuilder returnValue = new StringBuilder(20);
        Hand sortedHand = this.sort(new CardInsideHandComparator());
        List<Suit> suitsInDescendingOrder = List.of(Suit.getSPADES(), Suit.getHEARTS(), Suit.getDIAMONDS(), Suit.getCLUBS());
        for (Suit currentSuit : suitsInDescendingOrder) {
            if (Suit.getSPADES() != currentSuit) {
                returnValue.append(".");
            }
            sortedHand.getCards().stream().filter(card -> card.getSuit() == currentSuit).map(card -> card.getRank().getSymbol())
                    .forEach(returnValue::append);
        }
        return returnValue.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Hand)) {
            return false;
        }
        Hand other = (Hand) o;
        HashSet<Card> myCards = new HashSet<Card>(this.getCards());
        HashSet<Card> otherCards = new HashSet<Card>(other.getCards());
        return myCards.equals(otherCards);
    }

    @Override
    public int hashCode() {
        HashSet<Card> myCards = new HashSet<Card>(this.getCards());
        return myCards.hashCode();
    }

}
