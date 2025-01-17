package ee.ivar.mugloar;

import ee.ivar.mugloar.game.GameRunner;
import ee.ivar.mugloar.game.strategy.GameSettings;
import ee.ivar.mugloar.game.strategy.RandomDecisionStrategy;
import ee.ivar.mugloar.game.strategy.WeightedDecisionStrategy;
import ee.ivar.mugloar.game.trainer.GeneticTrainer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class MugloarApplication {

    private static final String GAME_MODE_NAIVE = "naive";
    private static final String GAME_MODE_RANDOM = "random";
    private static final String GAME_MODE_PRETRAINED = "pretrained";
    private static final String GAME_MODE_TRAIN = "train";

    @Value("${game.mode:random}")
    private String gameMode;

    public static void main(String[] args) {
        var context = SpringApplication.run(MugloarApplication.class, args);

        GameRunner gameRunner = context.getBean(GameRunner.class);

        switch (context.getBean(MugloarApplication.class).gameMode) {
            case GAME_MODE_NAIVE:
                log.info("Running in Naive mode...");
                gameRunner.runGame(GameSettings.createWithNaiveWeights(), new WeightedDecisionStrategy());
                break;

            case GAME_MODE_RANDOM:
                log.info("Running in Random mode...");
                gameRunner.runGame(GameSettings.createWithRandomWeights(), new RandomDecisionStrategy());
                break;

            case GAME_MODE_PRETRAINED:
                log.info("Running in Pretrained mode...");
                gameRunner.runGame(GameSettings.createWithPretrainedWeights(), new WeightedDecisionStrategy());
                break;

            case GAME_MODE_TRAIN:
                log.info("Running in Training mode...");
                GameSettings settings = context.getBean(GeneticTrainer.class).runTraining();
                int finalScore = gameRunner.runGame(settings, new WeightedDecisionStrategy());
                log.info("Final score for best player {}", finalScore);
                break;
            default:
                log.info("default :(");
                break;
        }
        String mode = System.getProperty("spring.profiles.active");
        if (!"integTest".equalsIgnoreCase(mode)) {
            int exitCode = SpringApplication.exit(context, () -> 0);
            System.exit(exitCode);
        } else {
            System.out.println("Running in integTest mode. Application will not exit.");
        }
    }

}
