package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import club.libridge.libridgebackend.core.exceptions.TrickAlreadyFullException;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.Rank;
import scalabridge.Suit;

public class TrickTest {

    private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;

    @Test
    public void shouldBePossibleToAddCardsUpToAMaximum() {
        Trick trick = new Trick(Direction.getNorth());
        Card card = mock(Card.class);
        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            trick.addCard(card);
        }

        verifyNoInteractions(card);
    }

    @Test
    public void shouldThrowExceptionWhenAddingMoreCardsThanTheMaximum() {
        Trick trick = new Trick(Direction.getNorth());
        Card card = mock(Card.class);
        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            trick.addCard(card);
        }
        Assertions.assertThrows(TrickAlreadyFullException.class, () -> {
            trick.addCard(card);
        });
        verifyNoInteractions(card);
    }

    @Test
    public void shouldReturnIfItIsEmpty() {
        Trick trick = new Trick(Direction.getNorth());
        assertTrue(trick.isEmpty());
    }

    @Test
    public void shouldReturnIfItIsComplete() {
        Trick trick = new Trick(Direction.getNorth());
        Card card = mock(Card.class);
        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            trick.addCard(card);
        }
        assertTrue(trick.isComplete());
        verifyNoInteractions(card);
    }

    @Test
    public void shouldGetLeaderFromConstruction() {
        Direction direction = Direction.getSouth();
        Trick newTrick = new Trick(direction);
        assertEquals(direction, newTrick.getLeader());
    }

    @Test
    public void shouldGetLeadSuitFromFirstCardAdded() {
        Trick trick = new Trick(Direction.getNorth());
        Suit clubs = Suit.getCLUBS();
        Card cardOfClubs = mock(Card.class);
        when(cardOfClubs.getSuit()).thenReturn(clubs);
        Card anyOtherCard = mock(Card.class);
        trick.addCard(cardOfClubs);
        trick.addCard(anyOtherCard);
        assertEquals(clubs, trick.getLeadSuit());
        verify(cardOfClubs, only()).getSuit();
    }

    @Test
    public void shouldGetWinnerWithoutTrumpSuit() {
        Direction leader = Direction.getSouth();
        Trick trick = new Trick(leader);

        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.getRank()).thenReturn(Rank.getJACK());
        when(jackOfClubs.getSuit()).thenReturn(Suit.getCLUBS());

        Card queenOfHearts = mock(Card.class);
        when(queenOfHearts.getRank()).thenReturn(Rank.getQUEEN());
        when(queenOfHearts.getSuit()).thenReturn(Suit.getHEARTS());

        Card aceOfSpades = mock(Card.class);
        when(aceOfSpades.getRank()).thenReturn(Rank.getACE());
        when(aceOfSpades.getSuit()).thenReturn(Suit.getSPADES());

        Card kingOfClubs = mock(Card.class);
        when(kingOfClubs.getRank()).thenReturn(Rank.getKING());
        when(kingOfClubs.getSuit()).thenReturn(Suit.getCLUBS());

        when(jackOfClubs.compareRank(kingOfClubs)).thenReturn(-1);
        when(kingOfClubs.compareRank(jackOfClubs)).thenReturn(1);

        trick.addCard(jackOfClubs);
        trick.addCard(queenOfHearts);
        trick.addCard(aceOfSpades);
        trick.addCard(kingOfClubs);

        Direction winner = Direction.getEast();
        assertEquals(winner, trick.getWinnerWithoutTrumpSuit());

        verify(jackOfClubs, atLeastOnce()).getSuit();
        verify(queenOfHearts, atLeastOnce()).getSuit();
        verify(aceOfSpades, atLeastOnce()).getSuit();
        verify(kingOfClubs, atLeastOnce()).getSuit();

        // Java's Treeset compares the Card with itself!
        // That is why if we do verifications like
        // verify(jackOfClubs, atLeastOnce()).compareRank(kingOfClubs);
        // Then the verifyNoMoreInteractions(jackOfClubs) would break.
        // We probably should not care about implementation, but...

        verifyNoMoreInteractions(queenOfHearts);
        verifyNoMoreInteractions(aceOfSpades);

        ArgumentCaptor<Card> cardsJackOfClubsComparedTo = ArgumentCaptor.forClass(Card.class);
        ArgumentCaptor<Card> cardsKingOfClubsComparedTo = ArgumentCaptor.forClass(Card.class);
        Mockito.verify(jackOfClubs).compareRank(cardsJackOfClubsComparedTo.capture());
        Mockito.verify(kingOfClubs).compareRank(cardsKingOfClubsComparedTo.capture());
        // There must be a comparison between Jack of Clubs and King of Clubs at some
        // point
        assertTrue(
                cardsJackOfClubsComparedTo.getAllValues().contains(kingOfClubs) || cardsKingOfClubsComparedTo.getAllValues().contains(jackOfClubs));
    }

    @Test
    public void shouldGetWinnerWithTrumpSuit() {
        Direction leader = Direction.getSouth();
        Trick trick = new Trick(leader);

        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.getRank()).thenReturn(Rank.getJACK());
        when(jackOfClubs.getSuit()).thenReturn(Suit.getCLUBS());

        Card queenOfHearts = mock(Card.class);
        when(queenOfHearts.getRank()).thenReturn(Rank.getQUEEN());
        when(queenOfHearts.getSuit()).thenReturn(Suit.getHEARTS());

        Card aceOfSpades = mock(Card.class);
        when(aceOfSpades.getRank()).thenReturn(Rank.getACE());
        when(aceOfSpades.getSuit()).thenReturn(Suit.getSPADES());

        Card kingOfClubs = mock(Card.class);
        when(kingOfClubs.getRank()).thenReturn(Rank.getKING());
        when(kingOfClubs.getSuit()).thenReturn(Suit.getCLUBS());

        when(jackOfClubs.compareRank(kingOfClubs)).thenReturn(-1);
        when(kingOfClubs.compareRank(jackOfClubs)).thenReturn(1);

        trick.addCard(jackOfClubs);
        trick.addCard(queenOfHearts);
        trick.addCard(aceOfSpades);
        trick.addCard(kingOfClubs);

        Direction winnerWithDiamondsAsTrump = Direction.getEast();
        Direction winnerWithClubsAsTrump = Direction.getEast();
        Direction winnerWithHeartsAsTrump = Direction.getWest();
        Direction winnerWithSpadesAsTrump = Direction.getNorth();
        assertEquals(winnerWithDiamondsAsTrump, trick.getWinnerWithTrumpSuit(Suit.getDIAMONDS()));
        assertEquals(winnerWithClubsAsTrump, trick.getWinnerWithTrumpSuit(Suit.getCLUBS()));
        assertEquals(winnerWithHeartsAsTrump, trick.getWinnerWithTrumpSuit(Suit.getHEARTS()));
        assertEquals(winnerWithSpadesAsTrump, trick.getWinnerWithTrumpSuit(Suit.getSPADES()));

        verify(jackOfClubs, atLeastOnce()).getSuit();
        verify(queenOfHearts, atLeastOnce()).getSuit();
        verify(aceOfSpades, atLeastOnce()).getSuit();
        verify(kingOfClubs, atLeastOnce()).getSuit();

        verify(jackOfClubs, never()).compareRank(queenOfHearts);
        verify(jackOfClubs, never()).compareRank(aceOfSpades);

        verify(queenOfHearts, never()).compareRank(jackOfClubs);
        verify(queenOfHearts, never()).compareRank(aceOfSpades);
        verify(queenOfHearts, never()).compareRank(kingOfClubs);

        verify(aceOfSpades, never()).compareRank(jackOfClubs);
        verify(aceOfSpades, never()).compareRank(queenOfHearts);
        verify(aceOfSpades, never()).compareRank(kingOfClubs);

        verify(kingOfClubs, never()).compareRank(queenOfHearts);
        verify(kingOfClubs, never()).compareRank(aceOfSpades);

        ArgumentCaptor<Card> cardsJackOfClubsComparedTo = ArgumentCaptor.forClass(Card.class);
        ArgumentCaptor<Card> cardsKingOfClubsComparedTo = ArgumentCaptor.forClass(Card.class);
        Mockito.verify(jackOfClubs, atLeast(0)).compareRank(cardsJackOfClubsComparedTo.capture());
        Mockito.verify(kingOfClubs, atLeast(0)).compareRank(cardsKingOfClubsComparedTo.capture());
        // There must be a comparison between Jack of Clubs and King of Clubs at some
        // point
        assertTrue(
                cardsJackOfClubsComparedTo.getAllValues().contains(kingOfClubs) || cardsKingOfClubsComparedTo.getAllValues().contains(jackOfClubs));

    }

    @Test
    public void getListOfCardsShouldReturnTheCorrectList() {
        Trick trick = new Trick(Direction.getNorth());
        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.getRank()).thenReturn(Rank.getJACK());
        when(jackOfClubs.getSuit()).thenReturn(Suit.getCLUBS());

        Card queenOfHearts = mock(Card.class);
        when(queenOfHearts.getRank()).thenReturn(Rank.getQUEEN());
        when(queenOfHearts.getSuit()).thenReturn(Suit.getHEARTS());

        Card aceOfSpades = mock(Card.class);
        when(aceOfSpades.getRank()).thenReturn(Rank.getACE());
        when(aceOfSpades.getSuit()).thenReturn(Suit.getSPADES());

        Card kingOfClubs = mock(Card.class);
        when(kingOfClubs.getRank()).thenReturn(Rank.getKING());
        when(kingOfClubs.getSuit()).thenReturn(Suit.getCLUBS());

        trick.addCard(jackOfClubs);
        trick.addCard(queenOfHearts);
        trick.addCard(aceOfSpades);
        trick.addCard(kingOfClubs);

        List<Card> receivedList = trick.getCards();

        verifyNoInteractions(jackOfClubs);
        verifyNoInteractions(queenOfHearts);
        verifyNoInteractions(aceOfSpades);
        verifyNoInteractions(kingOfClubs);

        assertEquals(jackOfClubs.getRank(), receivedList.get(0).getRank());
        assertEquals(jackOfClubs.getSuit(), receivedList.get(0).getSuit());
        assertEquals(queenOfHearts.getRank(), receivedList.get(1).getRank());
        assertEquals(queenOfHearts.getSuit(), receivedList.get(1).getSuit());
        assertEquals(aceOfSpades.getRank(), receivedList.get(2).getRank());
        assertEquals(aceOfSpades.getSuit(), receivedList.get(2).getSuit());
        assertEquals(kingOfClubs.getRank(), receivedList.get(3).getRank());
        assertEquals(kingOfClubs.getSuit(), receivedList.get(3).getSuit());

    }

    @Test
    public void getListOfCardsShouldReturnAnUnmodifiableList() {
        Trick trick = new Trick(Direction.getNorth());
        Card jackOfClubs = mock(Card.class);
        when(jackOfClubs.getRank()).thenReturn(Rank.getJACK());
        when(jackOfClubs.getSuit()).thenReturn(Suit.getCLUBS());
        for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
            trick.addCard(jackOfClubs);
        }
        List<Card> receivedList = trick.getCards();
        verifyNoInteractions(jackOfClubs);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            receivedList.add(0, jackOfClubs);
        });

    }

    @Test
    public void getCardDirectionMapShouldReturnAMapContainingTheDirectionOfEachCard() {
        Direction leader = Direction.getSouth();
        Trick trick = new Trick(leader);
        int numberOfCards = 4;

        List<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < numberOfCards; i++) {
            Card card = mock(Card.class);
            cards.add(card);
            trick.addCard(card);
        }

        Map<Card, Direction> cardDirectionMap = trick.getCardDirectionMap();

        for (int i = 0; i < numberOfCards; i++) {
            assertEquals(leader.next(i), cardDirectionMap.get(cards.get(i)));
        }
    }

    @Test
    public void hasCardOfShouldReturnTrueWhenDirectionHasAlreadyPlayed() {
        Trick trick = this.trickWithTwoCardsAndNorthAsLeader();
        assertTrue(trick.hasCardOf(Direction.getEast()));
    }

    @Test
    public void hasCardOfShouldReturnFalseWhenDirectionHasNotPlayedYet() {
        Trick trick = this.trickWithTwoCardsAndNorthAsLeader();
        assertFalse(trick.hasCardOf(Direction.getSouth()));
    }

    @Test
    public void hasCardOfShouldReturnFalseWhenTheLeaderHasNotPlayedYet() {
        Direction leader = Direction.getNorth();
        Trick trickWithNoCards = new Trick(leader);
        assertFalse(trickWithNoCards.hasCardOf(leader));
    }

    private Trick trickWithTwoCardsAndNorthAsLeader() {
        Direction leader = Direction.getNorth();
        Trick trick = new Trick(leader);

        Card firstCard = mock(Card.class);
        Card secondCard = mock(Card.class);

        trick.addCard(firstCard);
        trick.addCard(secondCard);

        return trick;
    }

    @Test
    public void removeCardsUpToDirectionShouldEliminateAllAndOnlyTheCardsUpToThatDirection() {
        Trick trick = this.trickWithTwoCardsAndNorthAsLeader();
        trick.addCard(mock(Card.class));
        trick.removeCardsFromLastUpTo(Direction.getEast());
        assertFalse(trick.hasCardOf(Direction.getEast()));
        assertFalse(trick.hasCardOf(Direction.getSouth()));
        assertTrue(trick.hasCardOf(Direction.getNorth()));
        assertEquals(1, trick.getCards().size());
    }

    @Test
    public void removeCardsUpToDirectionShouldNotFailWhenTrickHasNoCards() {
        Trick trick = new Trick(Direction.getNorth());
        trick.removeCardsFromLastUpTo(Direction.getEast());
    }

    @Test
    public void removeCardsUpToLeaderShouldEliminateAllCardsFromTrick() {
        Trick trick = this.trickWithTwoCardsAndNorthAsLeader();
        trick.removeCardsFromLastUpTo(Direction.getNorth());
        assertTrue(trick.isEmpty());
    }

    @Test
    public void getCardsFromLastUpToLast() {
        Direction leader = Direction.getNorth();
        Trick trick = new Trick(leader);
        Direction next = leader.next();
        Card firstCard = mock(Card.class);
        Card secondCard = mock(Card.class);
        trick.addCard(firstCard);
        trick.addCard(secondCard);

        Map<Card, Direction> cardsUpToEast = trick.getCardsFromLastUpTo(next);

        Direction directionFromFirstCard = cardsUpToEast.get(firstCard);
        Direction directionFromSecondCard = cardsUpToEast.get(secondCard);
        assertNull(directionFromFirstCard);
        assertEquals(next, directionFromSecondCard);
    }

    @Test
    public void getCardsFromLastUpToDirectionThatDidNotPlayed() {
        Direction leader = Direction.getNorth();
        Trick trick = new Trick(leader);
        Card firstCard = mock(Card.class);
        trick.addCard(firstCard);

        assertTrue(trick.getCardsFromLastUpTo(leader.next()).isEmpty());
    }

    @Test
    public void getCardsFromLastUpToLeader() {
        Direction leader = Direction.getNorth();
        Trick trick = new Trick(leader);
        Card firstCard = mock(Card.class);
        Card secondCard = mock(Card.class);
        trick.addCard(firstCard);
        trick.addCard(secondCard);

        Map<Card, Direction> cardsUpToLeader = trick.getCardsFromLastUpTo(leader);

        assertEquals(leader, cardsUpToLeader.get(firstCard));
        assertEquals(leader.next(), cardsUpToLeader.get(secondCard));
    }

    @Test
    public void getLastPlayer() {
        Direction leader = Direction.getWest();
        Direction expectedLastPlayer = Direction.getSouth();
        Trick trick = new Trick(leader);
        Direction lastPlayer = trick.getLastPlayer();

        assertEquals(expectedLastPlayer, lastPlayer);
    }

}
