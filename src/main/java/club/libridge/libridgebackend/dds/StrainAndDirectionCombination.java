package club.libridge.libridgebackend.dds;

import scalabridge.Direction;
import scalabridge.Strain;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@AllArgsConstructor
@EqualsAndHashCode
public class StrainAndDirectionCombination {

    @NonNull
    private final Strain strain;
    @NonNull
    private final Direction direction;

}
