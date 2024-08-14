package club.libridge.libridgebackend.networking.server.notifications;

import lombok.Getter;
import scalabridge.Strain;

public class StrainNotification {

    @Getter
    private Strain strain;

    public void notifyAllWithStrain(Strain strain) {
        this.strain = strain;
        this.notifyAll(); // NOSONAR WONTFIX
    }

}
