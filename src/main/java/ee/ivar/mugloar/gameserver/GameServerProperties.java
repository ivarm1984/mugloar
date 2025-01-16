package ee.ivar.mugloar.gameserver;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "game.server")
@Setter
public class GameServerProperties {

    private String baseUrl;
    private String gameStartUrl;
    private String investigateReputationUrl;
    private String messagesUrl;
    private String solveUrl;
    private String shopUrl;
    private String shopBuyUrl;

    public String getGameStartUrl() {
        return baseUrl + this.gameStartUrl;
    }

    public String getInvestigateReputationUrl(String gameId) {
        return baseUrl + this.investigateReputationUrl.replace(":gameId", gameId);
    }

    public String getMessagesUrl(String gameId) {
        return baseUrl + this.messagesUrl.replace(":gameId", gameId);
    }

    public String getSolveUrl(String gameId, String adId) {
        return baseUrl + this.solveUrl.replace(":gameId", gameId).replace(":adId", adId);
    }

    public String getShopUrl(String gameId) {
        return baseUrl + this.shopUrl.replace(":gameId", gameId);
    }

    public String getShopBuyUrl(String gameId, String itemId) {
        return baseUrl + this.shopBuyUrl.replace(":gameId", gameId).replace(":itemId", itemId);
    }
}
