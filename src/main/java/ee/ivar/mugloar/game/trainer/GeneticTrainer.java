package ee.ivar.mugloar.game.trainer;

import ee.ivar.mugloar.game.GameRunner;
import ee.ivar.mugloar.game.domain.Probability;
import ee.ivar.mugloar.game.domain.ShopItem;
import ee.ivar.mugloar.game.strategy.GameSettings;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@Component
@RequiredArgsConstructor
public class GeneticTrainer {

    private final TrainingConfig trainingConfig;

    private final GameRunner gameRunner;

    private final List<Player> players = new ArrayList<>();
    private ExecutorService executor;

    @PostConstruct
    public void setup() {
        executor = Executors.newFixedThreadPool(trainingConfig.getNumberOfParallelGames());
    }

    public GameSettings runTraining() {
        initPlayers();
        for (int i = 0; i < trainingConfig.getTrainingRounds(); i++) {
            runATrainingRound();
            log.info("Round {} best score is {}", i + 1, findBestPlayer().getScore());
            if (i + 1 != trainingConfig.getTrainingRounds()) {
                mate();
            }
        }
        return findBestPlayer().getSettings();
    }

    private Player findBestPlayer() {
        return players.stream()
                .max(Comparator.comparingInt(Player::getScore))
                .orElse(null);
    }

    private void initPlayers() {
        for (int i = 0; i < trainingConfig.getPlayerCount(); i++) {
            players.add(new Player(0, GameSettings.createWithRandomWeights()));
        }
    }

    private void mate() {
        List<Player> survivors = players.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()))
                .limit(trainingConfig.getNumberOfSurvivors())
                .toList();

        players.clear();
        players.addAll(survivors);
        Random random = ThreadLocalRandom.current();
        while (players.size() < trainingConfig.getPlayerCount()) {
            Player parent1 = survivors.get(random.nextInt(survivors.size()));
            Player parent2 = survivors.get(random.nextInt(survivors.size()));

            GameSettings childSettings = mateSettings(parent1.getSettings(), parent2.getSettings());
            players.add(new Player(0, childSettings));
        }
        players.forEach(player -> player.setScore(0));
    }

    private GameSettings mateSettings(GameSettings settings1, GameSettings settings2) {
        GameSettings newSettings = new GameSettings();
        newSettings.setDecisionStrategy(settings1.getDecisionStrategy());

        Map<Probability, Integer> combinedProbabilityWeights = new HashMap<>();
        for (Probability probability : settings1.getProbabilityWeights().keySet()) {
            int weight = ThreadLocalRandom.current().nextBoolean()
                    ? settings1.getProbabilityWeights().get(probability)
                    : settings2.getProbabilityWeights().get(probability);

            weight = applyMutation(weight, -5, 5, GameSettings.PROBABILITY_MAX_WEIGHT);

            combinedProbabilityWeights.put(probability, weight);
        }
        newSettings.setProbabilityWeights(combinedProbabilityWeights);

        Map<ShopItem, Integer> combinedShopItemWeights = new HashMap<>();
        for (ShopItem shopItem : settings1.getShopItemProbabilityWeights().keySet()) {
            int weight = ThreadLocalRandom.current().nextBoolean()
                    ? settings1.getShopItemProbabilityWeights().get(shopItem)
                    : settings2.getShopItemProbabilityWeights().get(shopItem);

            weight = applyMutation(weight, -5, 5, GameSettings.SHOP_ITEM_MAX_WEIGTH);
            combinedShopItemWeights.put(shopItem, weight);
        }
        newSettings.setShopItemProbabilityWeights(combinedShopItemWeights);

        int tooGoodToBeTrueLimit = ThreadLocalRandom.current().nextBoolean()
                ? settings1.getTooGoodToBeTrueLimit()
                : settings2.getTooGoodToBeTrueLimit();

        tooGoodToBeTrueLimit = applyMutation(tooGoodToBeTrueLimit, -20, 21, GameSettings.TOO_GOOD_TO_BE_TRUE_MAX_LIMIT);
        newSettings.setTooGoodToBeTrueLimit(tooGoodToBeTrueLimit);

        return newSettings;
    }

    private int applyMutation(int weight, int x, int x1, int probabilityMaxWeight) {
        if (ThreadLocalRandom.current().nextDouble() < trainingConfig.getMutationChancePercentage()) {
            weight += ThreadLocalRandom.current().nextInt(x, x1);
            weight = Math.min(probabilityMaxWeight, Math.max(0, weight));
        }
        return weight;
    }

    private void runATrainingRound() {

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < trainingConfig.getGamesInOneRound(); i++) {
            tasks.addAll(players.stream()
                    .map(player -> (Callable<Void>) () -> {
                        int score = gameRunner.runGame(player.getSettings());
                        synchronized (player) {
                            player.setScore(player.getScore() + score);
                        }
                        return null;
                    })
                    .toList());

        }
        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
