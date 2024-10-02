package club.libridge.libridgebackend.core.openingtrainer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import club.libridge.libridgebackend.app.persistence.BoardFactory;
import club.libridge.libridgebackend.core.PavlicekNumber;
import scalabridge.Call;
import scalabridge.Direction;
import scalabridge.Hand;
import scalabridge.pbn.PBNUtils;

public class OpeningSystemTest {

    private static BoardFactory boardFactory;
    private static OpeningSystem subject;

    @BeforeAll
    public static void setup() {
        boardFactory = new BoardFactory(new PavlicekNumber());
        subject = new OpeningSystem();
    }

    @Test
    public void getCall_shouldReturnTheRightCall() {
        String pbnString = "972.4.986.AQT842";
        Hand hand = PBNUtils.handFromPartialDealTag(pbnString).get();
        Call call = subject.getCall(boardFactory.fromHandAndDirection(hand, Direction.getNorth()));
        assertTrue(call.isPass());
    }
}
