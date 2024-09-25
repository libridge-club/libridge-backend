package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import club.libridge.libridgebackend.lin.LinParser;
import club.libridge.libridgebackend.lin.ParsedLin;
import club.libridge.libridgebackend.utils.FileUtils;
import scala.jdk.javaapi.OptionConverters;
import scalabridge.Auction;
import scalabridge.BiddingBox;
import scalabridge.Contract;
import scalabridge.Direction;
import scalabridge.VulnerabilityStatus;

class AuctionExtendedTest {

    @Test
    void shouldFinishSomeValidAuctions() throws IOException {
        String data = FileUtils.readFromFilename("/auctions-valid.txt", false);
        String validAuctions[] = data.split("\n");
        for (String validAuction : validAuctions) {
            Direction current = Direction.getEast();
            Auction auction = new Auction(current);
            for (String call : validAuction.split(" ")) {
                assertFalse(auction.isFinished());
                auction = auction.makeCall(current, OptionConverters.toJava(BiddingBox.getOption(call)).get()).get();
                current = current.next();
            }
            assertTrue(auction.isFinished());
        }
    }

    @Test
    void shouldReadLinFile() throws IOException {
        String linList[] = FileUtils.readFromFilename("/lin/lin_list.txt", false).split("\n");
        Map<String, Integer> quantity = new TreeMap<>();
        for (String linFile : linList) {
            try {
                String data = FileUtils.readFromFilename("/lin/" + linFile, true);
                ParsedLin parsedLin = LinParser.fromString(data);
                List<Auction> auctions = parsedLin.getAuctions();

                for (Auction currentAuction : auctions) {
                    if (currentAuction.getCalls().size() > 0) { // Ignore empty auctions
                        if (!currentAuction.isFinished()) {
                            // System.out.println(linFile + " false");
                        } else { // remove later
                            System.out.println(currentAuction);
                            assertTrue(currentAuction.isFinished());
                            if (currentAuction.getCalls().size() > 4) {
                                Contract finalContract = currentAuction.getFinalContract(VulnerabilityStatus.NONVULNERABLE).get();
                                // System.out.println(finalContract);
                                String finalContractText = finalContract.toString();
                                if (quantity.get(finalContractText) == null) {
                                    quantity.put(finalContractText, 1);
                                } else {
                                    int oldQuantity = quantity.get(finalContractText);
                                    quantity.put(finalContractText, oldQuantity + 1);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // System.out.println(currentAuction);
                // System.out.println(linFile + " exception. Current call: " + currentToken);
            }

        }

    }

    // @Test
    // void shouldReadLinFileAndGetDeals() throws IOException {
    // try {
    // String data = FileUtils.readFromFilename("/example.lin", true);
    // ParsedLin parsedLin = LinParser.fromString(data);
    // List<OpenDeal> deals = parsedLin.getDeals();
    // assertEquals(2, deals.size());

    // OpenDeal firstDeal = deals.get(0);
    // int firstDeal_callsMadeExpected = 13;
    // int firstDeal_callsMadeActual = firstDeal.getDealEvents().toStream().filter(event -> event instanceof CallEvent).toList().size();
    // assertEquals(firstDeal_callsMadeExpected, firstDeal_callsMadeActual);
    // int firstDeal_cardsPlayedActual = 24;
    // int firstDeal_cardsPlayedCurrent = firstDeal.getDealEvents().toStream().filter(event -> event instanceof PlayCardEvent).toList().size();
    // assertEquals(firstDeal_cardsPlayedActual, firstDeal_cardsPlayedCurrent);

    // OpenDeal secondDeal = deals.get(1);
    // int secondDeal_callsMadeExpected = 11;
    // int secondDeal_callsMadeActual = secondDeal.getDealEvents().toStream().filter(event -> event instanceof CallEvent).toList().size();
    // assertEquals(secondDeal_callsMadeExpected, secondDeal_callsMadeActual);
    // int secondDeal_cardsPlayedActual = 24;
    // int secondDeal_cardsPlayedCurrent = secondDeal.getDealEvents().toStream().filter(event -> event instanceof PlayCardEvent).toList().size();
    // assertEquals(secondDeal_cardsPlayedActual, secondDeal_cardsPlayedCurrent);
    // // FIXME Play card direction is wrong for now.
    // // It needs to check who is declarer for first trick and who won the trick for the other tricks.
    // } catch (Exception e) {
    // e.printStackTrace();
    // // System.out.println(currentAuction);
    // // System.out.println(linFile + " exception. Current call: " + currentToken);
    // }

    // }

}
