package club.libridge.libridgebackend.core.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import scalabridge.Card;
import scalabridge.Rank;
import scalabridge.Suit;

public class RankComparatorTest {

    private Card aceOfClubs = new Card(Suit.getCLUBS(), Rank.getACE());
    private Card aceOfDiamonds = new Card(Suit.getDIAMONDS(), Rank.getACE());
    private Card aceOfHearts = new Card(Suit.getHEARTS(), Rank.getACE());
    private Card aceOfSpades = new Card(Suit.getSPADES(), Rank.getACE());

    private Card kingOfClubs = new Card(Suit.getCLUBS(), Rank.getKING());
    private Card tenOfDiamonds = new Card(Suit.getDIAMONDS(), Rank.getTEN());
    private Card sevenOfHearts = new Card(Suit.getHEARTS(), Rank.getSEVEN());
    private Card twoOfSpades = new Card(Suit.getSPADES(), Rank.getTWO());

    @Test
    public void shouldCompareRanksCorrectlyInsideSuit() {
        RankComparator rankComparator = new RankComparator();

        assertTrue(rankComparator.compare(kingOfClubs, aceOfClubs) < 0);
        assertTrue(rankComparator.compare(tenOfDiamonds, aceOfDiamonds) < 0);
        assertTrue(rankComparator.compare(sevenOfHearts, aceOfHearts) < 0);
        assertTrue(rankComparator.compare(twoOfSpades, aceOfSpades) < 0);

        assertEquals(0, rankComparator.compare(kingOfClubs, kingOfClubs));
        assertEquals(0, rankComparator.compare(tenOfDiamonds, tenOfDiamonds));
        assertEquals(0, rankComparator.compare(sevenOfHearts, sevenOfHearts));
        assertEquals(0, rankComparator.compare(twoOfSpades, twoOfSpades));

    }

    @Test
    public void shouldIgnoreSuit() {
        RankComparator rankComparator = new RankComparator();

        assertEquals(rankComparator.compare(kingOfClubs, aceOfClubs), rankComparator.compare(kingOfClubs, aceOfDiamonds));
        assertEquals(0, rankComparator.compare(aceOfClubs, aceOfDiamonds));

    }

}
