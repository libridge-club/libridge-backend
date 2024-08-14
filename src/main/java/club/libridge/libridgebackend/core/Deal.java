package club.libridge.libridgebackend.core;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import club.libridge.libridgebackend.core.comparators.CardInsideHandWithSuitComparator;
import club.libridge.libridgebackend.core.exceptions.DoesNotFollowSuitException;
import club.libridge.libridgebackend.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import club.libridge.libridgebackend.core.rulesets.abstractrulesets.Ruleset;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.Suit;

@EqualsAndHashCode
public class Deal {

    /**
     * @deprecated Kryo needs a no-arg constructor FIXME kryo is not used anymore. Does jackson or spring web needs this?
     */
    @Deprecated
    @SuppressWarnings("unused")
    private Deal() {
    }

    private Board board;
    @Getter
    private int completedTricks;
    private int startingNumberOfCardsInTheHand;
    @Getter
    @Setter
    private Direction currentPlayer;
    @Getter
    private Score score;
    private Map<Direction, Player> players = new EnumMap<Direction, Player>(Direction.class);

    @Getter
    private Ruleset ruleset;

    @Getter
    private List<Trick> tricks;
    private Trick currentTrick;
    @Getter
    @Setter
    private Direction dummy;

    @Getter
    private Direction claimer;
    @Getter
    private Map<Direction, Boolean> acceptedClaimMap = new EnumMap<Direction, Boolean>(Direction.class);
    @Getter
    private Boolean isPartnershipGame;

    public Deal(Board board, Ruleset ruleset, Direction leader, Boolean isPartnershipGame) {
        this.board = board;
        this.currentPlayer = leader;
        this.setRuleset(ruleset);
        this.completedTricks = 0;
        this.startingNumberOfCardsInTheHand = board.getHandOf(leader).size();
        this.tricks = new ArrayList<Trick>();
        this.players = new EnumMap<Direction, Player>(Direction.class);
        Direction[] allValues = Direction.values();
        for (Direction direction : allValues) {
            acceptedClaimMap.put(direction, false);
        }
        this.isPartnershipGame = isPartnershipGame;
    }

    public void setRuleset(Ruleset ruleset) {
        this.ruleset = ruleset;
        this.board.sortAllHands(ruleset.getCardComparator());
        this.score = new Score();
    }

    public Player getPlayerOf(Direction direction) {
        return this.players.get(direction);
    }

    public void setPlayerOf(Direction direction, Player player) {
        this.players.put(direction, player);
    }

    public void unsetPlayerOf(Direction direction) {
        this.players.remove(direction);
    }

    public Hand getHandOf(Direction direction) {
        return this.board.getHandOf(direction);
    }

    public Trick getCurrentTrick() {
        if (this.currentTrick == null) {
            return new Trick(currentPlayer);
        } else {
            return this.currentTrick;
        }
    }

    public int getNorthSouthPoints() {
        return this.score.getNorthSouthTricks();
    }

    public int getEastWestPoints() {
        return this.score.getEastWestTricks();
    }

    public boolean isFinished() {
        return allTricksPlayed();
    }

    private boolean allTricksPlayed() {
        return this.completedTricks == startingNumberOfCardsInTheHand;
    }

    /**
     * This method will see if playing the card is a valid move. If it is, it will play it.
     *
     * @param card Card to be played on the board.
     */
    public void playCard(Card card) {
        Hand handOfCurrentPlayer = getHandOfCurrentPlayer();

        throwExceptionIfCardIsNotFromCurrentPlayer(handOfCurrentPlayer, card);
        if (currentTrickHasCards()) {
            throwExceptionIfCardDoesNotFollowSuit(card, handOfCurrentPlayer);
        }
        if (currentTrickNotStartedYet()) {
            this.currentTrick = startNewTrick();
        }

        moveCardFromHandToCurrentTrick(card, handOfCurrentPlayer);

        if (currentTrick.isComplete()) {
            Direction currentTrickWinner = this.getWinnerOfCurrentTrick();
            currentPlayer = currentTrickWinner;
            updateScoreboard(currentTrickWinner);
            completedTricks++;
        } else {
            currentPlayer = currentPlayer.next();
        }

    }

