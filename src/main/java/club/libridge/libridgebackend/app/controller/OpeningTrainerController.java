package club.libridge.libridgebackend.app.controller;

import static club.libridge.libridgebackend.logging.LibridgeLogger.LOGGER;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import club.libridge.libridgebackend.app.persistence.BoardFactory;
import club.libridge.libridgebackend.app.service.OpeningTrainerService;
import club.libridge.libridgebackend.dto.ExpectedCallDTO;
import club.libridge.libridgebackend.dto.HandWithCandidateBidsDTO;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import scalabridge.Direction;

@RestController
@RequestMapping("/openingTrainer")
@AllArgsConstructor
public class OpeningTrainerController {

    @NonNull
    private final BoardFactory boardFactory;

    @NonNull
    private final OpeningTrainerService openingTrainerService;

    @GetMapping("/{boardId}/{direction}")
    public ExpectedCallDTO getExpectedCall(@PathVariable UUID boardId, @PathVariable Direction direction) {
        LOGGER.trace("getExpectedCall");
        Optional<ExpectedCallDTO> expectedCallOption = openingTrainerService.getExpectedCall(boardId, direction);
        if (expectedCallOption.isPresent()) {
            return expectedCallOption.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no board in the database with this ID");
        }
    }

    @GetMapping("/getRandom")
    public HandWithCandidateBidsDTO getRandom(@RequestParam(required = false) Boolean avoidPass) {
        LOGGER.trace("openingTrainer_getRandom");
        Boolean myAvoidPass = false;
        if (avoidPass != null) {
            myAvoidPass = avoidPass;
        }
        return this.openingTrainerService.getRandomHandWithCandidateBids(myAvoidPass);
    }
}
