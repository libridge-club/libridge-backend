package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import scalabridge.OddTricks;
import scalabridge.Strain;

public class ContractTest {

    private static int anyLevel = 3;
    private static OddTricks anyOddTricks = OddTricks.fromLevel(anyLevel);
    private static Strain anyStrain = Strain.DIAMONDS;
    private static PenaltyStatus noPenalty = PenaltyStatus.NONE;
    private static boolean vulnerable = false;

    private Contract getSampleContract() {
        return new Contract(anyOddTricks, anyStrain, noPenalty, vulnerable);
    }

    @Test
    @SuppressWarnings("null")
    public void constructor_shouldThrowInvalidArgumentExceptionWithNullParams() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Contract(null, anyStrain, noPenalty, vulnerable);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Contract(anyOddTricks, null, noPenalty, vulnerable);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Contract(anyOddTricks, anyStrain, null, vulnerable);
        });
    }

    @Test
    public void shouldGetItsLevel() {
        Contract subject = this.getSampleContract();
        assertEquals(anyLevel, subject.getLevel());
    }

    @Test
    public void shouldGetItsStrain() {
        Contract subject = this.getSampleContract();
        assertEquals(anyStrain, subject.getStrain());
    }

    @Test
    public void shouldGetItsDoubled() {
        Contract subject = new Contract(anyOddTricks, anyStrain, PenaltyStatus.DOUBLED, vulnerable);
        assertTrue(subject.isDoubled());
        assertFalse(subject.isRedoubled());
    }

    @Test
    public void shouldGetItsRedoubled() {
        Contract subject = new Contract(anyOddTricks, anyStrain, PenaltyStatus.REDOUBLED, vulnerable);
        assertFalse(subject.isDoubled());
        assertTrue(subject.isRedoubled());
    }

}
