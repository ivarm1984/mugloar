package ee.ivar.mugloar.game.strategy;

import ee.ivar.mugloar.game.GameState;
import ee.ivar.mugloar.game.domain.Message;
import ee.ivar.mugloar.game.domain.Shop;

public interface DecisionStrategy {
    Message chooseMessageToSolve(GameState gameState, GameSettings gameSettings);

    Shop.Item chooseItemToBuy(int gold, Shop shop, GameSettings gameSettings);
}
