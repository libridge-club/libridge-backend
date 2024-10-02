package club.libridge.libridgebackend.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import club.libridge.libridgebackend.dto.BoardDTO;
import scalabridge.GameConstants;

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

        assertEquals(GameConstants.NUMBER_OF_HANDS(), randomBoard.getHands().size());
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

        assertEquals(createdBoard.getPbnDealTag(), byPavlicekNumber.getPbnDealTag());
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

        assertEquals(GameConstants.NUMBER_OF_HANDS(), randomBoard.getHands().size());
    }

    @Test
    public void magicNumberURL_ShouldRunInLessThanFiveSeconds() {
        /**
         * This pavlicek number and double dummy table refers to this specific hand of the hands-with-table.txt file:
         * PBN 0 2 0 3 "N:KT.6.AKQ64.A7654 Q53.KT9874.T2.Q2 AJ876.A2.953.KJT 942.QJ53.J87.983"
         * TABLE 13 0 13 0 8 5 8 5 13 0 13 0 13 0 13 0 13 0 13 0
         */
        String includedBoard = "28127388640394793836568292831";
        int[] correctDoubleDummyTableList = { 13, 0, 13, 0, 8, 5, 8, 5, 13, 0, 13, 0, 13, 0, 13, 0, 13, 0, 13, 0 };

        Instant start, finish;
        int numberOfCreatedHands = 1000;
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
