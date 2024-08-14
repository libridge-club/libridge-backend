package club.libridge.libridgebackend.core;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import lombok.NonNull;
import scalabridge.Card;

/**
 * This class centralizes the access to Random. This decouples other classes from Random but most importantly increases the quality and efficiency of
 * RNG.
 */
public class RandomUtils {
    private static final SecureRandom RANDOM = new SecureRandom();

    public int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    public long nextLong(long bound) {
        return RANDOM.nextLong(bound);
    }

    public void shuffle(@NonNull List<Card> listOfCards) {
        Collections.shuffle(listOfCards, RANDOM);
    }

    public void shuffleWithSeed(@NonNull List<Card> listOfCards, long seed) {
        Random seededRandom = new Random(seed);
        Collections.shuffle(listOfCards, seededRandom);
    }

    public Card pickOneRandomCard(@NonNull Hand hand) {
        ArrayList<Card> cards = new ArrayList<Card>(hand.getCards());
        int randomIndex = this.nextInt(cards.size());
        return cards.get(randomIndex);
    }

}
