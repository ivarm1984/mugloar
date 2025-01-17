package ee.ivar.mugloar.game.trainer;

import ee.ivar.mugloar.game.strategy.GameSettings;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {
    private int score;
    private GameSettings settings;
}