    private Hand getHandOfCurrentPlayer() {
        return this.board.getHandOf(this.currentPlayer);
    }

    private void throwExceptionIfCardIsNotFromCurrentPlayer(Hand handOfCurrentPlayer, Card card) {
        if (!handOfCurrentPlayer.containsCard(card)) {
            throw new PlayedCardInAnotherPlayersTurnException();
        }
    }

    private boolean currentTrickNotStartedYet() {
        return this.currentTrick == null || this.currentTrick.isEmpty() || this.currentTrick.isComplete();
    }

    private boolean currentTrickHasCards() {
        return !currentTrickNotStartedYet();
    }

    private void throwExceptionIfCardDoesNotFollowSuit(Card card, Hand handOfCurrentPlayer) {
        if (!this.ruleset.followsSuit(this.currentTrick, handOfCurrentPlayer, card)) {
            throw new DoesNotFollowSuitException();
        }
    }

    private Trick startNewTrick() {
        Trick newTrick = new Trick(currentPlayer);
        tricks.add(newTrick);
        return newTrick;
    }

    private void moveCardFromHandToCurrentTrick(Card card, Hand handOfCurrentPlayer) {
        // FIXME Should be a transaction
        handOfCurrentPlayer.removeCard(card);
        currentTrick.addCard(card);
    }

    private Direction getWinnerOfCurrentTrick() {
        return this.ruleset.getWinner(currentTrick);
    }

    private void updateScoreboard(Direction currentTrickWinner) {
        this.score.addTrickToDirection(currentTrick, currentTrickWinner);
    }

    public Direction getDealer() {
        return this.board.getDealer();
    }

    public boolean isDummyOpen() {
        return this.getCompletedTricks() > 0 || !this.getCurrentTrick().isEmpty();
    }

    public void sortAllHandsByTrumpSuit(Suit trumpSuit) {
        CardInsideHandWithSuitComparator cardInsideHandWithSuitComparator = new CardInsideHandWithSuitComparator(trumpSuit);
        this.board.sortAllHands(cardInsideHandWithSuitComparator);
    }

    private Direction getCorrectUndoDirectionConsideringDummy(Direction direction) {
        Direction lastPlayer;
        if (this.currentTrickHasCards()) {
            lastPlayer = this.currentPlayer.next(3);
        } else if (this.completedTricks > 0) {
            lastPlayer = this.getPreviousTrick().getLastPlayer();
        } else {
            return direction;
        }
        if (this.dummy != null && direction.next(2) == this.dummy) {
            if (lastPlayer == this.dummy.next(1) || lastPlayer == this.dummy) {
                return this.dummy;
            }
        }
        return direction;
    }

    private Trick getPreviousTrick() {
        if (this.completedTricks == 0) {
            throw new RuntimeException("There is no previous trick.");
        }
        return this.tricks.get(this.completedTricks - 1);
    }

    public void undo(Direction direction) {
        direction = getCorrectUndoDirectionConsideringDummy(direction);
        Map<Card, Direction> removedCardsUpToDirection = this.removeCardsUpTo(direction);
        if (!removedCardsUpToDirection.isEmpty()) {
            this.giveBackCardsToHands(removedCardsUpToDirection);
            if (this.currentTrick.isComplete()) {
                Direction winnerOfTrick = this.ruleset.getWinner(this.currentTrick);
                this.setCurrentPlayer(winnerOfTrick);
            } else {
                this.setCurrentPlayer(direction);
            }
        }
        this.removeLastTrickIfEmpty();
    }

    private void removeLastTrickIfEmpty() {
        if (tricks.isEmpty()) {
            return;
        }
        if (this.completedTricks < this.tricks.size()) {
            Trick lastTrick = this.tricks.get(this.completedTricks);
            if (lastTrick != null && lastTrick.isEmpty()) {
                this.tricks.remove(this.completedTricks);
                if (this.completedTricks > 0) {
                    this.currentTrick = this.tricks.get(this.completedTricks - 1);
                }
            }
        }
    }

