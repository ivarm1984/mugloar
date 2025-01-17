package ee.ivar.mugloar.game;

import ee.ivar.mugloar.game.domain.GameStartInfo;
import ee.ivar.mugloar.game.domain.Message;
import ee.ivar.mugloar.game.domain.Shop;
import ee.ivar.mugloar.game.domain.SolveResult;
import ee.ivar.mugloar.game.strategy.GameSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class GameRunner {

    private static GameSettings SETTINGS = GameSettings.createWithNaiveWeights();

    private final GameService gameApi;

    public int runGame(GameSettings settings) {
        if (settings != null) {
            SETTINGS = settings;
        }
        GameStartInfo gameInfo = gameApi.startGame();
        log.info("Starting game {}", gameInfo.getGameId());

        GameState gameState = new GameState(gameInfo);
        while (hasLives(gameState)) {
            playARound(gameState);
        }

        log.info("Game: {} ended on turn: {} with a score of: {} settings: {}",
                gameState.getGameId(), gameState.getTurn(), gameState.getScore(), settings);
        return gameState.getScore();
    }

    private void playARound(GameState gameState) {
        tryToBuy(gameState);

        List<Message> messages = gameApi.getMessages(gameState.getGameId());
        gameState.setMessages(messages);

        Message messageToSolve = SETTINGS.getDecisionStrategy().chooseMessageToSolve(gameState, SETTINGS);
        log.debug("Chose message with risk: {} and reward: {}", messageToSolve.getProbability(), messageToSolve.getReward());

        SolveResult result = gameApi.solveMessage(gameState.getGameId(), messageToSolve);
        log.debug("Result is: {} score: {}", result.isSuccess(), result.getScore());
        gameState.setLives(result.getLives());
        gameState.setTurn(result.getTurn());
        gameState.setScore(result.getScore());
        gameState.setGold(result.getGold());
    }

    private void tryToBuy(GameState gameState) {
        Shop shop = gameApi.getShop(gameState.getGameId());
        Shop.Item itemToBuy = SETTINGS.getDecisionStrategy().chooseItemToBuy(gameState.getGold(), shop, SETTINGS);
        if (itemToBuy != null) {
            int remainingGold = gameApi.buyItem(gameState.getGameId(), itemToBuy.getType().getId());
            gameState.setGold(remainingGold);
        }
    }

    private boolean hasLives(GameState gameState) {
        return gameState.getLives() != 0;
    }
}
