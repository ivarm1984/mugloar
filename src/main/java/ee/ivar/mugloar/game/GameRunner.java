package ee.ivar.mugloar.game;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class GameRunner {

    private static final DecisionStrategy STRATEGY = new WeightedDecisionStrategy();
    private static final GameSettings SETTINGS = GameSettings.createWithNaiveWeights();

    private final GameService gameApi;

    @PostConstruct
    public void runGame() {


        GameStartInfo gameInfo = gameApi.startGame();
        log.info("Starting game {}", gameInfo.getGameId());

        GameState gameState = new GameState(gameInfo);
        while (hasLives(gameState)) {
            playARound(gameState);
        }

        log.info("Game ended on turn: {} with a score of: {}", gameState.getTurn(), gameState.getScore());

    }

    private void playARound(GameState gameState) {
        List<Message> messages = gameApi.getMessages(gameState.getGameId());
        gameState.setMessages(messages);

        Message messageToSolve = STRATEGY.chooseMessageToSolve(gameState, SETTINGS);
        log.info("Chose message with risk: {} and reward: {}", messageToSolve.getProbability(), messageToSolve.getReward());
        SolveResult result = gameApi.solveMessage(gameState.getGameId(), messageToSolve);
        log.info("Result is: {} score: {}", result.isSuccess(), result.getScore());
        gameState.setLives(result.getLives());
        gameState.setTurn(result.getTurn());
        gameState.setScore(result.getScore());
    }

    private boolean hasLives(GameState gameState) {
        return gameState.getLives() != 0;
    }
}
