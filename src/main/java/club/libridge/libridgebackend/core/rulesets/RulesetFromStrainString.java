package club.libridge.libridgebackend.core.rulesets;

import java.util.HashMap;
import java.util.Map;

import club.libridge.libridgebackend.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import club.libridge.libridgebackend.core.rulesets.concrete.PositiveRuleset;
import club.libridge.libridgebackend.core.rulesets.concrete.PositiveWithTrumpsRuleset;
import lombok.NonNull;
import scalabridge.Strain;
import scalabridge.Suit;

public final class RulesetFromStrainString {

    private RulesetFromStrainString() {
        throw new IllegalStateException("Utility class");
    }

    private static Map<String, PositiveRuleset> shortDescriptionOfRulesets = new HashMap<String, PositiveRuleset>();

    // Static initialization block to avoid doing this calculation every time
    // identify(..) is called.
    static {
        shortDescriptionOfRulesets.put(Strain.CLUBS.getName(), new PositiveWithTrumpsRuleset(Suit.CLUBS));
        shortDescriptionOfRulesets.put(Strain.DIAMONDS.getName(), new PositiveWithTrumpsRuleset(Suit.DIAMONDS));
        shortDescriptionOfRulesets.put(Strain.HEARTS.getName(), new PositiveWithTrumpsRuleset(Suit.HEARTS));
        shortDescriptionOfRulesets.put(Strain.SPADES.getName(), new PositiveWithTrumpsRuleset(Suit.SPADES));
        shortDescriptionOfRulesets.put(Strain.NOTRUMPS.getName(), new PositiveNoTrumpsRuleset());
    }

    public static PositiveRuleset identify(@NonNull String strain) {
        return shortDescriptionOfRulesets.get(strain);
    }

}