    private Map<Card, Direction> removeCardsUpTo(Direction direction) {
        Map<Card, Direction> playedCardsUpToDirection = new HashMap<Card, Direction>();
        if (this.currentTrick == null) {
            return playedCardsUpToDirection;
        } else if (this.currentTrick.hasCardOf(direction)) {
            if (this.currentTrick.isComplete()) {
                this.undoScore(this.currentTrick);
                this.completedTricks--;
            }
            playedCardsUpToDirection = this.currentTrick.getCardsFromLastUpTo(direction);
            this.currentTrick.removeCardsFromLastUpTo(direction);
            return playedCardsUpToDirection;
        } else if (this.tricks.size() >= 2) {
            playedCardsUpToDirection = this.currentTrick.getCardDirectionMap();
            this.removeCurrentTrick();
            playedCardsUpToDirection.putAll(this.currentTrick.getCardsFromLastUpTo(direction));
            this.currentTrick.removeCardsFromLastUpTo(direction);
            this.completedTricks--;
            return playedCardsUpToDirection;
        }
        return playedCardsUpToDirection;
    }

    private void removeCurrentTrick() {
        Trick trickToBeRemoved = this.getCurrentTrick();
        this.setCurrentPlayer(trickToBeRemoved.getLeader());
        this.tricks.remove(this.tricks.size() - 1);
        this.currentTrick = this.tricks.get(this.tricks.size() - 1);
        Trick newCurrentTrick = this.getCurrentTrick();
        this.undoScore(newCurrentTrick);
    }

    private void giveBackCardsToHands(Map<Card, Direction> cardDirectionMap) {
        this.board.putCardInHand(cardDirectionMap);
        this.board.sortAllHands(ruleset.getCardComparator());
    }

    private void undoScore(Trick trick) {
        Direction winnerDirection = this.ruleset.getWinner(trick);
        score.subtractTrickFromDirection(trick, winnerDirection);
    }

    public void giveBackAllCardsToHands() {
        for (Trick trick : tricks) {
            Direction currentDirection = trick.getLeader();
            for (Card card : trick.getCards()) {
                this.board.getHandOf(currentDirection).addCard(card);
                currentDirection = currentDirection.next();
            }
        }
        this.currentTrick = startNewTrick();
        this.board.sortAllHands(ruleset.getCardComparator());
    }

    public void claim(Direction direction) {
        if (this.claimer == null) {
            this.claimer = direction;
        }
    }

    public void acceptClaim(Direction direction) {
        this.acceptedClaimMap.put(direction, true);
        if (this.hasOtherPlayersAcceptedClaim()) {
            this.finishDeal(this.claimer);
        }
    }

    public void rejectClaim() {
        this.claimer = null;
        for (Direction direction : Direction.values()) {
            acceptedClaimMap.put(direction, false);
        }
    }

    private boolean hasOtherPlayersAcceptedClaim() {
        return this.acceptedClaimMap.entrySet().stream().filter(entry -> !isClaimerOrPartner(entry) && !isDummy(entry)).map(Entry::getValue)
                .reduce(Boolean::logicalAnd).orElse(false);
    }

    private boolean isClaimerOrPartner(Map.Entry<Direction, Boolean> entry) {
        Direction direction = entry.getKey();
        boolean isClaimer = direction == this.claimer;
        boolean isClaimerPartner = this.isPartnershipGame && direction.next(2) == this.claimer;
        return isClaimer || isClaimerPartner;
    }

    private boolean isDummy(Map.Entry<Direction, Boolean> entry) {
        return entry.getKey() == this.dummy;
    }

    private void finishDeal(Direction winner) {
        this.finishScore(winner);
    }

    private void finishScore(Direction winner) {
        int totalPoints = this.startingNumberOfCardsInTheHand;
        this.score.finishScore(winner, totalPoints);
    }

}
