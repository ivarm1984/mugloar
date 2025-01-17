package ee.ivar.mugloar.gameserver.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolveResponse {
    private boolean success;
    private int lives;
    private int gold;
    private int score;
    private int highScore;
    private int turn;
    private String message;
}
