package club.libridge.libridgebackend.lin;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import club.libridge.libridgebackend.core.Auction;
import club.libridge.libridgebackend.core.BoardNumber;
import club.libridge.libridgebackend.core.PlasticBoard;
import club.libridge.libridgebackend.pbn.PBNUtils;
import scalabridge.Call;
import scalabridge.Direction;
import scalabridge.OpenDeal;
import scalabridge.events.CallEvent;
import scalabridge.events.PlayCardEvent;
import scalabridge.nonpure.DuplicateBoardValidatedBuilder;

/**
 * You can use https://www.bridgebase.com/tools/handviewer.html?lin=<lin> to see what bridge base expects from a LIN.
 */

public class ParsedLin {

    private final Map<LinKey, List<Integer>> symbolToListOfIndexes; // FIXME this should be static
    private final ArrayList<LinKeyValuePair> list;
    private List<Auction> auctions;

    public ParsedLin(List<LinKeyValuePair> list) {
        symbolToListOfIndexes = new EnumMap<LinKey, List<Integer>>(LinKey.class);
        for (LinKey key : LinKey.values()) {
            symbolToListOfIndexes.put(key, new ArrayList<Integer>());
        }

        this.list = new ArrayList<>();
        int i = 0;
        for (LinKeyValuePair linKeyValuePair : list) {
            symbolToListOfIndexes.get(linKeyValuePair.getKey()).add(i);
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
            List<Integer> qxIndexes = symbolToListOfIndexes.get(LinKey.QX);
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
                        Direction dealer = PlasticBoard.getDealerFromBoardNumber(new BoardNumber(boardNumber));
                        currentAuction = new Auction(dealer);
                        currentDirection = dealer;
                    } else if (key.equals(LinKey.MB)) { // For every bid
                        Call currentCall = LinParser.parseFromLinMB(value);
                        currentAuction.makeCall(currentDirection, currentCall);
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
        List<Integer> qxIndexes = symbolToListOfIndexes.get(LinKey.QX);
        if (!qxIndexes.isEmpty()) {
            int firstQxIndex = qxIndexes.get(0);
            Direction currentDealer = null;
            Direction currentDirectionToMakeCall = null;
            Direction currentDirectionToPlayCard = null;
            boolean firstPlayedCard = true;
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
                    currentDealer = Direction.getWest().next(boardNumber);
                    currentDirectionToMakeCall = currentDealer;
                    currentDirectionToPlayCard = null;
                    firstPlayedCard = true;
                } else if (key.equals(LinKey.MD)) { // Hands definition
                    String dealTagString = PBNUtils.getDealTagStringFromLinMD(value, currentDealer);
                    currentDeal = OpenDeal.empty(DuplicateBoardValidatedBuilder.build(boardNumber, dealTagString));
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
