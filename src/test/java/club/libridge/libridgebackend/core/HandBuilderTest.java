package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import scalabridge.Card;
import scalabridge.Rank;
import scalabridge.Suit;

public class HandBuilderTest {

    @Test
    public void myTest() {
        String first = "q5.kt85.qjt8632.";
        HandBuilder handBuilder = new HandBuilder();
        Card sq = new Card(Suit.SPADES, Rank.QUEEN);
        Card s5 = new Card(Suit.SPADES, Rank.FIVE);
        Card s3 = new Card(Suit.SPADES, Rank.THREE);
        Card ht = new Card(Suit.HEARTS, Rank.TEN);
        Card d8 = new Card(Suit.DIAMONDS, Rank.EIGHT);
        Card ca = new Card(Suit.CLUBS, Rank.ACE);

        Hand firstHand = handBuilder.buildFromDotSeparatedString(first);

        assertTrue(firstHand.containsCard(sq));
        assertTrue(firstHand.containsCard(s5));
        assertFalse(firstHand.containsCard(s3));
        assertTrue(firstHand.containsCard(ht));
        assertTrue(firstHand.containsCard(d8));
        assertFalse(firstHand.containsCard(ca));
    }
}
