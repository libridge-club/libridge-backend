package club.libridge.libridgebackend.core.comparators;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import scalabridge.Card;
import scalabridge.Rank;
import scalabridge.Suit;

public class CardInsideHandWithSuitComparatorTest {

    private Card aceOfClubs = new Card(Suit.getCLUBS(), Rank.getACE());
    private Card aceOfDiamonds = new Card(Suit.getDIAMONDS(), Rank.getACE());
    private Card aceOfHearts = new Card(Suit.getHEARTS(), Rank.getACE());
    private Card aceOfSpades = new Card(Suit.getSPADES(), Rank.getACE());

    @Test
    public void shouldCompareSuitsCorrectlyWithClubsAsTrumpSuit() {
        CardInsideHandWithSuitComparator cardInsideHandWithSuitComparator = new CardInsideHandWithSuitComparator(Suit.getCLUBS());

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfClubs) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfDiamonds) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfHearts) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfSpades) == 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfHearts) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfSpades) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfDiamonds) < 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfSpades) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfDiamonds) < 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfDiamonds) < 0);

    }

    @Test
    public void shouldCompareSuitsCorrectlyWithDiamondsAsTrumpSuit() {
        CardInsideHandWithSuitComparator cardInsideHandWithSuitComparator = new CardInsideHandWithSuitComparator(Suit.getDIAMONDS());

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfClubs) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfDiamonds) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfHearts) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfSpades) == 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfSpades) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfHearts) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfClubs) < 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfHearts) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfClubs) < 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfClubs) < 0);

    }

    @Test
    public void shouldCompareSuitsCorrectlyWithHeartsAsTrumpSuit() {
        CardInsideHandWithSuitComparator cardInsideHandWithSuitComparator = new CardInsideHandWithSuitComparator(Suit.getHEARTS());

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfClubs) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfDiamonds) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfHearts) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfSpades) == 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfSpades) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfDiamonds) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfClubs) < 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfDiamonds) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfClubs) < 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfClubs) < 0);

    }

    @Test
    public void shouldCompareSuitsCorrectlyWithSpadesAsTrumpSuit() {
        CardInsideHandWithSuitComparator cardInsideHandWithSuitComparator = new CardInsideHandWithSuitComparator(Suit.getSPADES());

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfClubs) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfDiamonds) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfHearts) == 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfSpades) == 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfHearts) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfClubs) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfDiamonds) < 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfClubs) < 0);
        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfDiamonds) < 0);

        assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfDiamonds) < 0);

    }

}
