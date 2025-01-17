package ee.ivar.mugloar.gameserver;

import ee.ivar.mugloar.gameserver.response.GameStartResponse;
import ee.ivar.mugloar.gameserver.response.MessageResponse;
import ee.ivar.mugloar.gameserver.response.ReputationResponse;
import ee.ivar.mugloar.gameserver.response.ShopBuyResponse;
import ee.ivar.mugloar.gameserver.response.ShopResponse;
import ee.ivar.mugloar.gameserver.response.SolveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GameHttpClient {

    private final RestTemplate restTemplate;
    private final GameServerProperties gameServerProperties;

    public GameStartResponse startGame() {
        return restTemplate.postForObject(gameServerProperties.getGameStartUrl(), null, GameStartResponse.class);
    }

    public ReputationResponse investigateReputation(String gameId) {
        return restTemplate.postForObject(gameServerProperties.getInvestigateReputationUrl(gameId), null, ReputationResponse.class);
    }

    public List<MessageResponse> getMessages(String gameId) {
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(gameServerProperties.getMessagesUrl(gameId), MessageResponse[].class)));
    }

    public List<ShopResponse> getShop(String gameId) {
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(gameServerProperties.getShopUrl(gameId), ShopResponse[].class)));
    }

    public ShopBuyResponse shopBuy(String gameId, String itemId) {
        return restTemplate.postForObject(gameServerProperties.getShopBuyUrl(gameId, itemId), null, ShopBuyResponse.class);
    }

    public SolveResponse solve(String gameId, String messageId) {
        return restTemplate.postForObject(gameServerProperties.getSolveUrl(gameId, messageId), null, SolveResponse.class);
    }
}
