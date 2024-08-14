package club.libridge.libridgebackend.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import scalabridge.Call;

@AllArgsConstructor
public class ExpectedCallDTO {

    @SuppressWarnings("unused")
    private UUID boardID;
    @Getter
    @Setter
    private Call call;

}
