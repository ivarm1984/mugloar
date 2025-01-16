package ee.ivar.mugloar.game;

public interface DecisionStrategy {
    Message chooseMessageToSolve(GameState gameState, GameSettings gameSettings);
}
