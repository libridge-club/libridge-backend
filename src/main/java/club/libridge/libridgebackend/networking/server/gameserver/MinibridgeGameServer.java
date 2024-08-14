package club.libridge.libridgebackend.networking.server.gameserver;

import static club.libridge.libridgebackend.logging.LibridgeLogger.LOGGER;

import java.util.Deque;

import club.libridge.libridgebackend.core.boarddealer.Complete52CardDeck;
import club.libridge.libridgebackend.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import club.libridge.libridgebackend.core.exceptions.SelectedStrainInAnotherPlayersTurnException;
import club.libridge.libridgebackend.core.game.MinibridgeGame;
import club.libridge.libridgebackend.core.rulesets.RulesetFromStrainString;
import club.libridge.libridgebackend.core.rulesets.concrete.PositiveRuleset;
import club.libridge.libridgebackend.core.rulesets.concrete.PositiveWithTrumpsRuleset;
import club.libridge.libridgebackend.networking.server.notifications.CardPlayNotification;
import club.libridge.libridgebackend.networking.server.notifications.StrainNotification;
import scalabridge.Card;
import scalabridge.Direction;
import scalabridge.Strain;

public class MinibridgeGameServer extends GameServer {

    private StrainNotification strainNotification;
    private Strain currentStrain;
    private MinibridgeGame minibridgeGame;
    private boolean isRulesetPermitted;

    public MinibridgeGameServer() {
        this.setGameWithCardDeck(new Complete52CardDeck().getDeck());
    }

    @Override
    public void run() {

        while (!shouldStop && !game.isFinished()) {
            this.game.dealNewBoard();

            do {
                this.copyPlayersFromTableToGame();
                this.copyPlayersFromTableToDeal();
                this.sendInitializeDealAll();
                this.sendDealAll();

                this.strainNotification = new StrainNotification();
                synchronized (strainNotification) {
                    // wait until object notifies - which relinquishes the lock on the object too
                    while (!shouldStop && strainNotification.getStrain() == null) {
                        LOGGER.debug("getStrain: {}", strainNotification.getStrain());
                        try {
                            LOGGER.debug("I am waiting for some thread to notify that it wants to choose game Mode Or Strain");
                            strainNotification.wait(3000);
                            this.sendStrainChooserAll();
                        } catch (InterruptedException e) {
                            LOGGER.error(e);
                        }
                    }
                }
                if (this.shouldStop) {
                    return;
                }
                LOGGER.info("I received that is going to be " + strainNotification.getStrain().getName());
                this.currentStrain = strainNotification.getStrain();

                if (this.currentStrain == null) {
                    LOGGER.warn("This ruleset is not permitted. Restarting choose procedure");
                    this.sendInvalidRulesetAll();
                    this.isRulesetPermitted = false;
                } else {
                    this.sendValidRulesetAll();
                    this.isRulesetPermitted = true;
                }

            } while (!shouldStop && !isRulesetPermitted);
            if (this.shouldStop) {
                return;
            }

            LOGGER.info("Everything selected! Game commencing!");
            this.minibridgeGame.setRuleset(RulesetFromStrainString.identify(currentStrain.getName()));

            PositiveRuleset currentRuleset = RulesetFromStrainString.identify(currentStrain.getName());
            if (currentRuleset instanceof PositiveWithTrumpsRuleset positiveWithTrumpsRuleset) {
                this.game.getCurrentDeal().sortAllHandsByTrumpSuit(positiveWithTrumpsRuleset.getTrumpSuit());
            }

            this.dealHasChanged = true;
            while (!shouldStop && !this.game.getCurrentDeal().isFinished()) {
                if (this.dealHasChanged) {
                    LOGGER.debug("Sending new 'round' of deals");
                    this.sendDealAll();
                    this.dealHasChanged = false;
                }
                synchronized (cardPlayNotification) {
                    // wait until object notifies - which relinquishes the lock on the object too
                    try {
                        LOGGER.trace("I am waiting for some thread to notify that it wants to play a card.");
                        cardPlayNotification.wait(this.timeoutCardPlayNotification);
                    } catch (InterruptedException e) {
                        LOGGER.error(e);
                    }
                }
                if (this.shouldStop) {
                    return;
                }
                this.executeCardPlayNotification(cardPlayNotification);
                cardPlayNotification = new CardPlayNotification();
            }
            if (this.shouldStop) {
                return;
            }

            this.sendDealAll();
            this.sleepToShowLastCard();
            if (this.shouldStop) {
                return;
            }

            this.giveBackAllCards();
            this.sendDealAll();
            this.sleepToShowHands();
            if (this.shouldStop) {
                return;
            }

            this.game.finishDeal();

            this.libridgeServer.sendFinishDealToTable(this.table);
            LOGGER.info("Deal finished!");
        }

        LOGGER.info("Game has ended.");
    }

    @Override
    protected void playCard(Card card, Direction direction) {
        Direction currentDirectionToPlay = this.game.getCurrentDeal().getCurrentPlayer();
        LOGGER.debug("It is currently the {} turn", currentDirectionToPlay);
        try {
            if (this.isAllowedToPlayCardInTurnOf(direction, currentDirectionToPlay)) {
                syncPlayCard(card);
                this.dealHasChanged = true;
            } else {
                throw new PlayedCardInAnotherPlayersTurnException();
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    private boolean isAllowedToPlayCardInTurnOf(Direction player, Direction currentTurn) {
        Direction declarer = this.minibridgeGame.getDeclarer();
        Direction dummy = this.minibridgeGame.getDummy();
        if (currentTurn == dummy) {
            return player == declarer;
        } else {
            return player == currentTurn;
        }
    }

    public void notifyChooseStrain(Strain strain, Direction direction) {
        synchronized (strainNotification) {
            if (this.getCurrentStrainChooser() == direction) {
                this.strainNotification.notifyAllWithStrain(strain);
            } else {
                throw new SelectedStrainInAnotherPlayersTurnException();
            }
        }
    }

    private Direction getCurrentStrainChooser() {
        return this.minibridgeGame.getDeclarer();
    }

    private void sendStrainChooserAll() {
        this.libridgeServer.sendStrainChooserToTable(this.getCurrentStrainChooser(), this.table);
    }

    protected void setGameWithCardDeck(Deque<Card> deck) {
        this.game = new MinibridgeGame(deck);
        this.minibridgeGame = (MinibridgeGame) this.game;
    }

}
