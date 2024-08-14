package club.libridge.libridgebackend.dto;

import java.util.List;

import club.libridge.libridgebackend.core.Hand;
import lombok.Value;
import scalabridge.Call;

@Value
public class HandWithCandidateBidsDTO {

    private String hand;
    private String call;
    private List<CallWithProbabilityDTO> candidates;

    public HandWithCandidateBidsDTO(Hand hand, Call call, List<CallWithProbabilityDTO> candidates) {
        this.hand = hand.toString();
        this.call = call.toString();
        this.candidates = candidates;
    }

}
