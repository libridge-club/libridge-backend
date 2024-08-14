package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import scalabridge.Strain;

public class StrainTest {

    private static Strain diamonds;
    private static Strain clubs;
    private static Strain hearts;
    private static Strain spades;
    private static Strain noTrumps;

    @BeforeAll
    public static void setup() {
        diamonds = Strain.DIAMONDS;
        clubs = Strain.CLUBS;
        hearts = Strain.HEARTS;
        spades = Strain.SPADES;
        noTrumps = Strain.NOTRUMPS;
    }

    @Test
    public void theSameStrainShouldAlwaysBeTheSameObject() {
        Strain strain1 = Strain.CLUBS;
        Strain strain2 = Strain.CLUBS;
        assertTrue(strain1 == strain2);
    }

    @Test
    public void theSameStrainShouldAlwaysBeEqual() {
        Strain strain1 = Strain.CLUBS;
        Strain strain2 = Strain.CLUBS;
        assertEquals(strain1, strain2);
    }

    @Test
    public void shouldGetName() {
        assertEquals("Diamonds", diamonds.getName());
        assertEquals("Clubs", clubs.getName());
        assertEquals("Hearts", hearts.getName());
        assertEquals("Spades", spades.getName());
        assertEquals("No Trumps", noTrumps.getName());
    }

    @Test
    public void shouldGetSymbol() {
        assertEquals("D", diamonds.getSymbol());
        assertEquals("C", clubs.getSymbol());
        assertEquals("H", hearts.getSymbol());
        assertEquals("S", spades.getSymbol());
        assertEquals("N", noTrumps.getSymbol());
    }

}
