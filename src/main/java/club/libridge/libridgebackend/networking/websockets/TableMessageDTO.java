package club.libridge.libridgebackend.networking.websockets;

import club.libridge.libridgebackend.core.Deal;
import scalabridge.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TableMessageDTO {

    private String message;
    private String tableId;
    private Deal deal;
    private String content;
    private Direction direction;

    public static final class Builder {
        private String message;
        private String tableId;
        private Deal deal;
        private String content;
        private Direction direction;

        public Builder() {
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withTableId(String tableId) {
            this.tableId = tableId;
            return this;
        }

        public Builder withDeal(Deal deal) {
            this.deal = deal;
            return this;
        }

        public Builder withContent(String content) {
            this.content = content;
            return this;
        }

        public Builder withDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public TableMessageDTO build() {
            return new TableMessageDTO(this.message, this.tableId, this.deal, this.content, this.direction);
        }
    }
}
