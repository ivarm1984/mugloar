package ee.ivar.mugloar.game.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SolveResult {
    private int lives;
    private int turn;
    private int score;
    private boolean success;
    private int gold;
}
