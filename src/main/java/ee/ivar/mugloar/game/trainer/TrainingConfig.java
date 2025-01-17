package ee.ivar.mugloar.game.trainer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "training")
public class TrainingConfig {
    private int numberOfParallelGames;
    private int trainingRounds;
    private int gamesInOneRound;
    private int playerCount;
    private int numberOfSurvivors;
    private double mutationChancePercentage;
}