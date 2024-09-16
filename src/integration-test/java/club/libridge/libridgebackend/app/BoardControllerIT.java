package club.libridge.libridgebackend.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import club.libridge.libridgebackend.app.controller.BoardController;
import club.libridge.libridgebackend.app.persistence.BoardEntity;
import club.libridge.libridgebackend.app.persistence.BoardRepository;
import club.libridge.libridgebackend.core.Board;
import club.libridge.libridgebackend.dto.BoardDTO;

@SpringBootTest()
@ActiveProfiles("development")
public class BoardControllerIT {

    @Autowired
    private BoardController controller;

    @Autowired
    private BoardRepository repository;

    @Test
    public void GetRandomBoard_ShouldThrowNotFoundIfThereIsNoBoard() {
        repository.deleteAll();

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            controller.getRandomBoard();
        });

        assertEquals(HttpStatus.NOT_FOUND, responseStatusException.getStatusCode());
    }

    @Test
    public void GetRandomBoard_ShouldReturnABoardIfThereIsABoard() {
        controller.createRandomBoard();
        assertTrue(repository.count() > 0);

        BoardDTO randomBoard = controller.getRandomBoard();

        assertNotNull(randomBoard.getBoard());
    }

    @Test
    public void GetByPavlicekNumber_ShouldThrowNotFoundIfThereIsNoBoard() {
        repository.deleteAll();

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> {
            controller.getByPavlicekNumber("0");
        });

        assertEquals(HttpStatus.NOT_FOUND, responseStatusException.getStatusCode());
    }

    @Test
    public void GetByPavlicekNumber_ShouldReturnABoardIfThereIsABoard() {
        BoardDTO createdBoard = controller.createRandomBoard();
        String pavlicekNumber = createdBoard.getPavlicekNumber();

        BoardDTO byPavlicekNumber = controller.getByPavlicekNumber(pavlicekNumber);

        Board board1 = createdBoard.getBoard();
        Board board2 = byPavlicekNumber.getBoard();
        board1.equals(board2);

        assertEquals(createdBoard.getBoard(), byPavlicekNumber.getBoard());
    }

    @Test
    public void createRandomBoard_ShouldIncreaseTheNumberOfBoardsByOne() {
        long before = repository.count();

        controller.createRandomBoard();

        long after = repository.count();
        assertEquals(1, after - before);
    }

    @Test
    public void createRandomBoard_ShouldReturnABoard() {
        BoardDTO randomBoard = controller.createRandomBoard();

        assertNotNull(randomBoard.getBoard());
    }

    // @Test // Commenting this test for now
    public void magicNumberURL_ShouldCreateBoardsAndDoubleDummyTablesInLessThanFiveSeconds() {
        Instant start, finish;
        String includedBoard = "32950149871269851215330677922";
        int numberOfCreatedHands = 1000;
        int[] correctDoubleDummyTableList = { 8, 5, 8, 5, 9, 4, 9, 4, 6, 7, 6, 6, 7, 6, 6, 5, 8, 5, 7, 5 };
        long fiveSecondsInNanoseconds = 5000000000L;
        repository.deleteAll();

        start = Instant.now();
        controller.magicNumberCreateTablesFromFile();
        finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toNanos();

        List<BoardEntity> list = repository.findAll(Example.of(new BoardEntity(includedBoard)));
        assertFalse(list.isEmpty());
        List<Integer> ddsIntegerList = list.get(0).getDoubleDummyTableEntity().getDoubleDummyTable().toDDSIntegerList();
        for (int i = 0; i < correctDoubleDummyTableList.length; i++) {
            assertTrue(correctDoubleDummyTableList[i] == ddsIntegerList.get(i));
        }
        assertTrue(timeElapsed < fiveSecondsInNanoseconds);
        assertEquals(numberOfCreatedHands, repository.count());
    }

}
