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

    public static final int PROBABILITY_MAX_WEIGHT = 100;
    public static final int SHOP_ITEM_MAX_WEIGTH = 100;
    public static final int TOO_GOOD_TO_BE_TRUE_MAX_LIMIT = 200;
    private Map<Probability, Integer> probabilityWeights;
    private Map<ShopItem, Integer> shopItemProbabilityWeights;
    private int tooGoodToBeTrueLimit;
    private DecisionStrategy decisionStrategy;

    public static GameSettings createWithRandomWeights() {
        GameSettings gameSettings = new GameSettings();

        gameSettings.probabilityWeights = Arrays.stream(Probability.values())
                .collect(Collectors.toMap(
                        probability -> probability,
                        probability -> ThreadLocalRandom.current().nextInt(PROBABILITY_MAX_WEIGHT)
                ));
        gameSettings.shopItemProbabilityWeights = Arrays.stream(ShopItem.values())
                .collect(Collectors.toMap(
                   item -> item,
                   item -> ThreadLocalRandom.current().nextInt(SHOP_ITEM_MAX_WEIGTH)
                ));
        gameSettings.tooGoodToBeTrueLimit = ThreadLocalRandom.current().nextInt(TOO_GOOD_TO_BE_TRUE_MAX_LIMIT);
        gameSettings.decisionStrategy = new RandomDecisionStrategy();
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

        gameSettings.decisionStrategy = new WeightedDecisionStrategy();

        return gameSettings;
    }

    public static GameSettings createWithPretrainedWeights() {
        GameSettings gameSettings = new GameSettings();
        Map<Probability, Integer> probabilityWeights = new HashMap<>();
        probabilityWeights.put(Probability.HMM, 56);
        probabilityWeights.put(Probability.GAMBLE, 41);
        probabilityWeights.put(Probability.PLAYING_WITH_FIRE, 16);
        probabilityWeights.put(Probability.QUITE_LIKELY, 54);
        probabilityWeights.put(Probability.PIECE_OF_CAKE, 81);
        probabilityWeights.put(Probability.RATHER_DETRIMENTAL, 94);
        probabilityWeights.put(Probability.RISKY, 82);
        probabilityWeights.put(Probability.SUICIDE_MISSION, 24);
        probabilityWeights.put(Probability.SURE_THING, 96);
        probabilityWeights.put(Probability.UNKNOWN, 42);
        probabilityWeights.put(Probability.WALK_IN_THE_PARK, 74);

        gameSettings.setProbabilityWeights(probabilityWeights);
        gameSettings.setTooGoodToBeTrueLimit(163);

        Map<ShopItem, Integer> itemWeights = new HashMap<>();
        itemWeights.put(ShopItem.CH, 6);
        itemWeights.put(ShopItem.CS, 59);
        itemWeights.put(ShopItem.GAS, 40);
        itemWeights.put(ShopItem.WAX, 54);
        itemWeights.put(ShopItem.HPOT, 8);
        itemWeights.put(ShopItem.TRICKS, 55);
        itemWeights.put(ShopItem.WINGPOT, 40);
        itemWeights.put(ShopItem.RF, 35);
        itemWeights.put(ShopItem.IRON, 54);
        itemWeights.put(ShopItem.MTRIX, 52);
        itemWeights.put(ShopItem.WINGPOTMAX, 30);
        itemWeights.put(ShopItem.UNKNOWN, 42);
        gameSettings.setShopItemProbabilityWeights(itemWeights);

        gameSettings.decisionStrategy = new WeightedDecisionStrategy();

        return gameSettings;
    }
}
