package ee.ivar.mugloar.gameserver;

import lombok.Data;

@Data
public class GameStartResponse {

    private String gameId;
    private int lives;
    private int gold;
    private int level;
    private int score;
    private int highScore;
    private int turn;
}
