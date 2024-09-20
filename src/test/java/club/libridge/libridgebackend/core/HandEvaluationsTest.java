package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scalabridge.Card;
import scalabridge.Hand;
import scalabridge.HandEvaluations;
import scalabridge.Rank;
import scalabridge.Suit;

public class HandEvaluationsTest {

    private Card aceOfSpades;
    private Card kingOfSpades;
    private Card queenOfSpades;
    private Card jackOfSpades;
    private Card tenOfSpades;
    private Card nineOfSpades;
    private Card kingOfHearts;
    private Card threeOfClubs;
    private Card sevenOfDiamonds;
    private Hand emptyHand;
    private HandEvaluations emptyHandEvaluations;

    @BeforeEach
    public void setup() {
        this.aceOfSpades = new Card(Suit.getSPADES(), Rank.getACE());
        this.kingOfSpades = new Card(Suit.getSPADES(), Rank.getKING());
        this.queenOfSpades = new Card(Suit.getSPADES(), Rank.getQUEEN());
        this.jackOfSpades = new Card(Suit.getSPADES(), Rank.getJACK());
        this.tenOfSpades = new Card(Suit.getSPADES(), Rank.getTEN());
        this.nineOfSpades = new Card(Suit.getSPADES(), Rank.getNINE());
        this.kingOfHearts = new Card(Suit.getHEARTS(), Rank.getKING());
        this.threeOfClubs = new Card(Suit.getCLUBS(), Rank.getTHREE());
        this.sevenOfDiamonds = new Card(Suit.getDIAMONDS(), Rank.getSEVEN());
        this.emptyHand = new Hand(new ArrayList<Card>());
        this.emptyHandEvaluations = new HandEvaluations(emptyHand);
    }

