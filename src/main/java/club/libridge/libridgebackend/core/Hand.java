package club.libridge.libridgebackend.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import club.libridge.libridgebackend.core.comparators.CardInsideHandComparator;
import scalabridge.Card;
import scalabridge.Suit;

public class Hand {

    private final List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<Card>();
    }

    public Collection<Card> getCards() {
        return Collections.unmodifiableCollection(this.cards);
    }

    public HandEvaluations getHandEvaluations() {
        return new HandEvaluations(this);
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public void removeCard(Card card) {
        this.cards.remove(card);
    }

    public Card removeOneRandomCard() {
        Card randomCardFromHand = new RandomUtils().pickOneRandomCard(this);
        this.removeCard(randomCardFromHand);
        return randomCardFromHand;
    }

    public Card get(int position) {
        return this.getCardsAsList().get(position);
    }

    public int size() {
        return this.getCardsAsList().size();
    }

    public void sort(Comparator<Card> comparator) {
        Collections.sort(this.cards, comparator);
    }

    public boolean containsCard(Card card) {
        return this.getCardsAsList().contains(card);
    }

    public boolean hasSuit(Suit suit) {
        for (Card card : this.getCardsAsList()) {
            if (card.getSuit() == suit) {
                return true;
            }
        }
        return false;
    }

    private List<Card> getCardsAsList() {
        return Collections.unmodifiableList(this.cards);
    }

    /**
     * Implemented from PBN Standard 2.1 - Defined at section 3.4.11 The Deal tag
     */
    @Override
    public String toString() {
        StringBuilder returnValue = new StringBuilder(20);
        this.sort(new CardInsideHandComparator());
        List<Suit> suitsInDescendingOrder = List.of(Suit.getSPADES(), Suit.getHEARTS(), Suit.getDIAMONDS(), Suit.getCLUBS());
        for (Suit currentSuit : suitsInDescendingOrder) {
            if (Suit.getSPADES() != currentSuit) {
                returnValue.append(".");
            }
            this.getCards().stream().filter(card -> card.getSuit() == currentSuit).map(card -> card.getRank().getSymbol())
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
