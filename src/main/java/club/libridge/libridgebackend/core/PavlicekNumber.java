package club.libridge.libridgebackend.core;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.Rank;
import scalabridge.Suit;

/*
 * This is an implementation of http://www.rpbridge.net/7z68.htm
 * It is a mapping between boards and numbers from 0 to N-1
 * where N = the number of different bridge deals or Boards, as modelled here.
 * N is also equal to 52! / (13! ^ 4) or 53644737765488792839237440000
 * which is between 95 and 96 bits ( i.e. log2(N)~=95.4 )
 * * One important difference is the suit order,
 * in which clubs and diamonds are swapped.
 *
 * Another implementation of the same algorithm:
 * https://bridge.thomasoandrews.com/bridge/impossible/
 * This one differs from mine in the minor suits. One of them is wrong.
 * I don't know which one.
 *
 * For now, the important part is that f(f'(x)) = x and this works for the current
 * purpose. In the future, it could be refactored and easily recalculated for every
 * saved board.
 */

@Component
public class PavlicekNumber {
    static BigInteger bigD;

    static Deque<Card> getReferenceDeque() {
        int totalNumberOfCards = 52;
        Deque<Card> deck = new ArrayDeque<Card>(totalNumberOfCards);
        List<Suit> suitOrder = Arrays.asList(Suit.getDIAMONDS(), Suit.getCLUBS(), Suit.getHEARTS(), Suit.getSPADES());
        for (Suit suit : suitOrder) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        return deck;
    }

    static {
        BigInteger nominator = BigInteger.ONE;
        for (long i = 1; i <= 52; i++) {
            nominator = nominator.multiply(BigInteger.valueOf(i));
        }

        BigInteger denominator = BigInteger.ONE;
        for (long i = 1; i <= 13; i++) {
            denominator = denominator.multiply(BigInteger.valueOf(i));
        }
        denominator = denominator.pow(4);

        bigD = nominator.divide(denominator);
    }

    public static void main(String[] args) {
        // Deque<Card> completeDeque = getReferenceDeque();
        // ShuffledBoardDealer dealer = new ShuffledBoardDealer();
        // Board board = dealer.dealBoard(Direction.getNorth(), completeDeque);
        PavlicekNumber pavlicekNumber = new PavlicekNumber();
        // printBoard(board);
        // BigInteger derivedNumber = pavlicekNumber.getNumberFromBoard(board);
        // System.out.println(derivedNumber);
        String numberString = "25119193013129491541542678034";
        BigInteger bigInt = new BigInteger(numberString);
        Board derivedBoard = pavlicekNumber.getBoardFromNumber(bigInt);
        // printBoard(derivedBoard);
        derivedBoard = pavlicekNumber.getBoardFromNumber(new BigInteger("25119193013129491541542678034"));
        printBoard(derivedBoard);
    }

    public BigInteger getNumberFromBoard(Board board) {
        /*
         * This will be used as the ordering reference or the implementation
         */
        Deque<Card> completeDeque = getReferenceDeque();
        long north = 13;
        long east = 13;
        long south = 13;
        long west = 13;
        BigInteger k = clone(bigD);
        BigInteger i = BigInteger.ZERO;
        BigInteger x = BigInteger.ZERO;
        Map<Integer, Direction> map = preProcess(board);
        for (long cards = 52; cards > 0; k = clone(x), cards--) {
            Card card = completeDeque.pollLast();
            Direction directionFromCard = (Direction) map.get(card.hashCode());

            x = getNewX(k, north, cards);
            if (Direction.getNorth().equals(directionFromCard)) {
                north--;
                continue;
            }

            i = i.add(x);
            x = getNewX(k, east, cards);
            if (Direction.getEast().equals(directionFromCard)) {
                east--;
                continue;
            }

            i = i.add(x);
            x = getNewX(k, south, cards);
            if (Direction.getSouth().equals(directionFromCard)) {
                south--;
                continue;
            }

            i = i.add(x);
            x = getNewX(k, west, cards);
            west--;
        }
        return i;
    }

    public Board getBoardFromNumber(BigInteger i) {
        if (bigD.compareTo(i) <= 0 || BigInteger.ZERO.compareTo(i) > 0) {
            throw new IllegalArgumentException("Number must be between 0 and 53644737765488792839237440000 - 1");
        }
        /*
         * This will be used as the ordering reference or the implementation
         */
        Deque<Card> completeDeque = getReferenceDeque();
        long north = 13;
        long east = 13;
        long south = 13;
        long west = 13;
        BigInteger k = clone(bigD);
        BigInteger x = BigInteger.ZERO;
        Map<Direction, Hand> map = new HashMap<Direction, Hand>();
        for (Direction direction : Direction.values()) {
            map.put(direction, new Hand());
        }
        for (long cards = 52; cards > 0; k = clone(x), cards--) {
            Card card = completeDeque.pollLast();

            x = getNewX(k, north, cards);
            if (i.compareTo(x) < 0) {
                map.get(Direction.getNorth()).addCard(card);
                north--;
                continue;
            }

            i = i.subtract(x);
            x = getNewX(k, east, cards);
            if (i.compareTo(x) < 0) {
                map.get(Direction.getEast()).addCard(card);
                east--;
                continue;
            }

            i = i.subtract(x);
            x = getNewX(k, south, cards);
            if (i.compareTo(x) < 0) {
                map.get(Direction.getSouth()).addCard(card);
                south--;
                continue;
            }

            i = i.subtract(x);
            map.get(Direction.getWest()).addCard(card);
            x = getNewX(k, west, cards);
            west--;
        }
        return new Board(map, Direction.getNorth());
    }

    private Map<Integer, Direction> preProcess(Board board) {
        Map<Integer, Direction> map = new HashMap<Integer, Direction>();
        for (Direction direction : Direction.values()) {
            Collection<Card> cards = board.getHandOf(direction).getCards();
            for (Card card : cards) {
                map.put(card.hashCode(), direction);
            }
        }
        return map;
    }

    private BigInteger getNewX(BigInteger k, long direction, long cards) {
        return k.multiply(BigInteger.valueOf(direction)).divide(BigInteger.valueOf(cards));
    }

    private BigInteger clone(BigInteger x) {
        return x.add(BigInteger.ZERO);
    }

    private static void printBoard(Board board) {
        for (Direction direction : Direction.values()) {
            System.out.println(board.getHandOf(direction));
        }
    }

}
