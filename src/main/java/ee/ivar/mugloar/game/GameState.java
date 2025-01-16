package ee.ivar.mugloar.game;

import lombok.Data;

import java.util.List;

@Data
public class GameState {
    private String gameId;
    private int lives;
    private int score;
    private int turn;
    private List<Message> messages;

    public GameState(GameStartInfo gameInfo) {
        this.lives = gameInfo.getLives();
        this.gameId = gameInfo.getGameId();
    }
}
