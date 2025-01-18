package ee.ivar.mugloar.game;

import ee.ivar.mugloar.game.domain.GameStartInfo;
import ee.ivar.mugloar.game.domain.Message;
import ee.ivar.mugloar.game.domain.Probability;
import ee.ivar.mugloar.game.domain.Shop;
import ee.ivar.mugloar.game.domain.ShopItem;
import ee.ivar.mugloar.game.domain.SolveResult;
import ee.ivar.mugloar.game.strategy.GameSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameRunnerTest {
    private GameRunner gameRunner;
    private GameService gameService;
    private GameSettings gameSettings;
    private GameStartInfo gameStartInfo;

    @BeforeEach
    void setUp() {
        gameService = mock(GameService.class);
        gameRunner = new GameRunner(gameService);

        gameSettings = GameSettings.createWithNaiveWeights();

        gameStartInfo = new GameStartInfo("game1", 3);
    }

    @Test
    void shouldStartGameAndPlayRounds() {
        // Given
        when(gameService.startGame()).thenReturn(gameStartInfo);
        when(gameService.getMessages(anyString())).thenReturn(List.of(new Message("mess1", 30, Probability.HMM)));
        var solveResult = new SolveResult(1, 5, 100, false, 140);
        var solveResult2 = new SolveResult(0, 5, 100, false, 140);
        when(gameService.solveMessage(anyString(), any(Message.class))).thenReturn(solveResult).thenReturn(solveResult2);
        when(gameService.getShop(anyString())).thenReturn(new Shop(List.of()));

        // When
        int finalScore = gameRunner.runGame(gameSettings);

        // Then
        assertThat(finalScore).isEqualTo(100);
        verify(gameService, times(1)).startGame();
        verify(gameService, atLeastOnce()).getMessages(anyString());
        verify(gameService, atLeastOnce()).solveMessage(anyString(), any(Message.class));
        verify(gameService, atLeastOnce()).getShop(anyString());
    }

    @Test
    void shouldBuyItemAndUpdateGold() {
        // Given
        when(gameService.startGame()).thenReturn(gameStartInfo);
        when(gameService.getMessages(anyString())).thenReturn(List.of(new Message("mess1", 30, Probability.HMM)));

        var solveResult = new SolveResult(1, 5, 100, false, 160);
        var solveResult2 = new SolveResult(0, 5, 100, false, 110);
        when(gameService.solveMessage(anyString(), any(Message.class))).thenReturn(solveResult).thenReturn(solveResult2);

        Shop shop = new Shop(new ArrayList<>());
        Shop.Item itemToBuy = new Shop.Item(ShopItem.CS, 50, "CS");
        shop.setItems(List.of(itemToBuy));
        when(gameService.getShop(anyString())).thenReturn(shop);
        when(gameService.buyItem(anyString(), eq(itemToBuy.getType().getId()))).thenReturn(40);

        // When
        gameRunner.runGame(gameSettings);

        // Then
        verify(gameService).buyItem(anyString(), eq(itemToBuy.getType().getId()));
    }
}