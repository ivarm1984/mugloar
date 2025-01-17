package ee.ivar.mugloar.gameserver;

import ee.ivar.mugloar.game.domain.GameStartInfo;
import ee.ivar.mugloar.game.domain.Message;
import ee.ivar.mugloar.game.domain.Shop;
import ee.ivar.mugloar.game.domain.ShopItem;
import ee.ivar.mugloar.game.domain.SolveResult;
import ee.ivar.mugloar.gameserver.response.GameStartResponse;
import ee.ivar.mugloar.gameserver.response.MessageResponse;
import ee.ivar.mugloar.gameserver.response.ShopBuyResponse;
import ee.ivar.mugloar.gameserver.response.ShopResponse;
import ee.ivar.mugloar.gameserver.response.SolveResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {

    @Mock
    private GameHttpClient gameHttpClient;

    @Mock
    private MessageConverter messageConverter;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    void shouldReturnGameStartInfo() {
        // Given
        GameStartResponse response = new GameStartResponse();
        response.setGameId("123");
        response.setLives(3);
        when(gameHttpClient.startGame()).thenReturn(response);

        // When
        GameStartInfo result = gameService.startGame();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getGameId()).isEqualTo("123");
        assertThat(result.getLives()).isEqualTo(3);
    }

    @Test
    void shouldReturnListOfMessages() {
        // Given
        String gameId = "123";
        List<MessageResponse> gameMessages = List.of(mock(MessageResponse.class), mock(MessageResponse.class));
        when(gameHttpClient.getMessages(gameId)).thenReturn(gameMessages);
        when(messageConverter.convertToMessage(any())).thenReturn(Message.builder().messageId("mess1").build());

        // When
        List<Message> result = gameService.getMessages(gameId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(gameHttpClient).getMessages(gameId);
        verify(messageConverter, times(2)).convertToMessage(any());
    }

    @Test
    void shouldReturnSolveResult() {
        // Given
        String gameId = "123";
        Message message = Message.builder().messageId("mess1").build();
        SolveResponse response = SolveResponse.builder()
                .message("message")
                .gold(3)
                .score(343)
                .highScore(44)
                .lives(5)
                .turn(6)
                .success(true)
                .build();
        when(gameHttpClient.solve(gameId, message.getMessageId())).thenReturn(response);

        // When
        SolveResult result = gameService.solveMessage(gameId, message);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getLives()).isEqualTo(5);
        assertThat(result.getTurn()).isEqualTo(6);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getScore()).isEqualTo(343);
        assertThat(result.getGold()).isEqualTo(3);
    }

    @Test
    void shouldReturnShop() {
        // Given
        var shopResponse = ShopResponse.builder()
                .name("item1")
                .cost(100)
                .id("gas")
                .build();
        String gameId = "123";
        when(gameHttpClient.getShop(gameId)).thenReturn(List.of(shopResponse));

        // When
        Shop result = gameService.getShop(gameId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getName()).isEqualTo("item1");
        assertThat(result.getItems().get(0).getType()).isEqualTo(ShopItem.GAS);
        assertThat(result.getItems().get(0).getPrice()).isEqualTo(100);
    }

    @Test
    void shouldBuyItem() {
        // Given
        String gameId = "123";
        String itemId = "item1";
        when(gameHttpClient.shopBuy(gameId, itemId)).thenReturn(mock(ShopBuyResponse.class));
        when(gameHttpClient.shopBuy(gameId, itemId).getGold()).thenReturn(100);

        // When
        int result = gameService.buyItem(gameId, itemId);

        // Then
        assertThat(result).isEqualTo(100);
    }
}