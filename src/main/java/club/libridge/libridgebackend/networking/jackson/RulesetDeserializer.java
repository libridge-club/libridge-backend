package club.libridge.libridgebackend.networking.jackson;

import static club.libridge.libridgebackend.logging.LibridgeLogger.LOGGER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import scalabridge.Suit;
import club.libridge.libridgebackend.core.rulesets.abstractrulesets.Ruleset;
import club.libridge.libridgebackend.core.rulesets.concrete.PositiveNoTrumpsRuleset;
import club.libridge.libridgebackend.core.rulesets.concrete.PositiveRuleset;
import club.libridge.libridgebackend.core.rulesets.concrete.PositiveWithTrumpsRuleset;

public class RulesetDeserializer extends StdDeserializer<Ruleset> {

    private static final Map<String, Class<? extends Ruleset>> SIMPLE_NAME_TO_CLASS;
    private static final Map<String, Suit> SUIT_NAME_TO_SUIT;

    // Static initialization block to avoid doing this calculation every
    // deserialization
    static {
        SIMPLE_NAME_TO_CLASS = new HashMap<>();
        SIMPLE_NAME_TO_CLASS.put("NoRuleset", PositiveRuleset.class);
        SIMPLE_NAME_TO_CLASS.put("PositiveNoTrumpsRuleset", PositiveNoTrumpsRuleset.class);
        SIMPLE_NAME_TO_CLASS.put("PositiveWithTrumpsRuleset", PositiveWithTrumpsRuleset.class);

        SUIT_NAME_TO_SUIT = new HashMap<>();
        SUIT_NAME_TO_SUIT.put("Diamonds", Suit.DIAMONDS);
        SUIT_NAME_TO_SUIT.put("Clubs", Suit.CLUBS);
        SUIT_NAME_TO_SUIT.put("Hearts", Suit.HEARTS);
        SUIT_NAME_TO_SUIT.put("Spades", Suit.SPADES);
    }

    public RulesetDeserializer() {
        this(null);
    }

    public RulesetDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Ruleset deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String rulesetName = node.get("rulesetName").asText();
        String trumpSuit = node.get("trumpSuit").asText();
        Class<? extends Ruleset> class1 = SIMPLE_NAME_TO_CLASS.get(rulesetName);
        Ruleset response = new PositiveRuleset();
        if (PositiveWithTrumpsRuleset.class.equals(class1)) {
            response = new PositiveWithTrumpsRuleset(SUIT_NAME_TO_SUIT.get(trumpSuit));
        } else {
            try {
                response = class1.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
        return response;
    }
}
