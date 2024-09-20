package club.libridge.libridgebackend.ben;

import static club.libridge.libridgebackend.logging.LibridgeLogger.LOGGER;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import scalabridge.Hand;

@Component
public class BenWebClient {

    private static final String DEFAULT_BEN_URL = "http://ben-api:8082";
    private static final String BID_ENDPOINT = "/bid/";
    private ObjectMapper objectMapper = new ObjectMapper();
    private WebClient client;

    public BenWebClient() {
        this.client = WebClient.builder().baseUrl(DEFAULT_BEN_URL).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        this.objectMapper = new ObjectMapper();
    }

    public Optional<BenResponse> getBidForEmptyAuction(Hand hand) {
        try {
            String jsonText = client.get().uri(BID_ENDPOINT + hand.toString()).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class)
                    .block();
            return Optional.of(objectMapper.readValue(jsonText, BenResponse.class));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return Optional.empty();
        }
    }

}
