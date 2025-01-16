package ee.ivar.mugloar.game;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WeightedDecisionStrategy implements DecisionStrategy {

    @Override
    public Message chooseMessageToSolve(GameState gameState, GameSettings gameSettings) {
        List<Message> messages = gameState.getMessages();
        Map<Probability, Integer> probabilityWeights = gameSettings.getProbabilityWeights();

        Map<Message, Integer> messageValues = messages.stream()
                .collect(Collectors.toMap(
                        message -> message,
                        message -> calculateValue(message, probabilityWeights)
                ));

        return  messageValues.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private static int calculateValue(Message message, Map<Probability, Integer> probabilityWeights) {
        int reward = message.getReward();
        int weight = probabilityWeights.getOrDefault(message.getProbability(), 0);
        return reward + weight;
    }
}