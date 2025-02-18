package club.libridge.libridgebackend.app.persistence;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import club.libridge.libridgebackend.core.PavlicekNumber;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import scalabridge.CompleteDeckInFourHands;

@Entity(name = "Board")
@Getter
@Validated
public class BoardEntity {

    @Id
    @NotNull
    @GeneratedValue
    private UUID id;

    @Column
    @NotNull
    private String pavlicekNumber;

    /**
     * This is the inverse side of the relationship
     */
    @Setter
    @OneToOne(mappedBy = "boardEntity", cascade = CascadeType.ALL)
    private DoubleDummyTableEntity doubleDummyTableEntity;

    /**
     * @deprecated Spring eyes only
     */
    @Deprecated
    @SuppressWarnings("unused")
    private BoardEntity() {
    }

    public BoardEntity(@NotNull CompleteDeckInFourHands completeDeckInFourHands) {
        PavlicekNumber pavlicekNumberGenerator = new PavlicekNumber();
        this.pavlicekNumber = pavlicekNumberGenerator.getNumberFromBoard(completeDeckInFourHands).toString();
    }

    public BoardEntity(@NotNull String pavlicekNumber) {
        this.pavlicekNumber = pavlicekNumber;
    }

}
