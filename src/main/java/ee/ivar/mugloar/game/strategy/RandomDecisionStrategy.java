package ee.ivar.mugloar.game.strategy;

import ee.ivar.mugloar.game.GameState;
import ee.ivar.mugloar.game.domain.Message;
import ee.ivar.mugloar.game.domain.Probability;
import ee.ivar.mugloar.game.domain.Shop;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDecisionStrategy implements DecisionStrategy {

    @Override
    public Message chooseMessageToSolve(GameState gameState, GameSettings gameSettings) {
        List<Message> messages = gameState.getMessages().stream()
                .filter(message -> message.getProbability() != Probability.UNKNOWN)
                .toList();
        int randomIndex = ThreadLocalRandom.current().nextInt(messages.size());
        return messages.get(randomIndex);
    }

    @Override
    public Shop.Item chooseItemToBuy(int gold, Shop shop, GameSettings gameSettings) {
        List<Shop.Item> affordableItems = shop.getItems().stream().filter(item -> item.getPrice() <= gold).toList();

        if (affordableItems.isEmpty()) {
            return null;
        }

        return affordableItems.get(ThreadLocalRandom.current().nextInt(affordableItems.size()));
    }

}
