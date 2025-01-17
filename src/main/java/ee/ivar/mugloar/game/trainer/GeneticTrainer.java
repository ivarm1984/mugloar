package ee.ivar.mugloar.game.trainer;

import ee.ivar.mugloar.game.GameRunner;
import ee.ivar.mugloar.game.domain.Probability;
import ee.ivar.mugloar.game.domain.ShopItem;
import ee.ivar.mugloar.game.strategy.GameSettings;
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

    private static final int NUMBER_OF_PARALLEL_GAMES = 150;
    private static final int TRAINING_ROUNDS = 10;
    private static final int GAMES_IN_ONE_ROUND = 1;
    private static final int PLAYER_COUNT = 100;
    private static final int NR_OF_SURVIVORS = 10;
    private final GameRunner gameRunner;

    private final List<Player> players = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_PARALLEL_GAMES);

    public GameSettings runTraining() {
        initPlayers();
        for (int i = 0; i < TRAINING_ROUNDS; i++) {
            runATrainingRound();
            log.info("Round {} best score is {}", i + 1, findBestPlayer().getScore());
            if (i + 1 != TRAINING_ROUNDS) {
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
        for (int i = 0; i < PLAYER_COUNT; i++) {
            players.add(new Player(0, GameSettings.createWithRandomWeights()));
        }
    }

    private void mate() {
        List<Player> survivors = players.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()))
                .limit(NR_OF_SURVIVORS)
                .toList();

        players.clear();
        players.addAll(survivors);
        Random random = ThreadLocalRandom.current();
        while (players.size() < PLAYER_COUNT) {
            Player parent1 = survivors.get(random.nextInt(survivors.size()));
            Player parent2 = survivors.get(random.nextInt(survivors.size()));

            GameSettings childSettings = mateSettings(parent1.getSettings(), parent2.getSettings());
            players.add(new Player(0, childSettings));
        }
        players.forEach(player -> player.setScore(0));
    }

    private GameSettings mateSettings(GameSettings settings1, GameSettings settings2) {
        GameSettings newSettings = new GameSettings();

        Map<Probability, Integer> combinedProbabilityWeights = new HashMap<>();
        for (Probability probability : settings1.getProbabilityWeights().keySet()) {
            int weight = ThreadLocalRandom.current().nextBoolean()
                    ? settings1.getProbabilityWeights().get(probability)
                    : settings2.getProbabilityWeights().get(probability);

            // Apply mutation
            if (ThreadLocalRandom.current().nextInt(100) < 3) {
                weight += ThreadLocalRandom.current().nextInt(-5, 5);
                weight = Math.min(100, Math.max(0, weight));
            }

            combinedProbabilityWeights.put(probability, weight);
        }
        newSettings.setProbabilityWeights(combinedProbabilityWeights);

        Map<ShopItem, Integer> combinedShopItemWeights = new HashMap<>();
        for (ShopItem shopItem : settings1.getShopItemProbabilityWeights().keySet()) {
            int weight = ThreadLocalRandom.current().nextBoolean()
                    ? settings1.getShopItemProbabilityWeights().get(shopItem)
                    : settings2.getShopItemProbabilityWeights().get(shopItem);

            // Apply mutation
            if (ThreadLocalRandom.current().nextInt(100) < 3) {
                weight += ThreadLocalRandom.current().nextInt(-5, 5);
                weight = Math.min(100, Math.max(0, weight));
            }
            combinedShopItemWeights.put(shopItem, weight);
        }
        newSettings.setShopItemProbabilityWeights(combinedShopItemWeights);

        int tooGoodToBeTrueLimit = ThreadLocalRandom.current().nextBoolean()
                ? settings1.getTooGoodToBeTrueLimit()
                : settings2.getTooGoodToBeTrueLimit();
        // Apply mutation
        if (ThreadLocalRandom.current().nextInt(100) < 3) {
            tooGoodToBeTrueLimit += ThreadLocalRandom.current().nextInt(-20, 21);
            tooGoodToBeTrueLimit = Math.min(200, Math.max(0, tooGoodToBeTrueLimit));
        }
        newSettings.setTooGoodToBeTrueLimit(tooGoodToBeTrueLimit);

        return newSettings;
    }

    private void runATrainingRound() {

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < GAMES_IN_ONE_ROUND; i++) {
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
