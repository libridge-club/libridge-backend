package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import club.libridge.libridgebackend.core.exceptions.AuctionAlreadyFinishedException;
import club.libridge.libridgebackend.core.exceptions.CallInAnotherPlayersTurnException;
import club.libridge.libridgebackend.core.exceptions.InsufficientBidException;
import club.libridge.libridgebackend.core.exceptions.InvalidCallException;
import scala.jdk.javaapi.OptionConverters;
import scalabridge.BiddingBox;
import scalabridge.Call;
import scalabridge.Direction;
import scalabridge.PassingCall;

/**
 * The rules for a Bridge Auction are very well established and pretty simple. This test will take this into consideration and ignore many complex
 * cases created by composition of simpler ones.
 *
 * With that said: beware of changing the Auction class. This new code should probably go somewhere else unless the Laws of Bridge have changed.
 */

class AuctionTest {

    @Test
    void shouldGetDealerFromConstructor() {
        Direction east = Direction.getEast();
        Direction north = Direction.getNorth();
        assertEquals(east, new Auction(east).getDealer());
        assertEquals(north, new Auction(north).getDealer());
    }

    @Test
    void shouldReturnAnUnmodifiableListOfBids() {
        Auction subject = new Auction(Direction.getEast());
        List<Call> bids = subject.getBids();
        assertThrows(UnsupportedOperationException.class, () -> {
            bids.add(PassingCall.instance());
        });
    }

    @Test
    void shouldNotBeFinishedJustAfterCreation() {
        Auction auction = new Auction(Direction.getEast());
        assertFalse(auction.isFinished());
    }

    @Test
    void shouldThrowExceptionWhenAPlayerCallsOutOfTurn() {
        Auction auction = new Auction(Direction.getEast());
        Call pass = BiddingBox.getPass();
        assertThrows(CallInAnotherPlayersTurnException.class, () -> {
            auction.makeCall(auction.getDealer().next(), pass);
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerCallsOnAFinishedAuction() {
        Direction direction = Direction.getNorth();
        Auction auction = new Auction(direction);
        Call pass = BiddingBox.getPass();
        for (int i = 0; i < 4; i++) {
            auction.makeCall(direction, pass);
            direction = direction.next();
        }
        assertThrows(AuctionAlreadyFinishedException.class, () -> {
            auction.makeCall(Direction.getNorth(), pass);
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerMakesAnInvalidDouble() {
        Auction auction = new Auction(Direction.getNorth());
        auction.makeCall(Direction.getNorth(), BiddingBox.getPass());
        assertThrows(InvalidCallException.class, () -> {
            auction.makeCall(Direction.getEast(), BiddingBox.getDouble());
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerMakesAnInvalidRedouble() {
        Auction auction = new Auction(Direction.getNorth());
        auction.makeCall(Direction.getNorth(), BiddingBox.getPass());
        assertThrows(InvalidCallException.class, () -> {
            auction.makeCall(Direction.getEast(), BiddingBox.getRedouble());
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerMakesAnInsufficientBid() {
        Auction auction = new Auction(Direction.getNorth());
        auction.makeCall(Direction.getNorth(), OptionConverters.toJava(BiddingBox.getOption("2C")).get());
        assertThrows(InsufficientBidException.class, () -> {
            auction.makeCall(Direction.getEast(), OptionConverters.toJava(BiddingBox.getOption("1N")).get());
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerDoublesAPartnerBid() {
        Auction auction = new Auction(Direction.getNorth());
        auction.makeCall(Direction.getNorth(), OptionConverters.toJava(BiddingBox.getOption("1C")).get());
        auction.makeCall(Direction.getEast(), BiddingBox.getPass());
        assertThrows(InvalidCallException.class, () -> {
            auction.makeCall(Direction.getSouth(), BiddingBox.getDouble());
        });
    }

    @Test
    void shouldThrowExceptionWhenAPlayerRedoublesAPartnerDouble() {
        Auction auction = new Auction(Direction.getNorth());
        auction.makeCall(Direction.getNorth(), OptionConverters.toJava(BiddingBox.getOption("1C")).get());
        auction.makeCall(Direction.getEast(), BiddingBox.getDouble());
        auction.makeCall(Direction.getSouth(), BiddingBox.getPass());
        assertThrows(InvalidCallException.class, () -> {
            auction.makeCall(Direction.getWest(), BiddingBox.getRedouble());
        });
    }

}
