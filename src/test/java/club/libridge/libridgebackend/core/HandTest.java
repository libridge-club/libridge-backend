package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import scalabridge.Card;
import scalabridge.Hand;
import scalabridge.Rank;
import scalabridge.Suit;

public class HandTest {

    @Test
    public void shouldAddCard() {
        Card card = Mockito.mock(Card.class);
        Hand hand = new Hand(List.of(card));

        assertEquals(1, hand.size());
    }

    @Test
    public void shouldRemoveOnlyTheCorrectCard() {
        Card firstCard = Mockito.mock(Card.class);
        Card secondCard = Mockito.mock(Card.class);

        Hand hand = new Hand(List.of(firstCard, secondCard));
        Hand newHand = hand.playCard(firstCard);

        assertEquals(1, newHand.size());
        assertTrue(newHand.getCards().contains(secondCard));
        assertFalse(newHand.getCards().contains(firstCard));
    }

    @Test
    public void shouldReturnIfItContainsACard() {
        Card firstCard = Mockito.mock(Card.class);
        Card secondCard = Mockito.mock(Card.class);
        Hand hand = new Hand(List.of(firstCard));

        assertTrue(hand.containsCard(firstCard));
        assertFalse(hand.containsCard(secondCard));
    }

    @Test
    public void shouldReturnIfItHasASuit() {
        Suit spades = Suit.getSPADES();
        Suit hearts = Suit.getHEARTS();
        Suit clubs = Suit.getCLUBS();
        Suit diamonds = Suit.getDIAMONDS();

        Card aceOfSpades = new Card(spades, Rank.getACE());
        Card kingOfHearts = new Card(hearts, Rank.getKING());

        Hand hand = new Hand(List.of(aceOfSpades, kingOfHearts));

        assertTrue(hand.hasSuit(spades));
        assertTrue(hand.hasSuit(hearts));
        assertFalse(hand.hasSuit(clubs));
        assertFalse(hand.hasSuit(diamonds));
    }

    @Test
    public void shouldTransformToStringUsingPBNImplementation() {
        Card aceOfSpades = new Card(Suit.getSPADES(), Rank.getACE());
        Card kingOfHearts = new Card(Suit.getHEARTS(), Rank.getKING());
        Hand hand = new Hand(List.of(aceOfSpades, kingOfHearts));
        String expectedString = "A.K..";

        assertEquals(expectedString, hand.toString());
    }

    @Test
    public void shouldBeEqualToAnotherHandWithSameCards() {
        Card aceOfSpades = new Card(Suit.getSPADES(), Rank.getACE());
        Hand hand1 = new Hand(List.of(aceOfSpades));
        Hand hand2 = new Hand(List.of(aceOfSpades));

        assertEquals(hand1, hand2);
    }

}
