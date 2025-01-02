package club.libridge.libridgebackend.lin;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import club.libridge.libridgebackend.pbn.PBNUtils;
import scalabridge.Auction;
import scalabridge.Call;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;
import scalabridge.OpenDeal;
import scalabridge.PositiveInteger;
import scalabridge.events.CallEvent;
import scalabridge.events.PlayCardEvent;
import scalabridge.nonpure.DuplicateBoardBuilder;

/**
 * You can use https://www.bridgebase.com/tools/handviewer.html?lin=<lin> to see what bridge base expects from a LIN.
 */

public class ParsedLin {

    private static final Map<LinKey, List<Integer>> SYMBOL_TO_LIST_OF_INDEXES;
    private static String anyDealTag = "N:86.KT2.K85.Q9742 KJT932.97.942.86 54.8653.AQJT73.3 AQ7.AQJ4.6.AKJT5";
    static {
        SYMBOL_TO_LIST_OF_INDEXES = new EnumMap<LinKey, List<Integer>>(LinKey.class);
        for (LinKey key : LinKey.values()) {
            SYMBOL_TO_LIST_OF_INDEXES.put(key, new ArrayList<Integer>());
        }
    }

    private final ArrayList<LinKeyValuePair> list;
    private List<Auction> auctions;

    public ParsedLin(List<LinKeyValuePair> list) {
        this.list = new ArrayList<>();
        int i = 0;
        for (LinKeyValuePair linKeyValuePair : list) {
            SYMBOL_TO_LIST_OF_INDEXES.get(linKeyValuePair.getKey()).add(i);
            this.list.add(linKeyValuePair);
            i++;
        }

        auctions = null;
    }

    @SuppressWarnings("null") // We will run this risk for now.
    public List<Auction> getAuctions() {
        if (this.auctions != null) {
            return Collections.unmodifiableList(auctions);
        } else {
            List<Auction> auctionList = new ArrayList<Auction>();
            List<Integer> qxIndexes = SYMBOL_TO_LIST_OF_INDEXES.get(LinKey.QX);
            if (!qxIndexes.isEmpty()) {
                int firstQxIndex = qxIndexes.get(0);
                Auction currentAuction = null;
                boolean firstBoard = true;
                Direction currentDirection = Direction.getNorth();
                for (int currentIndex = firstQxIndex; currentIndex < list.size(); currentIndex++) {
                    LinKey key = list.get(currentIndex).getKey();
                    String value = list.get(currentIndex).getValue();
                    if (key.equals(LinKey.QX)) {
                        if (firstBoard) {
                            firstBoard = false;
                        } else { // Board is finished. Add auction to list
                            auctionList.add(currentAuction);
                        }
                        // Then start the new board
                        Integer boardNumber = Integer.parseInt(value.substring(1));
                        DuplicateBoard duplicateBoard = DuplicateBoardBuilder.build(boardNumber, anyDealTag);
                        Direction dealer = duplicateBoard.getDealer();
                        currentAuction = new Auction(dealer);
                        currentDirection = dealer;
                    } else if (key.equals(LinKey.MB)) { // For every bid
                        Call currentCall = LinParser.parseFromLinMB(value);
                        currentAuction = currentAuction.makeCall(currentDirection, currentCall).get(); // FIXME need to test this
                        currentDirection = currentDirection.next();
                    }
                }
                auctionList.add(currentAuction); // Finish last auction.
            }
            this.auctions = auctionList;
            return this.getAuctions(); // Guaranteeing memoization
        }
    }

    public List<OpenDeal> getDeals() {
        List<OpenDeal> dealList = new ArrayList<OpenDeal>();
        List<Integer> qxIndexes = SYMBOL_TO_LIST_OF_INDEXES.get(LinKey.QX);
        if (!qxIndexes.isEmpty()) {
            int firstQxIndex = qxIndexes.get(0);
            Direction currentDealer = null;
            Direction currentDirectionToMakeCall = null;
            Direction currentDirectionToPlayCard = null;
            Integer boardNumber = null;
            OpenDeal currentDeal = null;
            boolean firstBoard = true;
            for (int currentIndex = firstQxIndex; currentIndex < list.size(); currentIndex++) {
                LinKey key = list.get(currentIndex).getKey();
                String value = list.get(currentIndex).getValue();
                if (key.equals(LinKey.QX)) {
                    if (firstBoard) {
                        firstBoard = false;
                    } else { // Board is finished. Add deal to list
                        dealList.add(currentDeal);
                    }
                    // Then start the new board
                    boardNumber = Integer.parseInt(value.substring(1));
                    currentDealer = Direction.getWest().next(new PositiveInteger(boardNumber));
                    currentDirectionToMakeCall = currentDealer;
                    currentDirectionToPlayCard = null;
                } else if (key.equals(LinKey.MD)) { // Hands definition
                    String dealTagString = PBNUtils.getDealTagStringFromLinMD(value, currentDealer);
                    currentDeal = OpenDeal.empty(DuplicateBoardBuilder.build(boardNumber, dealTagString));
                } else if (key.equals(LinKey.MB)) { // For every call
                    currentDeal = currentDeal.addEvent(new CallEvent(Instant.now(), currentDirectionToMakeCall, LinParser.parseFromLinMB(value)));
                    currentDirectionToMakeCall = currentDirectionToMakeCall.next();
                } else if (key.equals(LinKey.PC)) { // For every card played
                    // FIXME Play card direction is wrong for now.
                    // It needs to check who is declarer for first trick and who won the trick for the other tricks.
                    currentDirectionToPlayCard = Direction.getNorth();
                    currentDeal = currentDeal.addEvent(new PlayCardEvent(Instant.now(), currentDirectionToPlayCard, LinParser.parseFromLinPC(value)));
                }
            }
            dealList.add(currentDeal); // Finish last deal.
        }
        return dealList;
    }

    public ArrayList<LinKeyValuePair> getAllValuePairs() {
        return this.list;
    }

}
