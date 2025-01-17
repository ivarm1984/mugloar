package ee.ivar.mugloar.game;

import ee.ivar.mugloar.game.domain.GameStartInfo;
import ee.ivar.mugloar.game.domain.Message;
import lombok.Data;

import java.util.List;

@Data
public class GameState {
    private String gameId;
    private int lives;
    private int score;
    private int turn;
    private int gold;
    private List<Message> messages;

    public GameState(GameStartInfo gameInfo) {
        this.lives = gameInfo.getLives();
        this.gameId = gameInfo.getGameId();
    }
}
