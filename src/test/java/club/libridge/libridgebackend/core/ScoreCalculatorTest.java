package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import scalabridge.OddTricks;
import scalabridge.Strain;

class ScoreCalculatorTest {

    /**
     * This is governed by LAW 77 - DUPLICATE BRIDGE SCORING TABLE
     */

    private Contract getContract(String text, boolean vulnerable) {
        return ContractFactory.fromText(text, vulnerable);
    }

    @Test
    void oneNoTrumpMade() {
        Contract oneNoTrump = getContract("1N", false);
        assertEquals(90, ScoreCalculator.score(oneNoTrump, 7));
    }

    @Test
    void threeNoTrumpsMade() {
        Contract threeNoTrumpsNonVul = getContract("3N", false);
        Contract threeNoTrumpsVul = getContract("3N", true);
        assertEquals(400, ScoreCalculator.score(threeNoTrumpsNonVul, 9));
        assertEquals(600, ScoreCalculator.score(threeNoTrumpsVul, 9));
    }

    @Test
    void sevenNoTrumpsRedoubledMade() {
        int tricksMade = 13;
        Contract sevenNoTrumpsRedoubledNonVul = getContract("7NXX", false);
        Contract sevenNoTrumpsRedoubledVul = getContract("7NXX", true);
        assertEquals(2280, ScoreCalculator.score(sevenNoTrumpsRedoubledNonVul, tricksMade));
        assertEquals(2980, ScoreCalculator.score(sevenNoTrumpsRedoubledVul, tricksMade));
    }

    @Test
    void someArbitraryContracts() {
        boolean vul = true;
        boolean nonVul = false;

        assertEquals(70, ScoreCalculator.score(getContract("1C", vul), 7));
        assertEquals(80, ScoreCalculator.score(getContract("1H", vul), 7));
        assertEquals(90, ScoreCalculator.score(getContract("1C", vul), 8));
        assertEquals(90, ScoreCalculator.score(getContract("1N", vul), 7));
        assertEquals(110, ScoreCalculator.score(getContract("1H", vul), 8));
        assertEquals(120, ScoreCalculator.score(getContract("1N", vul), 8));
        assertEquals(180, ScoreCalculator.score(getContract("2DX", vul), 8));
        assertEquals(180, ScoreCalculator.score(getContract("1NX", vul), 7));
        assertEquals(230, ScoreCalculator.score(getContract("3S", nonVul), 12));
        assertEquals(380, ScoreCalculator.score(getContract("2DX", vul), 9));
        assertEquals(420, ScoreCalculator.score(getContract("5D", nonVul), 12));
        assertEquals(620, ScoreCalculator.score(getContract("5D", vul), 12));
        assertEquals(670, ScoreCalculator.score(getContract("3DX", vul), 9));
        assertEquals(750, ScoreCalculator.score(getContract("3NX", nonVul), 11));
        assertEquals(760, ScoreCalculator.score(getContract("2DXX", vul), 8));
        assertEquals(870, ScoreCalculator.score(getContract("3DX", vul), 10));
        assertEquals(980, ScoreCalculator.score(getContract("6H", nonVul), 12));
        assertEquals(1160, ScoreCalculator.score(getContract("2DXX", vul), 9));
        assertEquals(1330, ScoreCalculator.score(getContract("3HX", vul), 12));
        assertEquals(1370, ScoreCalculator.score(getContract("6C", vul), 12));
        assertEquals(1430, ScoreCalculator.score(getContract("6H", vul), 12));
    }

    @Test
    void allDownResults() {
        OddTricks sevenOddTricks = OddTricks.SEVEN;
        int totalTricks = 13;
        Strain anyStrain = Strain.NOTRUMPS;
        boolean vulnerable = true;
        // Trusting completely in http://rpbridge.net/cgi-bin/xsc2.pl
        int[][] values = { new int[] { -50, -100, -200, -100, -200, -400 }, // -1
                new int[] { -100, -300, -600, -200, -500, -1000 }, // -2
                new int[] { -150, -500, -1000, -300, -800, -1600 }, // -3
                new int[] { -200, -800, -1600, -400, -1100, -2200 }, // -4
                new int[] { -250, -1100, -2200, -500, -1400, -2800 }, // -5
                new int[] { -300, -1400, -2800, -600, -1700, -3400 }, // -6
                new int[] { -350, -1700, -3400, -700, -2000, -4000 }, // -7
                new int[] { -400, -2000, -4000, -800, -2300, -4600 }, // -8
                new int[] { -450, -2300, -4600, -900, -2600, -5200 }, // -9
                new int[] { -500, -2600, -5200, -1000, -2900, -5800 }, // -10
                new int[] { -550, -2900, -5800, -1100, -3200, -6400 }, // -11
                new int[] { -600, -3200, -6400, -1200, -3500, -7000 }, // -12
                new int[] { -650, -3500, -7000, -1300, -3800, -7600 } // -13
        };

        Contract nonVul;
        Contract nonVulDoubled;
        Contract nonVulRedoubled;
        Contract vul;
        Contract vulDoubled;
        Contract vulRedoubled;

        for (int i = 1; i <= totalTricks; i++) {
            int downI = totalTricks - i;
            int[] current = values[i - 1];
            nonVul = new Contract(sevenOddTricks, anyStrain, PenaltyStatus.NONE, !vulnerable);
            nonVulDoubled = new Contract(sevenOddTricks, anyStrain, PenaltyStatus.DOUBLED, !vulnerable);
            nonVulRedoubled = new Contract(sevenOddTricks, anyStrain, PenaltyStatus.REDOUBLED, !vulnerable);
            vul = new Contract(sevenOddTricks, anyStrain, PenaltyStatus.NONE, vulnerable);
            vulDoubled = new Contract(sevenOddTricks, anyStrain, PenaltyStatus.DOUBLED, vulnerable);
            vulRedoubled = new Contract(sevenOddTricks, anyStrain, PenaltyStatus.REDOUBLED, vulnerable);
            assertEquals(current[0], ScoreCalculator.score(nonVul, downI));
            assertEquals(current[1], ScoreCalculator.score(nonVulDoubled, downI));
            assertEquals(current[2], ScoreCalculator.score(nonVulRedoubled, downI));
            assertEquals(current[3], ScoreCalculator.score(vul, downI));
            assertEquals(current[4], ScoreCalculator.score(vulDoubled, downI));
            assertEquals(current[5], ScoreCalculator.score(vulRedoubled, downI));
        }
    }

}
