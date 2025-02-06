package club.libridge.libridgebackend.app.persistence;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity(name = "_Table")
@Validated
@Getter
public class TableEntity {

    @Id
    @NotNull
    @GeneratedValue
    private UUID id;

}
