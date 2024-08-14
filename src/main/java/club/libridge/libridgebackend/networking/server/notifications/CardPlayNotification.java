package club.libridge.libridgebackend.networking.server.notifications;

import scalabridge.Card;
import scalabridge.Direction;
import lombok.Getter;

public class CardPlayNotification {

    @Getter
    private Card card;
    @Getter
    private Direction direction;

    public void notifyAllWithCardAndDirection(Card card, Direction direction) {
        this.card = card;
        this.direction = direction;
        this.notifyAll(); // NOSONAR WONTFIX
    }

}
