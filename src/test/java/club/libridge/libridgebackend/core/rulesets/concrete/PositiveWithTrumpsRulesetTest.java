package club.libridge.libridgebackend.core.rulesets.concrete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scalabridge.Suit;
import club.libridge.libridgebackend.core.comparators.CardInsideHandWithSuitComparator;

public class PositiveWithTrumpsRulesetTest {

    private PositiveWithTrumpsRuleset positiveWithTrumpRuleset;
    private Suit trumpSuit;

    @BeforeEach
    public void createPositiveNoTrumpsRuleset() {
        trumpSuit = Suit.SPADES;
        this.positiveWithTrumpRuleset = new PositiveWithTrumpsRuleset(trumpSuit);
    }

    @Test
    public void shouldGetTrumpSuit() {
        assertEquals(trumpSuit, this.positiveWithTrumpRuleset.getTrumpSuit());
    }

    @Test
    public void shouldGetComparator() {
        assertEquals(trumpSuit, this.positiveWithTrumpRuleset.getTrumpSuit());
        assertTrue(this.positiveWithTrumpRuleset.getCardComparator() instanceof CardInsideHandWithSuitComparator);
    }

}
