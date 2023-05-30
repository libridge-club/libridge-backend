package br.com.sbk.sbking.networking.rest;

import java.util.UUID;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;

public class RestHTTPClient extends BaseRestHTTPClient {

    public RestHTTPClient(String ip, UUID identifier) {
        super(ip, identifier);
    }

    public RestHTTPClient(String ip) {
        this(ip, null);
    }

    public void play(Card card) {
        String url = this.baseUrl + "playcard";
        String body = String.format("{\"rank\":\"%s\",\"suit\":\"%s\"}",
                card.getRank().getSymbol(), card.getSuit().getSymbol());
        createAndSendPostRequest(url, body);
    }

    public void sendCreateTableMessage(String gameName) {
        String url = this.baseUrl + "table";
        String body = String.format("{\"content\":\"%s\"}", gameName);
        createAndSendPostRequest(url, body);
    }

    public void sendJoinTableMessage(UUID tableId) {
        String url = this.baseUrl + "table/join/" + tableId.toString();
        createAndSendPostRequest(url);
    }

    public void leaveTable() {
        String url = this.baseUrl + "table/leave";
        createAndSendPostRequest(url);
    }

    public void moveToSeat(Direction direction) {
        String url = this.baseUrl + "moveToSeat/" + direction.getAbbreviation();
        createAndSendPostRequest(url);
    }

    public void sendNickname(String nickname) {
        String url = this.baseUrl + "player/nickname";
        String body = String.format("{\"content\":\"%s\"}", nickname);
        createAndSendPutRequest(url, body);
    }

    public void claim() {
        String url = this.baseUrl + "claim";
        createAndSendPostRequest(url);
    }

    public void handleClaim(boolean accept) {
        String url = this.baseUrl + "claim/" + accept;
        createAndSendPostRequest(url);
    }

    public void undo() {
        String url = this.baseUrl + "undo";
        createAndSendPostRequest(url);
    }

    public void choosePositive() {
        String url = this.baseUrl + "choosePositiveOrNegative/" + "+";
        createAndSendPostRequest(url);
    }

    public void chooseNegative() {
        String url = this.baseUrl + "choosePositiveOrNegative/" + "-";
        createAndSendPostRequest(url);
    }

    public void chooseGameModeOrStrain(String gameModeOrStrain) {
        String url = this.baseUrl + "chooseGameModeOrStrain";
        String body = String.format("{\"content\":\"%s\"}", gameModeOrStrain);
        createAndSendPostRequest(url, body);
    }

}
