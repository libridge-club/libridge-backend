package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import scalabridge.Bid;
import scalabridge.Call;
import scalabridge.OddTricks;
import scalabridge.Strain;

class BidTest {

    static final OddTricks three = OddTricks.THREE;
    static final Strain clubs = Strain.CLUBS;
    static final Bid subject = new Bid(three, clubs);

    // FIXME This test should be moved to scalabridge
    // @Test
    // @SuppressWarnings("null")
    // void constructor_throwsIllegalArgumentExceptionWithNullParams() {
    // Assertions.assertThrows(IllegalArgumentException.class, () -> {
    // new Bid(null, clubs);
    // });
    // Assertions.assertThrows(IllegalArgumentException.class, () -> {
    // new Bid(three, null);
    // });
    // Assertions.assertThrows(IllegalArgumentException.class, () -> {
    // new Bid(null, null);
    // });
    // }

    @Test
    void contractBidIsACall() {
        assertTrue(subject instanceof Call);
    }

    @Test
    void hasTheOddTricksProvidedAtConstruction() {
        assertEquals(three, subject.getOddTricks());
    }

    @Test
    void hasTheStrainProvidedAtConstruction() {
        assertEquals(clubs, subject.getStrain());
    }

    @Test
    void shouldBeComparable() {
        Bid oneClubs = new Bid(OddTricks.ONE, Strain.CLUBS);
        Bid anotherOneClubs = new Bid(OddTricks.ONE, Strain.CLUBS);
        Bid oneDiamonds = new Bid(OddTricks.ONE, Strain.DIAMONDS);
        Bid twoClubs = new Bid(OddTricks.TWO, Strain.CLUBS);

        assertEquals(0, oneClubs.compareTo(anotherOneClubs));
        assertTrue(oneClubs.compareTo(oneDiamonds) < 0);
        assertTrue(oneClubs.compareTo(twoClubs) < 0);

        assertTrue(oneDiamonds.compareTo(oneClubs) > 0);
        assertTrue(oneDiamonds.compareTo(twoClubs) < 0);

        assertTrue(twoClubs.compareTo(oneClubs) > 0);
        assertTrue(twoClubs.compareTo(oneDiamonds) > 0);
    }

}
