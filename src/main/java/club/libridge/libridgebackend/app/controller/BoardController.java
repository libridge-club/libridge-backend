package club.libridge.libridgebackend.app.controller;

import static club.libridge.libridgebackend.logging.LibridgeLogger.LOGGER;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import club.libridge.libridgebackend.app.persistence.BoardRepository;
import club.libridge.libridgebackend.dto.BoardDTO;
import club.libridge.libridgebackend.networking.server.LibridgeServer;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@RestController
@RequestMapping("/boards")
@AllArgsConstructor
public class BoardController {

    @NonNull
    private final LibridgeServer libridgeServer;
    @NonNull
    private final BoardRepository repository;

    @GetMapping("/getRandom")
    public BoardDTO getRandomBoard() {
        LOGGER.trace("getRandomBoard");
        Optional<BoardDTO> randomBoard = this.libridgeServer.getRandomBoard(repository);
        if (randomBoard.isPresent()) {
            return randomBoard.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no board in the database");
        }
    }

    @GetMapping("/getByPavlicekNumber/{pavlicekNumber}")
    public BoardDTO getByPavlicekNumber(@PathVariable String pavlicekNumber) {
        LOGGER.trace("getByPavlicekNumber");
        Optional<BoardDTO> boardDTO = this.libridgeServer.getBoardByPavlicekNumber(repository, pavlicekNumber);
        if (boardDTO.isPresent()) {
            return boardDTO.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no board with this pavlicekNumber");
        }
    }

    @GetMapping("/createRandom")
    public BoardDTO createRandomBoard() {
        LOGGER.trace("createRandomBoard");
        return this.libridgeServer.createRandomBoard(repository);
    }

    @GetMapping("/437c542d77291aaff96d6b223b798628") // This should be a PUT, but a GET is easier to trigger with a browser request.
    @PutMapping("/437c542d77291aaff96d6b223b798628")
    public void magicNumberCreateTablesFromFile() {
        LOGGER.trace("magicNumberCreateTablesFromFile");
        String existingBoard = "32950149871269851215330677922";
        Optional<BoardDTO> boardDTO = this.libridgeServer.getBoardByPavlicekNumber(repository, existingBoard);
        if (boardDTO.isEmpty()) {
            this.libridgeServer.magicNumberCreateTablesFromFile(repository);
        }
    }

}
