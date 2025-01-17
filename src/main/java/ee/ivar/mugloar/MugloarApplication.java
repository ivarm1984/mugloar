package ee.ivar.mugloar;

import ee.ivar.mugloar.game.GameRunner;
import ee.ivar.mugloar.game.strategy.GameSettings;
import ee.ivar.mugloar.game.trainer.GeneticTrainer;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class MugloarApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(MugloarApplication.class, args);

        GameRunner gameRunner = context.getBean(GameRunner.class);
        //gameRunner.runGame(null);

        GameSettings settings = context.getBean(GeneticTrainer.class).runTraining();

        System.out.println(settings);

        int finalScore = gameRunner.runGame(settings);

        log.info("Final score for best player {}", finalScore);

        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }

}
