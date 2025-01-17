package ee.ivar.mugloar.game.strategy;

import ee.ivar.mugloar.game.domain.Probability;
import ee.ivar.mugloar.game.domain.ShopItem;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GameSettingsTest {

    @Test
    void shouldReturnNaiveSettings() {
        // Given
        GameSettings naiveGameSettings = GameSettings.createWithNaiveWeights();

        // When
        Map<Probability, Integer> probabilityWeights = naiveGameSettings.getProbabilityWeights();
        Map<ShopItem, Integer> shopItemWeights = naiveGameSettings.getShopItemProbabilityWeights();

        // Then
        assertThat(probabilityWeights).containsEntry(Probability.HMM, 70);
        assertThat(probabilityWeights).containsEntry(Probability.GAMBLE, 40);
        assertThat(probabilityWeights).containsEntry(Probability.PLAYING_WITH_FIRE, 10);
        assertThat(probabilityWeights).containsEntry(Probability.SURE_THING, 90);

        assertThat(shopItemWeights).containsEntry(ShopItem.CH, 15);
        assertThat(shopItemWeights).containsEntry(ShopItem.CS, 70);
        assertThat(shopItemWeights).containsEntry(ShopItem.GAS, 30);
        assertThat(shopItemWeights).containsEntry(ShopItem.MTRIX, 80);

        assertThat(naiveGameSettings.getTooGoodToBeTrueLimit()).isEqualTo(120);
    }

    @Test
    void shouldReturnRandomSettings() {
        // Given
        GameSettings randomGameSettings = GameSettings.createWithRandomWeights();

        // When
        Map<Probability, Integer> probabilityWeights = randomGameSettings.getProbabilityWeights();
        Map<ShopItem, Integer> shopItemWeights = randomGameSettings.getShopItemProbabilityWeights();

        // Then
        assertThat(probabilityWeights).isNotEmpty();
        assertThat(shopItemWeights).isNotEmpty();
        assertThat(randomGameSettings.getTooGoodToBeTrueLimit()).isBetween(0, GameSettings.TOO_GOOD_TO_BE_TRUE_MAX_LIMIT);
    }

    @Test
    void shouldReturnPretrainedSettings() {
        // Given
        GameSettings pretrainedGameSettings = GameSettings.createWithPretrainedWeights();

        // When
        Map<Probability, Integer> probabilityWeights = pretrainedGameSettings.getProbabilityWeights();
        Map<ShopItem, Integer> shopItemWeights = pretrainedGameSettings.getShopItemProbabilityWeights();

        // Then
        assertThat(probabilityWeights).containsEntry(Probability.HMM, 56);
        assertThat(probabilityWeights).containsEntry(Probability.GAMBLE, 41);
        assertThat(probabilityWeights).containsEntry(Probability.PIECE_OF_CAKE, 81);
        assertThat(probabilityWeights).containsEntry(Probability.SURE_THING, 96);

        assertThat(shopItemWeights).containsEntry(ShopItem.CH, 6);
        assertThat(shopItemWeights).containsEntry(ShopItem.CS, 59);
        assertThat(shopItemWeights).containsEntry(ShopItem.GAS, 40);
        assertThat(shopItemWeights).containsEntry(ShopItem.MTRIX, 52);

        assertThat(pretrainedGameSettings.getTooGoodToBeTrueLimit()).isEqualTo(163);
    }

}