package ee.ivar.mugloar.gameserver;

import ee.ivar.mugloar.game.GameService;
import ee.ivar.mugloar.game.GameStartInfo;
import ee.ivar.mugloar.game.Message;
import ee.ivar.mugloar.game.SolveResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameHttpClient gameHttpClient;
    private final MessageConverter messageConverter;

    @Override
    public GameStartInfo startGame() {
        GameStartResponse response = gameHttpClient.startGame();
        return GameStartInfo.builder()
                .gameId(response.getGameId())
                .lives(response.getLives())
                .build();
    }

    @Override
    public List<Message> getMessages(String gameId) {
        return gameHttpClient.getMessages(gameId).stream().map(messageConverter::convertToMessage).toList();
    }

    @Override
    public SolveResult solveMessage(String gameId, Message message) {
        SolveResponse response = gameHttpClient.solve(gameId, message.getMessageId());
        return SolveResult.builder()
                .lives(response.getLives())
                .turn(response.getTurn())
                .success(response.isSuccess())
                .score(response.getScore())
                .build();
    }
}
