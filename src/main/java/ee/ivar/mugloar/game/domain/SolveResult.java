package ee.ivar.mugloar.game.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class SolveResult {
    private int lives;
    private int turn;
    private int score;
    private boolean success;
    private int gold;
}
