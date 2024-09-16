package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import club.libridge.libridgebackend.core.boarddealer.BoardDealer;
import club.libridge.libridgebackend.core.boarddealer.CardDeck;
import club.libridge.libridgebackend.core.boarddealer.Complete52CardDeck;
import club.libridge.libridgebackend.core.boarddealer.ShuffledBoardDealer;
import club.libridge.libridgebackend.pbn.PBNUtils;
import scalabridge.Direction;

public class PavlicekNumberTest {

    private PavlicekNumber subject;

    @BeforeEach
    public void setup() {
        subject = new PavlicekNumber();
    }

    @Test
    /**
     * This test is random. Lets hope it does not fail.
     */
    public void bothFunctionsShouldBeInverseOfEachOther() {
        int maxNumberOfBits = 95;
        BigInteger randomNumber = new BigInteger(maxNumberOfBits, new SecureRandom());
        assertEquals(randomNumber, subject.getNumberFromBoard(subject.getBoardFromNumber(randomNumber)));
    }

    @Test
    /**
     * This test is random. Lets hope it does not fail.
     */
    public void bothFunctionsShouldBeInverseOfEachOtherTwo() {
        BoardDealer boardDealer = new ShuffledBoardDealer();
        CardDeck anyCardDeck = new Complete52CardDeck();
        Board randomBoard = boardDealer.dealBoard(Direction.getNorth(), anyCardDeck.getDeck());
        assertEquals(randomBoard, subject.getBoardFromNumber(subject.getNumberFromBoard(randomBoard)));
    }

    @Test
    public void shouldWorkForSomeArbitraryNumbers() {
        Map<String, String> test1 = Map.of("N:86.KT2.K85.Q9742 KJT932.97.942.86 54.8653.AQJT73.3 AQ7.AQJ4.6.AKJT5", "46537212628585927172034960904",
                "N:AKQJT98765432... .AKQJT98765432.. ..AKQJT9876543.A ..2.KQJT98765432", "1",
                "N:K2.3.AKJ863.AQ82 8543.T764.Q4.T53 JT7.KQ952.T9.K64 AQ96.AJ8.752.J97", "43411288333940402635407348608",
                "N:K432.A964.QJT73. J975.853.A865.A9 AQ.KQJ7.42.KJ764 T86.T2.K9.QT8532", "28919292405012072303933333048",
                "N:7632.JT8752.6.AQ 95.AQ9.KQJT4.962 AKJ84.63.72.KJ53 QT.K4.A9853.T874", "36587790094099656990585989598");
        for (Map.Entry<String, String> entry : test1.entrySet()) {
            String expectedDealTag = entry.getKey();
            BigInteger expectedNumber = new BigInteger(entry.getValue());
            String actualDealTag = PBNUtils.dealTagStringFromBoard(subject.getBoardFromNumber(expectedNumber));
            BigInteger actualNumber = subject.getNumberFromBoard(PBNUtils.getBoardFromDealTag(expectedDealTag));

            assertEquals(expectedNumber, actualNumber);
            assertEquals(expectedDealTag, actualDealTag);
        }
    }
}
