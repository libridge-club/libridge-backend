package br.com.sbk.sbking.networking.rest;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sbk.sbking.clientapp.ClientObjectMapperConfiguration;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.dto.LobbyScreenTableDTO;

public class RestHTTPClient extends BaseRestHTTPClient {

    ObjectMapper mapper;

    public RestHTTPClient(String ip) {
        super(ip, null);
        this.mapper = new ClientObjectMapperConfiguration().objectMapper();
    }

    // GET

    public UUID connect() {
        String url = this.baseUrl + "connect";
        LOGGER.info("SENDING FIRST CONNECT MESSAGE to URL: {}", url);
        String result = this.getWithoutIdentification(url);
        LOGGER.info("GOT RESULT: {}", result);
        UUID response = null;
        try {
            response = this.mapper.readValue(result, UUID.class);
        } catch (JsonProcessingException e) {
            LOGGER.error(e);
        }
        LOGGER.debug("RECEIVED RESPONSE: {}", response);
        return response;
    }

    public void askForRefreshTable(UUID tableId) {
        String url = this.baseUrl + "table/refresh/" + tableId.toString();
        post(url);
    }

    public List<String> getSpectators() {
        String url = this.baseUrl + "spectators";
        String result = get(url);
        List<String> response = new ArrayList<String>();
        try {
            response = this.mapper.readValue(result, new TypeReference<List<String>>() {
            });
        } catch (JsonProcessingException e) {
            LOGGER.error(e);
        }
        return response;
    }

    public List<LobbyScreenTableDTO> getTables() {
        String url = this.baseUrl + "tables";
        LOGGER.trace("getTables");
        String result = get(url);
        List<LobbyScreenTableDTO> response = new ArrayList<LobbyScreenTableDTO>();
        try {
            response = this.mapper.readValue(result, new TypeReference<List<LobbyScreenTableDTO>>() {
            });
        } catch (JsonProcessingException e) {
            LOGGER.error(e);
        }
        return response;
    }

    // POST

    public void play(Card card) {
        String url = this.baseUrl + "playcard";
        String body = String.format("{\"rank\":\"%s\",\"suit\":\"%s\"}",
                card.getRank().getSymbol(), card.getSuit().getSymbol());
        post(url, body);
    }

    public String sendCreateTableMessage(String gameName) {
        String url = this.baseUrl + "table";
        String body = createBodyFromContent(gameName);
        return post(url, body);
    }

    public void sendJoinTableMessage(UUID tableId) {
        String url = this.baseUrl + "table/join/" + tableId.toString();
        post(url);
    }

    public void leaveTable() {
        String url = this.baseUrl + "table/leave";
        post(url);
    }

    public void moveToSeat(Direction direction) {
        String url = this.baseUrl + "moveToSeat/" + direction.getAbbreviation();
        post(url);
    }

    public void claim() {
        String url = this.baseUrl + "claim";
        post(url);
    }

    public void handleClaim(boolean accept) {
        String url = this.baseUrl + "claim/" + accept;
        post(url);
    }

    public void undo() {
        String url = this.baseUrl + "undo";
        post(url);
    }

    public void chooseStrain(String strain) {
        String url = this.baseUrl + "chooseStrain";
        String body = createBodyFromContent(strain);
        post(url, body);
    }

    // PUT

    public void sendNickname(String nickname) {
        String url = this.baseUrl + "player/nickname";
        String body = createBodyFromContent(nickname);
        put(url, body);
    }

}
