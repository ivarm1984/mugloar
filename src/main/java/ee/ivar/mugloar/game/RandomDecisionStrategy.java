package ee.ivar.mugloar.game;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDecisionStrategy implements DecisionStrategy {

    @Override
    public Message chooseMessageToSolve(GameState gameState, GameSettings gameSettings) {
        List<Message> messages = gameState.getMessages();
        int randomIndex = ThreadLocalRandom.current().nextInt(messages.size());
        return messages.get(randomIndex);
    }


}
