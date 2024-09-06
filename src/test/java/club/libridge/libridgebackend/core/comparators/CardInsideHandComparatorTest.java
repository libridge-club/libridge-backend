package club.libridge.libridgebackend.core.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import scalabridge.Card;
import scalabridge.Rank;
import scalabridge.Suit;

public class CardInsideHandComparatorTest {

    private Card aceOfClubs = new Card(Suit.getCLUBS(), Rank.getACE());
    private Card aceOfDiamonds = new Card(Suit.getDIAMONDS(), Rank.getACE());
    private Card aceOfHearts = new Card(Suit.getHEARTS(), Rank.getACE());
    private Card aceOfSpades = new Card(Suit.getSPADES(), Rank.getACE());

    private Card kingOfClubs = new Card(Suit.getCLUBS(), Rank.getKING());
    private Card tenOfDiamonds = new Card(Suit.getDIAMONDS(), Rank.getTEN());
    private Card sevenOfHearts = new Card(Suit.getHEARTS(), Rank.getSEVEN());
    private Card twoOfSpades = new Card(Suit.getSPADES(), Rank.getTWO());

    @Test
    public void shouldCompareSuitsCorrectly() {
        CardInsideHandComparator cardInsideHandComparator = new CardInsideHandComparator();

        assertEquals(0, cardInsideHandComparator.compare(aceOfClubs, aceOfClubs));
        assertEquals(0, cardInsideHandComparator.compare(aceOfDiamonds, aceOfDiamonds));
        assertEquals(0, cardInsideHandComparator.compare(aceOfHearts, aceOfHearts));
        assertEquals(0, cardInsideHandComparator.compare(aceOfSpades, aceOfSpades));

        assertTrue(cardInsideHandComparator.compare(aceOfSpades, aceOfHearts) < 0);
        assertTrue(cardInsideHandComparator.compare(aceOfSpades, aceOfClubs) < 0);
        assertTrue(cardInsideHandComparator.compare(aceOfSpades, aceOfDiamonds) < 0);

        assertTrue(cardInsideHandComparator.compare(aceOfHearts, aceOfClubs) < 0);
        assertTrue(cardInsideHandComparator.compare(aceOfHearts, aceOfDiamonds) < 0);

        assertTrue(cardInsideHandComparator.compare(aceOfClubs, aceOfDiamonds) < 0);

    }

    @Test
    public void shouldCompareRanksCorrectlyInsideSuit() {
        CardInsideHandComparator cardInsideHandComparator = new CardInsideHandComparator();

        assertTrue(cardInsideHandComparator.compare(kingOfClubs, aceOfClubs) > 0);
        assertTrue(cardInsideHandComparator.compare(tenOfDiamonds, aceOfDiamonds) > 0);
        assertTrue(cardInsideHandComparator.compare(sevenOfHearts, aceOfHearts) > 0);
        assertTrue(cardInsideHandComparator.compare(twoOfSpades, aceOfSpades) > 0);

        assertEquals(0, cardInsideHandComparator.compare(kingOfClubs, kingOfClubs));
        assertEquals(0, cardInsideHandComparator.compare(tenOfDiamonds, tenOfDiamonds));
        assertEquals(0, cardInsideHandComparator.compare(sevenOfHearts, sevenOfHearts));
        assertEquals(0, cardInsideHandComparator.compare(twoOfSpades, twoOfSpades));

    }

}
