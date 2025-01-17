package ee.ivar.mugloar.game;

import ee.ivar.mugloar.game.domain.GameStartInfo;
import ee.ivar.mugloar.game.domain.Message;
import ee.ivar.mugloar.game.domain.Shop;
import ee.ivar.mugloar.game.domain.SolveResult;

import java.util.List;

public interface GameService {
    GameStartInfo startGame();

    List<Message> getMessages(String gameId);

    SolveResult solveMessage(String gameId, Message message);

    Shop getShop(String gameId);

    int buyItem(String gameId, String itemId);
}
