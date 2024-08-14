package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import scalabridge.Bid;
import scalabridge.Call;
import scalabridge.DoubleCall;
import scalabridge.OddTricks;
import scalabridge.PassingCall;
import scalabridge.RedoubleCall;
import scalabridge.Strain;

class BiddingBoxTest {

    static final String PASS_STRING = "P";
    static final String DOUBLE_STRING = "X";
    static final String REDOUBLE_STRING = "XX";
    static final String THREE_NO_TRUMPS_STRING = "3N";
    static final OddTricks three = OddTricks.THREE;
    static final Strain noTrumps = Strain.NOTRUMPS;

    static final Call passingBid = PassingCall.instance();
    static final Call doubleBid = DoubleCall.instance();
    static final Call redoubleBid = RedoubleCall.instance();
    static final Bid threeNoTrumps = new Bid(three, noTrumps);

    @Test
    void passShouldBeABidEqualToAnyOtherPASS() {
        assertEquals(passingBid, BiddingBox.PASS);
    }

    @Test
    void passCanBeConstructedUsingGetter() {
        assertEquals(passingBid, BiddingBox.get(PASS_STRING));
    }

    @Test
    void doubleShouldBeABidEqualToAnyOtherDouble() {
        assertEquals(doubleBid, BiddingBox.DOUBLE);
    }

    @Test
    void doubleCanBeConstructedUsingGetter() {
        assertEquals(doubleBid, BiddingBox.get(DOUBLE_STRING));
    }

    @Test
    void redoubleShouldBeABidEqualToAnyOtherRedouble() {
        assertEquals(redoubleBid, BiddingBox.REDOUBLE);
    }

    @Test
    void redoubleCanBeConstructedUsingGetter() {
        assertEquals(redoubleBid, BiddingBox.get(REDOUBLE_STRING));
    }

    @Test
    void contractBidsShouldBeEqualToAnyOthercontractBidsOfTheSameOddTricksAndStrain() {
        assertEquals(threeNoTrumps, BiddingBox.get(THREE_NO_TRUMPS_STRING));
    }

}
