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
        shortDescriptionOfRulesets.put(Strain.getCLUBS().getName(), new PositiveWithTrumpsRuleset(Suit.getCLUBS()));
        shortDescriptionOfRulesets.put(Strain.getDIAMONDS().getName(), new PositiveWithTrumpsRuleset(Suit.getDIAMONDS()));
        shortDescriptionOfRulesets.put(Strain.getHEARTS().getName(), new PositiveWithTrumpsRuleset(Suit.getHEARTS()));
        shortDescriptionOfRulesets.put(Strain.getSPADES().getName(), new PositiveWithTrumpsRuleset(Suit.getSPADES()));
        shortDescriptionOfRulesets.put(Strain.getNOTRUMPS().getName(), new PositiveNoTrumpsRuleset());
    }

    public static PositiveRuleset identify(@NonNull String strain) {
        return shortDescriptionOfRulesets.get(strain);
    }

}
