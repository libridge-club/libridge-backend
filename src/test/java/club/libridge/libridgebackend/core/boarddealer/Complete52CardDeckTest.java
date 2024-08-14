package club.libridge.libridgebackend.core.boarddealer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import scalabridge.Card;

public class Complete52CardDeckTest {

    private Complete52CardDeck subject;

    @BeforeEach
    public void setup() {
        this.subject = new Complete52CardDeck();
    }

    @Test
    public void getDeckshouldReturnADeckWith52DifferentCards() {
        Set<Card> set = new HashSet<Card>(subject.getDeck());
        assertEquals(Complete52CardDeck.TOTAL_NUMBER_OF_CARDS, set.size());
    }

}
