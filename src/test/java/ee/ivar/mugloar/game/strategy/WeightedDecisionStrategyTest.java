package ee.ivar.mugloar.game.strategy;

import ee.ivar.mugloar.game.GameState;
import ee.ivar.mugloar.game.domain.Message;
import ee.ivar.mugloar.game.domain.Probability;
import ee.ivar.mugloar.game.domain.Shop;
import ee.ivar.mugloar.game.domain.ShopItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class WeightedDecisionStrategyTest {

    private WeightedDecisionStrategy decisionStrategy;
    private GameState gameState = new GameState();
    private GameSettings gameSettings = new GameSettings();
    private Shop shop;

    @BeforeEach
    void setUp() {
        decisionStrategy = new WeightedDecisionStrategy();
        shop = Mockito.mock(Shop.class);
    }

    @Test
    void shouldChooseMessageWithHighestValue() {
        // Given
        Message message1 = new Message("mess1", 30, Probability.HMM);
        Message message2 = new Message("mess2", 3, Probability.RISKY);
        Message message3 = new Message("mess3", 54, Probability.PIECE_OF_CAKE);

        List<Message> messages = List.of(message1, message2, message3);
        gameState.setMessages(messages);

        Map<Probability, Integer> probabilityWeights = Map.of(
                Probability.HMM, 10,
                Probability.RISKY, 20,
                Probability.PIECE_OF_CAKE, 30
        );
        gameSettings.setProbabilityWeights(probabilityWeights);
        gameSettings.setTooGoodToBeTrueLimit(90);

        // When
        Message result = decisionStrategy.chooseMessageToSolve(gameState, gameSettings);

        // Then
        assertThat(result).isEqualTo(message3);
    }

    @Test
    void shouldIgnoreUnknownProbabilityMessages() {
        // Given
        Message message1 = new Message("mess1", 30, Probability.HMM);
        Message message2 = new Message("mess2", 50, Probability.RISKY);
        Message message3 = new Message("mess3", 60, Probability.UNKNOWN);
        List<Message> messages = List.of(message1, message2, message3);
        gameState.setMessages(messages);

        Map<Probability, Integer> probabilityWeights = Map.of(
                Probability.HMM, 10,
                Probability.RISKY, 20,
                Probability.UNKNOWN, 20
        );
        gameSettings.setProbabilityWeights(probabilityWeights);
        gameSettings.setTooGoodToBeTrueLimit(100);

        // When
        Message result = decisionStrategy.chooseMessageToSolve(gameState, gameSettings);

        // Then
        assertThat(result).isEqualTo(message2);
    }

    @Test
    void shouldSetZeroForTooGoodToBeTrue() {
        // Given
        Message message1 = new Message("mess1", 30, Probability.HMM);
        Message message2 = new Message("mess2", 50, Probability.RISKY);
        Message message3 = new Message("mess3", 60, Probability.PIECE_OF_CAKE);
        List<Message> messages = List.of(message1, message2, message3);
        gameState.setMessages(messages);

        Map<Probability, Integer> probabilityWeights = Map.of(
                Probability.HMM, 10,
                Probability.RISKY, 20,
                Probability.PIECE_OF_CAKE, 20
        );
        gameSettings.setProbabilityWeights(probabilityWeights);
        gameSettings.setTooGoodToBeTrueLimit(71);

        // When
        Message result = decisionStrategy.chooseMessageToSolve(gameState, gameSettings);

        // Then
        assertThat(result).isEqualTo(message2);
    }

    @Test
    void shouldChooseItem() {
        // Given
        int gold = 100;

        Shop.Item item1 = new Shop.Item(ShopItem.CS, 50, "name1");
        Shop.Item item2 = new Shop.Item(ShopItem.HPOT, 40, "name2");
        Shop.Item item3 = new Shop.Item(ShopItem.GAS, 30, "name3");

        List<Shop.Item> items = List.of(item1, item2, item3);
        Mockito.when(shop.getItems()).thenReturn(items);

        Map<ShopItem, Integer> itemWeights = Map.of(
                ShopItem.CS, 40,
                ShopItem.HPOT, 20,
                ShopItem.GAS, 30
        );
        gameSettings.setShopItemProbabilityWeights(itemWeights);

        // When
        Shop.Item result = decisionStrategy.chooseItemToBuy(gold, shop, gameSettings);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void chooseItemToBuy_shouldReturnNullIfNoAffordableItems() {
        // Given
        int gold = 10; // Not enough for any item

        Shop.Item item1 = new Shop.Item(ShopItem.CS, 50, "name1");
        Shop.Item item2 = new Shop.Item(ShopItem.HPOT, 40, "name2");
        Shop.Item item3 = new Shop.Item(ShopItem.GAS, 30, "name3");

        List<Shop.Item> items = List.of(item1, item2, item3);
        Mockito.when(shop.getItems()).thenReturn(items);

        // When
        Shop.Item result = decisionStrategy.chooseItemToBuy(gold, shop, gameSettings);

        // Then
        assertThat(result).isNull(); // No items can be bought, so the result should be null
    }

    @Test
    void shouldPickRandomItemBasedOnWeight() {
        // Given
        int gold = 100;

        Shop.Item item1 = new Shop.Item(ShopItem.CS, 50, "name1");
        Shop.Item item2 = new Shop.Item(ShopItem.HPOT, 40, "name2");
        Shop.Item item3 = new Shop.Item(ShopItem.GAS, 30, "name3");

        List<Shop.Item> items = List.of(item1, item2, item3);
        Mockito.when(shop.getItems()).thenReturn(items);

        Map<ShopItem, Integer> itemWeights = Map.of(
                ShopItem.CS, 40,
                ShopItem.HPOT, 20,
                ShopItem.GAS, 30
        );
        gameSettings.setShopItemProbabilityWeights(itemWeights);

        // When
        Shop.Item result = decisionStrategy.chooseItemToBuy(gold, shop, gameSettings);

        // Then
        assertThat(result).isNotNull();
        assertThat(items).contains(result);
    }
}