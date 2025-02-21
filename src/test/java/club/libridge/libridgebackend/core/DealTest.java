package club.libridge.libridgebackend.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import club.libridge.libridgebackend.core.rulesets.abstractrulesets.Ruleset;
import scalabridge.Card;
import scalabridge.CompleteHand;
import scalabridge.Direction;
import scalabridge.DuplicateBoard;
import scalabridge.GameConstants;
import scalabridge.Hand;
import scalabridge.PositiveInteger;

public class DealTest {

    // private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = GameConstants.COMPLETE_TRICK_NUMBER_OF_CARDS();
    private static final Direction leader = Direction.getNorth();

    private DuplicateBoard board;
    private Ruleset ruleset;
    private Direction dealer;
    private CompleteHand hand;

    @BeforeEach
    public void setup() {
        int anyNumberOfCards = 13;
        this.board = mock(DuplicateBoard.class);
        this.ruleset = mock(Ruleset.class);
        this.hand = mock(CompleteHand.class);
        this.dealer = Direction.getNorth();
        when(this.board.getHandOf(any(Direction.class))).thenReturn(this.hand);
        when(this.hand.hand()).thenReturn(mock(Hand.class));
        when(this.hand.hand().size()).thenReturn(anyNumberOfCards);
    }

    @Test
    public void shouldConstructADealWithTheDealerFromTheBoard() {

        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, leader, null);

