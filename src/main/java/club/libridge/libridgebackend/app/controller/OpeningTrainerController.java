package club.libridge.libridgebackend.app.controller;

import static club.libridge.libridgebackend.logging.LibridgeLogger.LOGGER;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import club.libridge.libridgebackend.app.persistence.BoardEntity;
import club.libridge.libridgebackend.app.persistence.BoardFactory;
import club.libridge.libridgebackend.app.persistence.BoardRepository;
import club.libridge.libridgebackend.app.service.OpeningTrainerService;
import club.libridge.libridgebackend.core.Direction;
import club.libridge.libridgebackend.dto.ExpectedCallDTO;
import club.libridge.libridgebackend.dto.HandWithCandidateBidsDTO;
import club.libridge.libridgebackend.networking.server.LibridgeServer;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@RestController
@RequestMapping("/openingTrainer")
@AllArgsConstructor
public class OpeningTrainerController {

    @NonNull
    private final LibridgeServer libridgeServer;
    @NonNull
    private final BoardRepository boardRepository;
    @NonNull
    private final BoardFactory boardFactory;

    @NonNull
    private final OpeningTrainerService openingTrainerService;

    @GetMapping("/{boardId}/{direction}")
    public ExpectedCallDTO getExpectedCall(@PathVariable UUID boardId, @PathVariable Direction direction) {
        LOGGER.trace("getExpectedCall");
        Optional<BoardEntity> boardEntity = boardRepository.findById(boardId);
        if (boardEntity.isPresent()) {
            return new ExpectedCallDTO(boardId, libridgeServer.getExpectedCall(boardFactory.fromEntity(boardEntity.get())));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no board in the database with this ID");
        }
    }

    @GetMapping("/getRandom")
    public HandWithCandidateBidsDTO getRandom() {
        LOGGER.trace("openingTrainer_getRandom");
        return this.openingTrainerService.getRandomHandWithCandidateBidsDTO();
    }

}
