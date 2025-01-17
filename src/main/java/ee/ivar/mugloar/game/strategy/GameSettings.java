package ee.ivar.mugloar.game.strategy;

import ee.ivar.mugloar.game.domain.Probability;
import ee.ivar.mugloar.game.domain.ShopItem;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Data
public class GameSettings {


    private Map<Probability, Integer> probabilityWeights;
    private Map<ShopItem, Integer> shopItemProbabilityWeights;
    private int tooGoodToBeTrueLimit;

    public static GameSettings createWithRandomWeights() {
        GameSettings gameSettings = new GameSettings();

        gameSettings.probabilityWeights = Arrays.stream(Probability.values())
                .collect(Collectors.toMap(
                        probability -> probability,
                        probability -> ThreadLocalRandom.current().nextInt(100)
                ));
        gameSettings.shopItemProbabilityWeights = Arrays.stream(ShopItem.values())
                .collect(Collectors.toMap(
                   item -> item,
                   item -> ThreadLocalRandom.current().nextInt(100)
                ));
        gameSettings.tooGoodToBeTrueLimit = ThreadLocalRandom.current().nextInt(200);
        return gameSettings;
    }

    public static GameSettings createWithNaiveWeights() {
        GameSettings gameSettings = new GameSettings();
        Map<Probability, Integer> probabilityWeights = new HashMap<>();
        probabilityWeights.put(Probability.HMM, 70);
        probabilityWeights.put(Probability.GAMBLE, 40);
        probabilityWeights.put(Probability.PLAYING_WITH_FIRE, 10);
        probabilityWeights.put(Probability.QUITE_LIKELY, 60);
        probabilityWeights.put(Probability.PIECE_OF_CAKE, 80);
        probabilityWeights.put(Probability.RATHER_DETRIMENTAL, 30);
        probabilityWeights.put(Probability.RISKY, 10);
        probabilityWeights.put(Probability.SUICIDE_MISSION, 10);
        probabilityWeights.put(Probability.SURE_THING, 90);
        probabilityWeights.put(Probability.UNKNOWN, 10);
        probabilityWeights.put(Probability.WALK_IN_THE_PARK, 50);

        gameSettings.setProbabilityWeights(probabilityWeights);
        gameSettings.setTooGoodToBeTrueLimit(120);

        Map<ShopItem, Integer> itemWeights = new HashMap<>();
        itemWeights.put(ShopItem.CH, 15);
        itemWeights.put(ShopItem.CS, 70);
        itemWeights.put(ShopItem.GAS, 30);
        itemWeights.put(ShopItem.WAX, 70);
        itemWeights.put(ShopItem.HPOT, 50);
        itemWeights.put(ShopItem.TRICKS, 70);
        itemWeights.put(ShopItem.WINGPOT, 90);
        itemWeights.put(ShopItem.RF, 10);
        itemWeights.put(ShopItem.IRON, 5);
        itemWeights.put(ShopItem.MTRIX, 80);
        itemWeights.put(ShopItem.WINGPOTMAX, 6);
        itemWeights.put(ShopItem.UNKNOWN, 1);
        gameSettings.setShopItemProbabilityWeights(itemWeights);

        return gameSettings;
    }
}
