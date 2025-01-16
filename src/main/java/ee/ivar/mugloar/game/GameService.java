package ee.ivar.mugloar.game;

import java.util.List;

public interface GameService {
    GameStartInfo startGame();

    List<Message> getMessages(String gameId);

    SolveResult solveMessage(String gameId, Message message);
}
