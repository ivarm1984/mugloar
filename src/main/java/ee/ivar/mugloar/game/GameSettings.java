package ee.ivar.mugloar.game;

import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Data
public class GameSettings {

    private final static Random random = new Random();

    private Map<Probability, Integer> probabilityWeights;

    public static GameSettings createWithRandomWeights() {
        GameSettings gameSettings = new GameSettings();

        gameSettings.probabilityWeights = Arrays.stream(Probability.values())
                .collect(Collectors.toMap(
                        probability -> probability,
                        probability -> random.nextInt(100)
                ));
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

        return gameSettings;
    }
}