        assertEquals(leader, deal.getCurrentPlayer());
    }

    @Test
    public void shouldConstructADealWithTheGivenRuleset() {
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, leader, null);

        assertEquals(ruleset, deal.getRuleset());
    }

    @Test
    public void shouldConstructADealWithNoPoints() {
        int noPoints = 0;
        when(board.getDealer()).thenReturn(Direction.getNorth());
        Deal deal = new Deal(board, ruleset, leader, null);
        assertEquals(noPoints, deal.getNorthSouthPoints());
        assertEquals(noPoints, deal.getEastWestPoints());
    }

    @Test
    public void shouldConstructADealWithACurrentTrickWithTheCorrectDealer() {
        Direction trickLeader = leader;
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, trickLeader, null);
        // The correct test would be verifying if new Trick(dealer) was called
        // but Mockito doesn't do that. So, coupling the test with Trick :(
        assertEquals(trickLeader, deal.getCurrentTrick().getLeader());
    }

    @Test
    public void shouldConstructADealWithNoCompletedTricks() {
        when(board.getDealer()).thenReturn(Direction.getNorth());

        Deal deal = new Deal(board, ruleset, leader, null);

        assertEquals(0, deal.getCompletedTricks());
    }

    // @Test
    // public void shouldConstructADealWithASortedBoard() {
    //     when(board.getDealer()).thenReturn(Direction.getNorth());
    //     @SuppressWarnings("unchecked")
    //     Comparator<Card> comparator = mock(Comparator.class);
    //     when(ruleset.getCardComparator()).thenReturn(comparator);

    //     new Deal(board, ruleset, leader, null);

    //     verify(board, atLeastOnce()).sortAllHands(comparator);
    // }

    @Test
    public void shouldGetHandOfACertainDirection() {
        dealer = Direction.getWest();
        Direction currentPlayer = Direction.getNorth();
        Hand oldHand = mock(Hand.class);
        when(board.getHandOf(currentPlayer)).thenReturn(hand);
        when(hand.hand()).thenReturn(oldHand);
        when(board.getDealer()).thenReturn(dealer);
        Deal deal = new Deal(board, ruleset, leader, null);

        assertEquals(oldHand, deal.getHandOf(currentPlayer));
    }

    @Test
    public void shouldAlwaysGetCurrentTrickEvenWhenEmpty() {
        when(board.getDealer()).thenReturn(Direction.getNorth());
        Deal deal = new Deal(board, ruleset, leader, null);

        assertNotNull(deal.getCurrentTrick());
    }

    // @Test
    // public void playCardShouldThrowExceptionWhenCardIsNotFromCurrentPlayer() {
    //     Direction currentPlayer = leader;
    //     CompleteHand handOfCurrentPlayer = mock(CompleteHand.class);
    //     Hand oldHand = mock(Hand.class);
    //     Card card = mock(Card.class);

    //     when(board.getDealer()).thenReturn(dealer);
    //     when(board.getHandOf(currentPlayer)).thenReturn(handOfCurrentPlayer);
    //     when(handOfCurrentPlayer.hand()).thenReturn(oldHand);
    //     when(handOfCurrentPlayer.containsCard(card)).thenReturn(false);

    //     Deal deal = new Deal(board, ruleset, currentPlayer, null);
    //     Assertions.assertThrows(PlayedCardInAnotherPlayersTurnException.class, () -> {
    //         deal.playCard(card);
    //     });
    // }

    // @Test
    // public void firstPlayedCardInATrickShouldAlwaysFollowSuit() {
    //     Card card = mock(Card.class);
    //     Hand oldHand = mock(Hand.class);

    //     when(board.getDealer()).thenReturn(dealer);
    //     when(board.getHandOf(any(Direction.class))).thenReturn(hand);
    //     when(hand.hand()).thenReturn(oldHand);
    //     when(hand.containsCard(card)).thenReturn(true);

    //     when(ruleset.followsSuit(any(), any(), any())).thenReturn(false);

    //     Deal deal = new Deal(board, ruleset, leader, null);

    //     assertEquals(leader, deal.getCurrentPlayer());
    //     deal.playCard(card);
    //     assertNotEquals(leader, deal.getCurrentPlayer());
    // }

    // @Test
    // public void playCardShouldThrowExceptionIfSecondCardDoesNotFollowSuit() {
    //     Card card = mock(Card.class);

    //     when(board.getDealer()).thenReturn(dealer);
    //     when(board.getHandOf(any(Direction.class))).thenReturn(hand);
    //     when(hand.containsCard(card)).thenReturn(true);

    //     when(ruleset.followsSuit(any(), any(), any())).thenReturn(false);

    //     Deal deal = new Deal(board, ruleset, leader, null);

    //     deal.playCard(card); // First card always follows suit.
    //     Assertions.assertThrows(DoesNotFollowSuitException.class, () -> {
    //         deal.playCard(card);
    //     });

    // }

    // @Test
    // public void playCardShouldStartNewTrickIfCurrentTrickIsNotStartedYet() {
    //     Card card = mock(Card.class);

    //     when(board.getDealer()).thenReturn(dealer);
    //     when(board.getHandOf(any(Direction.class))).thenReturn(hand);
    //     when(hand.containsCard(card)).thenReturn(true);

    //     Deal deal = new Deal(board, ruleset, leader, null);

    //     assertTrue(deal.getCurrentTrick().isEmpty());
    //     deal.playCard(card);
    //     assertFalse(deal.getCurrentTrick().isEmpty());

    // }

    // @Test
    // public void playCardShouldMoveCardFromHandToCurrentTrick() {
    //     Card card = mock(Card.class);
    //     int numberOfCardsInTheTrick;

    //     when(board.getDealer()).thenReturn(dealer);
    //     when(board.getHandOf(any(Direction.class))).thenReturn(hand);
    //     when(hand.containsCard(card)).thenReturn(true);

    //     Deal deal = new Deal(board, ruleset, leader, null);
    //     numberOfCardsInTheTrick = deal.getCurrentTrick().getCards().size();
    //     deal.playCard(card);

    //     verify(hand).playCard(card);
    //     assertEquals(numberOfCardsInTheTrick + 1, deal.getCurrentTrick().getCards().size());
    // }

    // @Test
    // public void playCardShouldMoveTurnToNextPlayerIfTrickHasNotEnded() {
    //     Direction currentPlayer = leader;
    //     Direction nextPlayer = currentPlayer.next();
    //     Card card = mock(Card.class);

    //     when(board.getDealer()).thenReturn(dealer);
    //     when(board.getHandOf(any(Direction.class))).thenReturn(hand);
    //     when(hand.containsCard(card)).thenReturn(true);

    //     Deal deal = new Deal(board, ruleset, currentPlayer, null);
    //     deal.playCard(card);

    //     assertEquals(nextPlayer, deal.getCurrentPlayer());
    // }

    // @Test
    // public void playCardShouldMoveTurnToWinnerIfTrickHasEnded() {
    //     Direction winner = Direction.getSouth();
    //     Card card = mock(Card.class);

    //     when(board.getDealer()).thenReturn(dealer);
    //     when(board.getHandOf(any(Direction.class))).thenReturn(hand);
    //     when(hand.containsCard(card)).thenReturn(true);

    //     when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

    //     when(ruleset.getWinner(any())).thenReturn(winner);

    //     Deal deal = new Deal(board, ruleset, leader, null);

    //     for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
    //         deal.playCard(card);
    //     }
    //     assertEquals(1, deal.getCompletedTricks());
    //     assertEquals(winner, deal.getCurrentPlayer());

    // }

    // @Test
    // public void playCardShouldUpdateTheScoreboardIfTrickHasEnded() {
    //     Direction currentPlayer = Direction.getNorth();
    //     Direction winner = Direction.getSouth();
    //     Card card = mock(Card.class);
    //     int trickPoints = 1;

    //     when(board.getDealer()).thenReturn(currentPlayer);
    //     when(board.getHandOf(any(Direction.class))).thenReturn(hand);
    //     when(hand.containsCard(card)).thenReturn(true);

    //     when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

    //     when(ruleset.getWinner(any())).thenReturn(winner);

    //     Deal deal = new Deal(board, ruleset, leader, null);

    //     for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
    //         deal.playCard(card);
    //     }

    //     assertEquals(trickPoints, deal.getNorthSouthPoints());

    // }

    // @Test
    // public void playCardShouldIncrementCompleteTricksIfTrickHasEnded() {
    //     Direction currentPlayer = Direction.getNorth();
    //     Direction winner = Direction.getSouth();
    //     Card card = mock(Card.class);
    //     int noCompletedTricks = 0;
    //     int oneCompletedTricks = noCompletedTricks + 1;

    //     when(board.getDealer()).thenReturn(currentPlayer);
    //     when(board.getHandOf(any(Direction.class))).thenReturn(hand);
    //     when(hand.containsCard(card)).thenReturn(true);

    //     when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

    //     when(ruleset.getWinner(any())).thenReturn(winner);

    //     Deal deal = new Deal(board, ruleset, leader, null);

    //     for (int i = 0; i < COMPLETE_TRICK_NUMBER_OF_CARDS; i++) {
    //         assertEquals(noCompletedTricks, deal.getCompletedTricks());
    //         deal.playCard(card);
    //     }

    //     assertEquals(oneCompletedTricks, deal.getCompletedTricks());

    // }

    // @Test
    // public void shouldSortAllHandsByTrumpSuit() {
    //     when(board.getDealer()).thenReturn(dealer);
    //     Deal deal = new Deal(board, ruleset, leader, null);
    //     Suit anySuit = Suit.getCLUBS();

    //     deal.sortAllHandsByTrumpSuit(anySuit);

    //     verify(board, atLeastOnce()).sortAllHands(any(CardInsideHandWithSuitComparator.class));

    // }

    private Deal initDeal(CompleteHand handOfCurrentPlayer) {
        Card card = mock(Card.class);

        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
        when(board.getDealer()).thenReturn(dealer);

        return new Deal(board, ruleset, leader, null);
    }

    // private Deal initDeal(CompleteHand handOfCurrentPlayer, DuplicateBoard board) {
    //     Card card = mock(Card.class);

    //     when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
    //     when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
    //     when(board.getDealer()).thenReturn(dealer);

    //     return new Deal(board, ruleset, leader, null);
    // }

    private Deal initDeal(CompleteHand handOfCurrentPlayer, Ruleset ruleset) {
        Card card = mock(Card.class);

        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
        when(board.getDealer()).thenReturn(dealer);
        when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

        return new Deal(board, ruleset, leader, null);
    }

    private Deal initDeal(CompleteHand handOfCurrentPlayer, Ruleset ruleset, Boolean isPartnershipGame) {
        Card card = mock(Card.class);

        when(board.getHandOf(any(Direction.class))).thenReturn(handOfCurrentPlayer);
        when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
        when(board.getDealer()).thenReturn(dealer);
        when(ruleset.followsSuit(any(), any(), any())).thenReturn(true);

        return new Deal(board, ruleset, leader, isPartnershipGame);
    }

    // private void playNTimesCard(Deal deal, int n, CompleteHand handOfCurrentPlayer) {
    //     for (int i = 0; i < n; i++) {
    //         Card card = mock(Card.class);
    //         when(handOfCurrentPlayer.containsCard(card)).thenReturn(true);
    //         deal.playCard(card);
    //     }
    // }

    @Test
    public void getTricksShouldReturnEmptyListIfDealHasNoTricks() {
        Deal deal = this.initDeal(hand);

        assertTrue(deal.getTricks().isEmpty());
    }

    @Test
    public void undoShouldDoNothingWhenDealHasNoTricks() {
        Deal deal = this.initDeal(hand);
        Direction directionThatCallsUndo = Direction.getNorth();

        deal.undo(directionThatCallsUndo);

        assertTrue(deal.getTricks().isEmpty());
    }

    // @Test
    // public void undoShouldDoNothingWhenFirstTrickAndCallerHasNotPlayed() {
    //     Deal deal = this.initDeal(hand, board);
    //     Direction directionThatCallsUndo = Direction.getEast();
    //     playNTimesCard(deal, 1, hand);
    //     Direction currentPlayerBeforeEachUndo = deal.getCurrentPlayer();

    //     deal.undo(directionThatCallsUndo);

    //     Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
    //     verify(board, never()).unplayCardsInHands(any());
    //     assertEquals(currentPlayerBeforeEachUndo, currentPlayerAfterUndo);
    // }

    //@Test //Undo is not implemented yet
    // public void undoShouldRemoveTrickAndSetCurrentPlayerWhenFirstPlayerAskedForUndo() {
    //     Deal deal = this.initDeal(hand);
    //     Direction currentPlayer = deal.getCurrentPlayer();
    //     Direction directionThatCallsUndo = currentPlayer;
    //     playNTimesCard(deal, 1, hand);

    //     deal.undo(directionThatCallsUndo);

    //     Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
    //     assertTrue(deal.getCurrentTrick().getCards().isEmpty());
    //     assertEquals(0, deal.getTricks().size());
    //     assertEquals(directionThatCallsUndo, currentPlayerAfterUndo);
    // }

    //@Test //Undo is not implemented yet
    // public void undoShouldRemoveCardAndSetCurrentPlayerWhenLastPlayerAskedForUndo() {
    //     Deal deal = this.initDeal(hand, ruleset);
    //     when(ruleset.getWinner(any())).thenReturn(Direction.getNorth());
    //     playNTimesCard(deal, 3, hand);
    //     Direction currentPlayer = deal.getCurrentPlayer();
    //     playNTimesCard(deal, 1, hand);
    //     Direction directionThatCallsUndo = currentPlayer;

    //     deal.undo(directionThatCallsUndo);

    //     Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
    //     assertEquals(3, deal.getCurrentTrick().getCards().size());
    //     assertEquals(1, deal.getTricks().size());
    //     assertEquals(directionThatCallsUndo, currentPlayerAfterUndo);
    // }

    //@Test //Undo is not implemented yet
    // public void undoShouldDoNothingWhenFirstTrickHasMoreThanOneCardAndCallerHasNotPlayedYet() {
    //     Deal deal = this.initDeal(hand, ruleset);
    //     playNTimesCard(deal, 2, hand);
    //     Direction currentPlayer = deal.getCurrentPlayer();
    //     Direction directionThatCallsUndo = currentPlayer.next();

    //     deal.undo(directionThatCallsUndo);

    //     Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
    //     assertEquals(2, deal.getCurrentTrick().getCards().size());
    //     assertEquals(1, deal.getTricks().size());
    //     assertEquals(currentPlayer, currentPlayerAfterUndo);
    // }

    //@Test //Undo is not implemented yet
    // public void undoShouldRemoveTwoTricksWhenLeaderOfThePreviousTrickAsksForUndoOnCurrentTrickBeforeEachPlayCard() {
    //     Deal deal = this.initDeal(hand, ruleset);
    //     Direction firstPlayer = deal.getCurrentPlayer();
    //     Direction anyOtherPlayer = firstPlayer.next(new PositiveInteger(3));
    //     when(ruleset.getWinner(any())).thenReturn(firstPlayer);
    //     int numberOfTricks = 10;
    //     playNTimesCard(deal, numberOfTricks * 4 - 1, hand);
    //     when(ruleset.getWinner(any())).thenReturn(anyOtherPlayer);
    //     playNTimesCard(deal, 2, hand);

    //     deal.undo(firstPlayer);

    //     Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
    //     assertEquals(4, deal.getCurrentTrick().getCards().size());
    //     assertEquals(numberOfTricks - 1, deal.getTricks().size());
    //     assertEquals(firstPlayer, currentPlayerAfterUndo);
    // }

    //@Test //Undo is not implemented yet
    // public void undoShouldUpdateScoreWhenCallHappensImmediatelyAfterCurrentTrickIsComplete() {
    //     Deal deal = this.initDeal(hand, ruleset);
    //     Direction firstPlayer = deal.getCurrentPlayer();
    //     when(ruleset.getWinner(any())).thenReturn(firstPlayer);
    //     int anyNumberOfTricks = 10;
    //     playNTimesCard(deal, anyNumberOfTricks * 4 - 1, hand);
    //     Score previousScore = deal.getScore();
    //     playNTimesCard(deal, 1, hand);

    //     deal.undo(firstPlayer);

    //     Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
    //     Score scoreAfterUndo = deal.getScore();
    //     assertEquals(4, deal.getCurrentTrick().getCards().size());
    //     assertEquals(anyNumberOfTricks - 1, deal.getTricks().size());
    //     assertEquals(firstPlayer, currentPlayerAfterUndo);
    //     assertEquals(previousScore, scoreAfterUndo);
    // }

    //@Test //Undo is not implemented yet
    // public void undoShouldUpdateCompletedTricksWhenFirstTrickIsCompleteAndAnyPlayerCallsUndo() {
    //     Deal deal = this.initDeal(hand, ruleset);
    //     Direction firstPlayer = deal.getCurrentPlayer();
    //     Direction anyPlayer = firstPlayer;
    //     when(ruleset.getWinner(any())).thenReturn(firstPlayer);
    //     playNTimesCard(deal, 1, hand);

    //     deal.undo(anyPlayer);

    //     int completedTricksAfterUndo = deal.getCompletedTricks();
    //     Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
    //     assertEquals(0, deal.getCurrentTrick().getCards().size());
    //     assertEquals(0, deal.getTricks().size());
    //     assertEquals(firstPlayer, currentPlayerAfterUndo);
    //     assertEquals(0, completedTricksAfterUndo);
    // }

    //@Test //Undo is not implemented yet
    // public void undoShouldUpdateCompletedTricksWhenNotFirstTrickAndTrickIsCompleteAndAnyPlayerCallsUndo() {
    //     Deal deal = this.initDeal(hand, ruleset);
    //     Direction firstPlayer = deal.getCurrentPlayer();
    //     Direction anyPlayer = firstPlayer;
    //     when(ruleset.getWinner(any())).thenReturn(firstPlayer);
    //     int anyNumberOfTricksDifferentThanOne = 13;
    //     playNTimesCard(deal, anyNumberOfTricksDifferentThanOne * 4, hand);

    //     deal.undo(anyPlayer);

    //     int completedTricksAfterUndo = deal.getCompletedTricks();
    //     Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
    //     assertEquals(4, deal.getCurrentTrick().getCards().size());
    //     assertEquals(anyNumberOfTricksDifferentThanOne - 1, deal.getTricks().size());
    //     assertEquals(firstPlayer, currentPlayerAfterUndo);
    //     assertEquals(anyNumberOfTricksDifferentThanOne - 1, completedTricksAfterUndo);
    // }

    //@Test //Undo is not implemented yet
    // public void undoShouldNotChangeTrickWhenUndoWasNotCalledByTheLeader() {
    //     Deal deal = this.initDeal(hand, ruleset);
    //     Direction firstPlayer = deal.getCurrentPlayer();
    //     Direction lastPlayer = firstPlayer.next();
    //     int anyNumberOfTricks = 2;
    //     when(ruleset.getWinner(any())).thenReturn(firstPlayer);
    //     playNTimesCard(deal, anyNumberOfTricks * 4, hand);

    //     deal.undo(lastPlayer);

    //     int completedTricksAfterUndo = deal.getCompletedTricks();
    //     Direction currentPlayerAfterUndo = deal.getCurrentPlayer();
    //     assertEquals(1, deal.getCurrentTrick().getCards().size());
    //     assertEquals(anyNumberOfTricks, deal.getTricks().size());
    //     assertEquals(lastPlayer, currentPlayerAfterUndo);
    //     assertEquals(anyNumberOfTricks - 1, completedTricksAfterUndo);
    // }

    //@Test //Undo is not implemented yet
    // public void giveBackAllCardsToHandsShouldReturnCardsToHands() {
    //     Deal deal = this.initDeal(hand, ruleset);
    //     Direction firstPlayer = deal.getCurrentPlayer();
    //     when(ruleset.getWinner(any())).thenReturn(firstPlayer);
    //     int numberOfCardsGiveBackToHands = GameConstants.COMPLETE_TRICK_NUMBER_OF_CARDS() * GameConstants.NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND();
    //     playNTimesCard(deal, numberOfCardsGiveBackToHands, hand);

    //     deal.giveBackAllCardsToHands();

    //     verify(hand, times(numberOfCardsGiveBackToHands)).addCard(any());
    //     assertTrue(deal.getCurrentTrick().isEmpty());
    // }

    @Test
    public void claimShouldSetClaimer() {
        Deal deal = this.initDeal(hand, ruleset);
        Direction claimer = deal.getCurrentPlayer();

        deal.claim(claimer);

        assertEquals(claimer, deal.getClaimer());
    }

    @Test
    public void acceptClaimShouldAddPlayerToAcceptedClaimMap() {
        Deal deal = this.initDeal(hand, ruleset, true);
        Direction claimer = deal.getCurrentPlayer();

        deal.claim(claimer);
        deal.acceptClaim(claimer.next());

        assertEquals(true, deal.getAcceptedClaimMap().get(claimer.next()));
    }

    @Test
    public void acceptClaimShouldFinishDealIfAllPlayersAcceptedClaimAndItIsPartnershipGame() {
        Deal deal = this.initDeal(hand, ruleset, true);
        Direction claimer = deal.getCurrentPlayer();
        int totalPoints = GameConstants.NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND();
        ;

        deal.claim(claimer);
        deal.acceptClaim(claimer.next());
        deal.acceptClaim(claimer.next(new PositiveInteger(3)));

        assertEquals(totalPoints, deal.getScore().getNorthSouthTricks());
    }

    @Test
    public void acceptClaimShouldFinishDealIfAllPlayersAcceptedClaimAndItIsNotPartnershipGame() {
        Deal deal = this.initDeal(hand, ruleset, false);
        Direction claimer = deal.getCurrentPlayer();
        int totalPoints = GameConstants.NUMBER_OF_TRICKS_IN_A_COMPLETE_HAND();

        deal.claim(claimer);
        deal.acceptClaim(claimer.next());
        deal.acceptClaim(claimer.next(new PositiveInteger(2)));
        deal.acceptClaim(claimer.next(new PositiveInteger(3)));

        assertEquals(totalPoints, deal.getScore().getNorthSouthTricks());
    }

    // @Test
    // public void rejectClaimShouldRemoveClaimerAndResetAcceptedClaims() {
    //     Deal deal = this.initDeal(hand, ruleset, true);
    //     Direction claimer = deal.getCurrentPlayer();

    //     deal.claim(claimer);
    //     this.playNTimesCard(deal, 1, hand);
    //     deal.acceptClaim(claimer.next());
    //     deal.rejectClaim();

    //     for (Direction direction : Direction.values()) {
    //         assertEquals(false, deal.getAcceptedClaimMap().get(direction));
    //     }
    //     assertEquals(null, deal.getClaimer());
    // }
}
