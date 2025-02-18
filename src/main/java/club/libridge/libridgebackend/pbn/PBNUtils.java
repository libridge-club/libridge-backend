package club.libridge.libridgebackend.pbn;

import java.util.Arrays;
import java.util.List;

import club.libridge.libridgebackend.core.exceptions.MalformedLinMDValueException;
import scalabridge.CompleteDeckInFourHands;
import scalabridge.Direction;
import scalabridge.Hand;
import scalabridge.PositiveInteger;
import scalabridge.Suit;
import scalabridge.nonpure.DuplicateBoardBuilder;

public final class PBNUtils {

    /**
     * 69 in fact: 52 cards + 3*4 = 12 dots, 3 spaces and a "X:" in the start
     */
    private static final int MAX_CHARS_IN_DEAL_TAG = 70;
    private static final Direction NORTH = Direction.getNorth();
    private static final int DEFAULT_DUPLICATE_BOARD_NUMBER = 1;

    private PBNUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     *
     * Implemented from PBN Standard 2.1 - Defined at section 3.4.11 The Deal tag
     */
    public static String dealTagStringFromBoard(CompleteDeckInFourHands board) {
        return dealTagStringFromBoardAndDirection(board, NORTH);
    }

    /**
     *
     * Implemented from PBN Standard 2.1 - Defined at section 3.4.11 The Deal tag
     */
    public static String dealTagStringFromBoardAndDirection(CompleteDeckInFourHands board, Direction firstDirection) {
        StringBuilder returnValue = new StringBuilder(MAX_CHARS_IN_DEAL_TAG);

        for (int i = 0; i < 4; i++) {
            Direction current = firstDirection.next(new PositiveInteger(i));
            Hand currentHand = board.getHandOf(current).hand();
            if (i == 0) {
                returnValue.append(firstDirection.getAbbreviation() + ":");
            } else {
                returnValue.append(" ");
            }
            returnValue.append(dealTagPartialStringFromHand(currentHand));
        }
        return returnValue.toString();
    }

    /**
     *
     * Implemented from PBN Standard 2.1 - Defined at section 3.4.11 The Deal tag
     */
    private static String dealTagPartialStringFromHand(Hand hand) {
        return hand.toString();
    }

    /**
     * Example "E:86.KT2.K85.Q9742 KJT932.97.942.86 54.8653.AQJT73.3 AQ7.AQJ4.6.AKJT5"
     */
    // FIXME this should create a DuplicateBoard
    public static CompleteDeckInFourHands getBoardFromDealTag(String dealTag) {
        return DuplicateBoardBuilder.build(DEFAULT_DUPLICATE_BOARD_NUMBER, dealTag).hands();
    }

    /**
     * Example: "3SAJHKT63DAKT7C652,SK653HA972DJ8CQ87,S972HQ854DQ954C94,SQT84HJD632CAKJT3"
     *
     * Format: <first_hand_indicator><hand1>,<hand2>,<hand3>,<hand4>
     *
     * first_hand_indicator is a number from 1 to 4 (inclusive) and represents the position of the first hand. 1 for the dealer as first hand 2 for
     * the player before the dealer as first hand etc.
     *
     * each of the hands have the format: <suit_holding_1><suit_holding_2><suit_holding_3><suit_holding_4> (in any order, but usually in the order
     * SHDC) where each suit holding is represented by its suit abbreviation [SHDC] and then the abbreviations of the ranks
     *
     * The last hand is optional on BBO, but will be obligatory here.
     */

    public static String getDealTagStringFromLinMD(String linMD, Direction dealer) {
        StringBuilder returnValue = new StringBuilder(MAX_CHARS_IN_DEAL_TAG);
        List<Suit> suitOrder = Arrays.asList(Suit.getSPADES(), Suit.getHEARTS(), Suit.getDIAMONDS(), Suit.getCLUBS());
        int magicNumberFirstHandIndicator = 5;
        try {
            int firstHandIndicator = Integer.parseInt(linMD.substring(0, 1));
            int index = magicNumberFirstHandIndicator - firstHandIndicator;
            if (index < 0) {
                throw new IllegalArgumentException("index must be non-negative");
            }
            Direction firstDirection = dealer.next(new PositiveInteger(index));
            returnValue.append(firstDirection.getAbbreviation());
            returnValue.append(":");
            String[] hands = linMD.substring(1).split(",");
            Boolean firstHand = true;
            for (String hand : hands) {
                if (firstHand) {
                    firstHand = false;
                } else {
                    returnValue.append(" ");
                }
                Boolean firstSuit = true;
                for (Suit suit : suitOrder) {
                    if (firstSuit) {
                        firstSuit = false;
                    } else {
                        returnValue.append(".");
                    }
                    String asfd = getSuitHolding(hand, suit);
                    returnValue.append(asfd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MalformedLinMDValueException(e.getMessage());
        }
        return returnValue.toString();

    }

    private static String getSuitHolding(String hand, Suit suit) {
        String handLower = hand.toLowerCase();
        StringBuilder suitHolding = new StringBuilder();
        int initialIndex = handLower.indexOf(suit.getSymbol()) + 1;
        for (int i = initialIndex; i < handLower.length(); i++) {
            char currentChar = handLower.charAt(i);
            if ('s' == currentChar || 'h' == currentChar || 'd' == currentChar || 'c' == currentChar) {
                break;
            }
            suitHolding.append(handLower.charAt(i));
        }
        return suitHolding.toString();
    }

}