    @Test
    public void shouldReturnTheCorrectHCP() {
        int acePoints = 4;
        int kingPoints = 3;
        int totalPoints = acePoints + kingPoints;

        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 1, 0, 0));
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(1, 1, 0, 0));

        HandEvaluations onlyKingHandEvaluations = new HandEvaluations(firstHand);
        HandEvaluations aceAndKingHandEvaluations = new HandEvaluations(secondHand);

        assertEquals(kingPoints, onlyKingHandEvaluations.getHCP());
        assertEquals(totalPoints, aceAndKingHandEvaluations.getHCP());
        assertEquals(0, emptyHandEvaluations.getHCP());
    }

    @Test
    public void shouldReturnTheCorrectShortestSuitLength() {

        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(1, 1, 0, 1));
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(1, 1, 1, 1));

        HandEvaluations onlyThreeSuitsHandEvaluations = new HandEvaluations(firstHand);
        HandEvaluations allSuitsHandEvaluations = new HandEvaluations(secondHand);

        assertEquals(0, emptyHandEvaluations.getShortestSuitLength());
        assertEquals(0, onlyThreeSuitsHandEvaluations.getShortestSuitLength());
        assertEquals(1, allSuitsHandEvaluations.getShortestSuitLength());
    }

    @Test
    public void shouldReturnTheCorrectLongestSuitLength() {

        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(1, 1, 0, 1));

        int numberOfHearts = 4;
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(1, numberOfHearts, 1, 1));

        HandEvaluations threeSuitsWithOneCardHandEvaluations = new HandEvaluations(firstHand);
        HandEvaluations manyHeartsHandEvaluations = new HandEvaluations(secondHand);

        assertEquals(0, emptyHandEvaluations.getLongestSuitLength());
        assertEquals(1, threeSuitsWithOneCardHandEvaluations.getLongestSuitLength());
        assertEquals(numberOfHearts, manyHeartsHandEvaluations.getLongestSuitLength());
    }

    @Test
    public void shouldReturnTheCorrectNumberOfDoubletons() {

        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 0, 0, 2));
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(2, 2, 0, 2));

        HandEvaluations oneDoubletonHandEvaluations = new HandEvaluations(firstHand);
        HandEvaluations threeDoubletonsHandEvaluations = new HandEvaluations(secondHand);

        assertEquals(0, emptyHandEvaluations.getNumberOfDoubletonSuits());
        assertEquals(1, oneDoubletonHandEvaluations.getNumberOfDoubletonSuits());
        assertEquals(3, threeDoubletonsHandEvaluations.getNumberOfDoubletonSuits());
    }

    @Test
    public void shouldReturnIfItHasFiveOrMoreCardsInAMajorSuit() {
        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 0, 0, 5));
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 5, 0, 5));
        Hand thirdHand = this.createMockedHandWithDistribution(this.createSuitDistribution(7, 0, 0, 5));

        HandEvaluations fiveCardClubsHandEvaluations = new HandEvaluations(firstHand);
        HandEvaluations fiveCardClubsAndHeartsHandEvaluations = new HandEvaluations(secondHand);
        HandEvaluations fiveCardClubsAndSevenSpadesHandEvaluations = new HandEvaluations(thirdHand);

        assertFalse(emptyHandEvaluations.hasFiveOrMoreCardsInAMajorSuit());
        assertFalse(fiveCardClubsHandEvaluations.hasFiveOrMoreCardsInAMajorSuit());
        assertTrue(fiveCardClubsAndHeartsHandEvaluations.hasFiveOrMoreCardsInAMajorSuit());
        assertTrue(fiveCardClubsAndSevenSpadesHandEvaluations.hasFiveOrMoreCardsInAMajorSuit());
    }

    @Test
    public void shouldReturnIfItHasThreeOrMoreCardsInAMinorSuit() {
        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 0, 0, 3));
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 5, 3, 5));
        Hand thirdHand = this.createMockedHandWithDistribution(this.createSuitDistribution(7, 0, 0, 2));

        HandEvaluations threeClubCardHandEvaluations = new HandEvaluations(firstHand);
        HandEvaluations threeClubCardAndFiveHeartsHandEvaluations = new HandEvaluations(secondHand);
        HandEvaluations twoClubCardHandEvaluations = new HandEvaluations(thirdHand);

        assertFalse(emptyHandEvaluations.hasThreeOrMoreCardsInAMinorSuit());
        assertTrue(threeClubCardHandEvaluations.hasThreeOrMoreCardsInAMinorSuit());
        assertTrue(threeClubCardAndFiveHeartsHandEvaluations.hasThreeOrMoreCardsInAMinorSuit());
        assertFalse(twoClubCardHandEvaluations.hasThreeOrMoreCardsInAMinorSuit());
    }

    @Test
    public void shouldReturnIfItHashasEightOrMoreCardsInAnySuit() {
        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(8, 0, 0, 5));
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 5, 8, 0));
        Hand thirdHand = this.createMockedHandWithDistribution(this.createSuitDistribution(7, 0, 0, 5));

        HandEvaluations eightCardsMajorSuitHandEvaluations = new HandEvaluations(firstHand);
        HandEvaluations eightCardsMinorSuitHandEvaluations = new HandEvaluations(secondHand);
        HandEvaluations doNotHaveEightCardsInAnySuitHandEvaluations = new HandEvaluations(thirdHand);

        assertFalse(emptyHandEvaluations.hasFiveOrMoreCardsInAMajorSuit());
        assertTrue(eightCardsMajorSuitHandEvaluations.hasEightOrMoreCardsInAnySuit());
        assertTrue(eightCardsMinorSuitHandEvaluations.hasEightOrMoreCardsInAnySuit());
        assertFalse(doNotHaveEightCardsInAnySuitHandEvaluations.hasEightOrMoreCardsInAnySuit());
    }

    @Test
    public void shouldReturnIfItHasSixCardsInAnySuit() {
        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 0, 0, 6));
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 5, 3, 5));
        Hand thirdHand = this.createMockedHandWithDistribution(this.createSuitDistribution(7, 0, 0, 2));

        HandEvaluations hasSixCardsInSomeSuit = new HandEvaluations(firstHand);
        HandEvaluations hasLessThanSixCardsInAllSuits = new HandEvaluations(secondHand);
        HandEvaluations hasMoreThanSixCardsInSomeSuit = new HandEvaluations(thirdHand);

        assertFalse(emptyHandEvaluations.hasSixCardsInLongestSuit());
        assertTrue(hasSixCardsInSomeSuit.hasSixCardsInLongestSuit());
        assertFalse(hasLessThanSixCardsInAllSuits.hasSixCardsInLongestSuit());
        assertFalse(hasMoreThanSixCardsInSomeSuit.hasSixCardsInLongestSuit());
    }

    @Test
    public void shouldReturnIfItHasSevenCardsInAnySuit() {
        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 0, 0, 7));
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 5, 3, 5));
        Hand thirdHand = this.createMockedHandWithDistribution(this.createSuitDistribution(8, 0, 0, 2));

        HandEvaluations hasSevenCardsInSomeSuit = new HandEvaluations(firstHand);
        HandEvaluations hasLessThanSevenCardsInAllSuits = new HandEvaluations(secondHand);
        HandEvaluations hasMoreThanSevenCardsInSomeSuit = new HandEvaluations(thirdHand);

        assertFalse(emptyHandEvaluations.hasSevenCardsInLongestSuit());
        assertTrue(hasSevenCardsInSomeSuit.hasSevenCardsInLongestSuit());
        assertFalse(hasLessThanSevenCardsInAllSuits.hasSevenCardsInLongestSuit());
        assertFalse(hasMoreThanSevenCardsInSomeSuit.hasSevenCardsInLongestSuit());
    }

    @Test
    public void shouldReturnIfItHasEightOrMoreCardsInAnySuit() {
        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 0, 0, 8));
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 9, 1, 4));
        Hand thirdHand = this.createMockedHandWithDistribution(this.createSuitDistribution(7, 0, 0, 2));

        HandEvaluations hasEightCardsInSomeSuitHandEvaluations = new HandEvaluations(firstHand);
        HandEvaluations hasMoreThanEightCardsInSomeSuitHandEvaluations = new HandEvaluations(secondHand);
        HandEvaluations hasLessThanEightCardsInEverySuitHandEvaluations = new HandEvaluations(thirdHand);

        assertFalse(emptyHandEvaluations.hasEightOrMoreCardsInAnySuit());
        assertTrue(hasEightCardsInSomeSuitHandEvaluations.hasEightOrMoreCardsInAnySuit());
        assertTrue(hasMoreThanEightCardsInSomeSuitHandEvaluations.hasEightOrMoreCardsInAnySuit());
        assertFalse(hasLessThanEightCardsInEverySuitHandEvaluations.hasEightOrMoreCardsInAnySuit());
    }

    @Test
    public void shouldReturnIfItHasTwoOutOfThreeHigherCardsInSuit() {
        Hand handWithAceAndKingInSpadesSuit = this.createMockedHandWithSpecificCards(aceOfSpades, kingOfSpades);
        Hand handWithKingAndQueenInSpadesSuit = this.createMockedHandWithSpecificCards(kingOfSpades, queenOfSpades);
        Hand handWithAceAndQueenInSpadesSuit = this.createMockedHandWithSpecificCards(aceOfSpades, queenOfSpades);
        Hand handWithAceAndJackInSpadesSuit = this.createMockedHandWithSpecificCards(aceOfSpades, jackOfSpades);
        Hand handWithThreeHigherCards = this.createMockedHandWithSpecificCards(aceOfSpades, kingOfSpades, queenOfSpades);

        HandEvaluations hasAceAndKingInSpadesHandEvaluations = new HandEvaluations(handWithAceAndKingInSpadesSuit);
        HandEvaluations hasKingAndQueenInSpadesHandEvaluations = new HandEvaluations(handWithKingAndQueenInSpadesSuit);
        HandEvaluations hasAceAndQueenInSpadesHandEvaluations = new HandEvaluations(handWithAceAndQueenInSpadesSuit);
        HandEvaluations hasAceAndJackInSpadesHandEvaluations = new HandEvaluations(handWithAceAndJackInSpadesSuit);
        HandEvaluations hasAceAndKingAndQueenHandEvaluations = new HandEvaluations(handWithThreeHigherCards);

        assertFalse(emptyHandEvaluations.hasTwoOutOfThreeHigherCards(Suit.getSPADES()));
        assertTrue(hasAceAndKingInSpadesHandEvaluations.hasTwoOutOfThreeHigherCards(Suit.getSPADES()));
        assertTrue(hasKingAndQueenInSpadesHandEvaluations.hasTwoOutOfThreeHigherCards(Suit.getSPADES()));
        assertTrue(hasAceAndQueenInSpadesHandEvaluations.hasTwoOutOfThreeHigherCards(Suit.getSPADES()));
        assertFalse(hasAceAndJackInSpadesHandEvaluations.hasTwoOutOfThreeHigherCards(Suit.getSPADES()));
        assertTrue(hasAceAndKingAndQueenHandEvaluations.hasTwoOutOfThreeHigherCards(Suit.getSPADES()));
    }

    @Test
    public void shouldReturnIfItHasThreeOutOfFiveHigherCardsInSuit() {
        Hand handWithAceAndKingAndQueenInSpadesSuit = this.createMockedHandWithSpecificCards(aceOfSpades, kingOfSpades, queenOfSpades);
        Hand handWithQueenAndJackAndTenInSpadesSuit = this.createMockedHandWithSpecificCards(queenOfSpades, jackOfSpades, tenOfSpades);
        Hand handWithAceAndQueenAndNineInSpadesSuit = this.createMockedHandWithSpecificCards(aceOfSpades, queenOfSpades, nineOfSpades);

        HandEvaluations hasAceAndKingAndQueenInSpadesHandEvaluations = new HandEvaluations(handWithAceAndKingAndQueenInSpadesSuit);
        HandEvaluations hasQueenAndJackAndTenInSpadesHandEvaluations = new HandEvaluations(handWithQueenAndJackAndTenInSpadesSuit);
        HandEvaluations hasAceAndQueenAndNineInSpadesHandEvaluations = new HandEvaluations(handWithAceAndQueenAndNineInSpadesSuit);

        assertFalse(emptyHandEvaluations.hasThreeOutOfFiveHigherCards(Suit.getSPADES()));
        assertTrue(hasAceAndKingAndQueenInSpadesHandEvaluations.hasThreeOutOfFiveHigherCards(Suit.getSPADES()));
        assertTrue(hasQueenAndJackAndTenInSpadesHandEvaluations.hasThreeOutOfFiveHigherCards(Suit.getSPADES()));
        assertFalse(hasAceAndQueenAndNineInSpadesHandEvaluations.hasThreeOutOfFiveHigherCards(Suit.getSPADES()));
    }

    @Test
    public void shouldReturnIfItIsBalanced() {
        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(4, 4, 5, 0));
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(4, 1, 4, 4));
        Hand thirdHand = this.createMockedHandWithDistribution(this.createSuitDistribution(2, 2, 3, 6));
        Hand fourthHand = this.createMockedHandWithDistribution(this.createSuitDistribution(2, 3, 3, 5));
        Hand fifthHand = this.createMockedHandWithDistribution(this.createSuitDistribution(2, 2, 4, 5));

        HandEvaluations voidClubsHandEvaluations = new HandEvaluations(firstHand);
        HandEvaluations singletonHeartsHandEvaluations = new HandEvaluations(secondHand);
        HandEvaluations sixClubCardsHandEvaluations = new HandEvaluations(thirdHand);
        HandEvaluations fiveThreeTwoTwoHandEvaluations = new HandEvaluations(fourthHand);
        HandEvaluations twoDoubletonsHandEvaluations = new HandEvaluations(fifthHand);

        assertFalse(emptyHandEvaluations.isBalanced());
        assertFalse(voidClubsHandEvaluations.isBalanced());
        assertFalse(singletonHeartsHandEvaluations.isBalanced());
        assertFalse(sixClubCardsHandEvaluations.isBalanced());
        assertTrue(fiveThreeTwoTwoHandEvaluations.isBalanced());
        assertFalse(twoDoubletonsHandEvaluations.isBalanced());
    }

    @Test
    public void shouldReturnIfItHasFourOrMoreCardsInMajorSuitExcludingSuitWithMoreCards() {
        Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(8, 4, 1, 0));
        Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(5, 8, 0, 0));
        Hand thirdHand = this.createMockedHandWithDistribution(this.createSuitDistribution(3, 3, 7, 0));

        HandEvaluations hasSpadesAsLongestSuitAndFourHeartsCardHandEvaluations = new HandEvaluations(firstHand);
        HandEvaluations hasHeartsAsLongestSuitAndFiveSpadesCardHandEvaluations = new HandEvaluations(secondHand);
        HandEvaluations hasMinorSuitAsLongestAndDoesNotHaveFourOrMoreCardsInMajorSuitHandEvaluations = new HandEvaluations(thirdHand);

        assertFalse(emptyHandEvaluations.hasFourOrMoreCardsInMajorSuitExcludingLongestSuit());
        assertTrue(hasSpadesAsLongestSuitAndFourHeartsCardHandEvaluations.hasFourOrMoreCardsInMajorSuitExcludingLongestSuit());
        assertTrue(hasHeartsAsLongestSuitAndFiveSpadesCardHandEvaluations.hasFourOrMoreCardsInMajorSuitExcludingLongestSuit());
        assertFalse(hasMinorSuitAsLongestAndDoesNotHaveFourOrMoreCardsInMajorSuitHandEvaluations.hasFourOrMoreCardsInMajorSuitExcludingLongestSuit());
    }

    private Hand createMockedHandWithDistribution(Map<Suit, Integer> suitDistribution) {
        List<Card> mockedCards = new ArrayList<Card>();
        Map<Suit, Card> mockedCardOfSuit = new EnumMap<Suit, Card>(Suit.class);
        mockedCardOfSuit.put(Suit.getSPADES(), aceOfSpades);
        mockedCardOfSuit.put(Suit.getHEARTS(), kingOfHearts);
        mockedCardOfSuit.put(Suit.getDIAMONDS(), sevenOfDiamonds);
        mockedCardOfSuit.put(Suit.getCLUBS(), threeOfClubs);

        for (Suit suit : Suit.values()) {
            Integer numberOfCards = suitDistribution.get(suit);
            if (numberOfCards == null) {
                continue;
            }
            for (int i = 0; i < numberOfCards; i++) {
                mockedCards.add(mockedCardOfSuit.get(suit));
            }
        }

        return new Hand(mockedCards);
    }

    private Map<Suit, Integer> createSuitDistribution(int spades, int hearts, int diamonds, int clubs) {
        Map<Suit, Integer> suitDistribution = new EnumMap<Suit, Integer>(Suit.class);
        suitDistribution.put(Suit.getSPADES(), spades);
        suitDistribution.put(Suit.getHEARTS(), hearts);
        suitDistribution.put(Suit.getDIAMONDS(), diamonds);
        suitDistribution.put(Suit.getCLUBS(), clubs);
        return suitDistribution;
    }

    private Hand createMockedHandWithSpecificCards(Card card1, Card card2) {
        List<Card> mockedCards = new ArrayList<Card>();
        mockedCards.add(card1);
        mockedCards.add(card2);
        return new Hand(mockedCards);
    }

    private Hand createMockedHandWithSpecificCards(Card card1, Card card2, Card card3) {
        List<Card> mockedCards = new ArrayList<Card>();
        mockedCards.add(card1);
        mockedCards.add(card2);
        mockedCards.add(card3);
        return new Hand(mockedCards);
    }
}
