package club.libridge.libridgebackend.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;
import scalabridge.Bid;
import scalabridge.Call;
import scalabridge.DoubleCall;
import scalabridge.OddTricks;
import scalabridge.PassingCall;
import scalabridge.RedoubleCall;
import scalabridge.Strain;

public final class BiddingBox {

    private static final Map<String, Call> LABEL_TO_BID_MAP;
    private static final String PASS_STRING = "P";
    private static final String DOUBLE_STRING = "X";
    private static final String REDOUBLE_STRING = "XX";

    // Easy shortcuts for non contract bids
    public static final Call PASS;
    public static final Call DOUBLE;
    public static final Call REDOUBLE;

    static {
        HashMap<String, Call> modifiableMap = new HashMap<>();
        modifiableMap.put(PASS_STRING, PassingCall.instance());
        modifiableMap.put(DOUBLE_STRING, DoubleCall.instance());
        modifiableMap.put(REDOUBLE_STRING, RedoubleCall.instance());

        for (OddTricks oddTricks : OddTricks.values()) {
            for (Strain strain : Strain.values()) {
                String label = oddTricks.getSymbol() + strain.getSymbol();
                modifiableMap.put(label, new Bid(oddTricks, strain));
            }
        }
        LABEL_TO_BID_MAP = Collections.unmodifiableMap(modifiableMap);
        PASS = LABEL_TO_BID_MAP.get(PASS_STRING);
        DOUBLE = LABEL_TO_BID_MAP.get(DOUBLE_STRING);
        REDOUBLE = LABEL_TO_BID_MAP.get(REDOUBLE_STRING);
    }

    private BiddingBox() {
        // Static class. Should not be instantiated
    }

    public static Call get(@NonNull String label) {
        return LABEL_TO_BID_MAP.get(label.toUpperCase());
    }

    public static Call get(@NonNull Call call) {
        if (call.isPass()) {
            return PASS;
        } else if (call.isDouble()) {
            return DOUBLE;
        } else if (call.isRedouble()) {
            return REDOUBLE;
        } else if (call.isBid()) {
            Bid bid = (Bid) call;
            String label = bid.getOddTricks().getSymbol() + bid.getStrain().getSymbol();
            return LABEL_TO_BID_MAP.get(label);
        }
        throw new IllegalArgumentException("Call must be a PASS, DOUBLE, REDOUBLE or BID");
    }

    public static Call getBid(@NonNull OddTricks oddTricks, @NonNull Strain strain) {
        String label = oddTricks.getSymbol() + strain.getSymbol();
        return (Bid) get(label);
    }

}
