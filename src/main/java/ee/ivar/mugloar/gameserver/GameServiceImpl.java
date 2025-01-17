package ee.ivar.mugloar.gameserver;

import ee.ivar.mugloar.game.GameService;
import ee.ivar.mugloar.game.domain.GameStartInfo;
import ee.ivar.mugloar.game.domain.Message;
import ee.ivar.mugloar.game.domain.Shop;
import ee.ivar.mugloar.game.domain.ShopItem;
import ee.ivar.mugloar.game.domain.SolveResult;
import ee.ivar.mugloar.gameserver.response.GameStartResponse;
import ee.ivar.mugloar.gameserver.response.ShopResponse;
import ee.ivar.mugloar.gameserver.response.SolveResponse;
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
        return gameHttpClient.getMessages(gameId).stream()
                .map(messageConverter::convertToMessage)
                .toList();
    }

    @Override
    public SolveResult solveMessage(String gameId, Message message) {
        SolveResponse response = gameHttpClient.solve(gameId, message.getMessageId());
        return SolveResult.builder()
                .lives(response.getLives())
                .turn(response.getTurn())
                .success(response.isSuccess())
                .score(response.getScore())
                .gold(response.getGold())
                .build();
    }

    @Override
    public Shop getShop(String gameId) {
        List<ShopResponse> shopResponse = gameHttpClient.getShop(gameId);
        Shop shop = new Shop();
        shop.setItems(shopResponse.stream()
                .map(item -> Shop.Item.builder()
                .name(item.getName())
                .price(item.getCost())
                .type(ShopItem.fromString(item.getId()))
                .build())
                .toList()
        );
        return shop;
    }

    @Override
    public int buyItem(String gameId, String itemId) {
       return gameHttpClient.shopBuy(gameId, itemId).getGold();
    }
}
