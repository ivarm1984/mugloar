package ee.ivar.mugloar.game.strategy;

import ee.ivar.mugloar.game.GameState;
import ee.ivar.mugloar.game.domain.Message;
import ee.ivar.mugloar.game.domain.Probability;
import ee.ivar.mugloar.game.domain.Shop;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Log4j2
public class WeightedDecisionStrategy implements DecisionStrategy {

    @Override
    public Message chooseMessageToSolve(GameState gameState, GameSettings gameSettings) {
        List<Message> messages = gameState.getMessages();

        Map<Message, Integer> messageValues = messages.stream()
                .collect(Collectors.toMap(
                        message -> message,
                        message -> calculateValue(message, gameSettings)
                ));

        return messageValues.entrySet()
                .stream()
                .filter(entry -> entry.getKey().getProbability() != Probability.UNKNOWN)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private static int calculateValue(Message message, GameSettings gameSettings) {
        int reward = message.getReward();
        int weight = gameSettings.getProbabilityWeights().getOrDefault(message.getProbability(), 0);
        int value = reward + weight;
        if (reward > gameSettings.getTooGoodToBeTrueLimit()) {
            log.debug("Too good to be true: {} {}", message.getReward(),  value);
            value -= gameSettings.getTooGoodToBeTrueLimit();
        }
        return value;
    }

    @Override
    public Shop.Item chooseItemToBuy(int gold, Shop shop, GameSettings gameSettings) {
        List<Shop.Item> affordableItems = shop.getItems().stream().filter(item -> item.getPrice() <= gold).toList();

        if (affordableItems.isEmpty()) {
            return null;
        }

        int totalWeight = affordableItems.stream()
                .mapToInt(item -> gameSettings.getShopItemProbabilityWeights().get(item.getType()))
                .sum();

        int randomValue = ThreadLocalRandom.current().nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (Shop.Item item : affordableItems) {
            cumulativeWeight += gameSettings.getShopItemProbabilityWeights().get(item.getType());
            if (randomValue < cumulativeWeight) {
                return item;
            }
        }

        return null;
    }
}