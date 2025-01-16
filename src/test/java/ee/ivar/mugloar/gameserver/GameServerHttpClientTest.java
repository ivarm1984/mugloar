package ee.ivar.mugloar.gameserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class GameServerHttpClientTest {

    @Autowired
    private GameHttpClient gameHttpClient;

    @Test
    void shouldStartGame() {
        // when
        GameStartResponse response = gameHttpClient.startGame();

        // then
        assertThat(response.getGameId()).isEqualTo("REVTn1Jg");
        assertThat(response.getLives()).isEqualTo(3);
    }

    @Test
    void shouldHandleInvestigateReputationResponse() {
        // when
        ReputationResponse response = gameHttpClient.investigateReputation("REVTn1Jg");

        // then
        assertThat(response.getPeople()).isEqualTo(4.800000000000001);
        assertThat(response.getState()).isEqualTo(-4);
        assertThat(response.getUnderworld()).isEqualTo(0);
    }

    @Test
    void shouldHandleMessagesResponse() {
        // when
        List<MessageResponse> response = gameHttpClient.getMessages("REVTn1Jg");

        // then
        assertThat(response).hasSize(10);
        assertThat(response.get(0).getAdId()).isEqualTo("qiylQBpT");
        assertThat(response.get(0).getMessage()).isNotEmpty();
        assertThat(response.get(0).getReward()).isEqualTo(67);
        assertThat(response.get(0).getExpiresIn()).isEqualTo(6);
        assertThat(response.get(0).getEncrypted()).isNull();
        assertThat(response.get(0).getProbability()).isEqualTo("Piece of cake");
    }

    @Test
    void shouldHandleSolveResponse() {
        // when
        SolveResponse response = gameHttpClient.solve("REVTn1Jg", "message1");

        // then
        assertThat(response.getGold()).isEqualTo(24);
        assertThat(response.getMessage()).isEqualTo("You successfully solved the mission!");
        assertThat(response.getTurn()).isEqualTo(2);
        assertThat(response.getLives()).isEqualTo(3);
        assertThat(response.getScore()).isEqualTo(24);
        assertThat(response.getHighScore()).isEqualTo(0);
    }

    @Test
    void shouldHandleShopResponse() {
        // when
        List<ShopResponse> response = gameHttpClient.getShop("REVTn1Jg");

        // then
        assertThat(response).hasSize(11);
        assertThat(response.get(0).getId()).isEqualTo("hpot");
        assertThat(response.get(0).getCost()).isEqualTo(50);
        assertThat(response.get(0).getName()).isEqualTo("Healing potion");
    }

    @Test
    void shouldHandleShopBuyResponse() {
        // when
        ShopBuyResponse response = gameHttpClient.shopBuy("REVTn1Jg", "hpot");

        // then
        assertThat(response.getGold()).isEqualTo(66);
        assertThat(response.getLevel()).isEqualTo(0);
        assertThat(response.getTurn()).isEqualTo(5);
        assertThat(response.getLives()).isEqualTo(4);
    }
}
