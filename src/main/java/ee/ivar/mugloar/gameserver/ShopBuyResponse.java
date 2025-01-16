package ee.ivar.mugloar.gameserver;

import lombok.Data;

@Data
public class ShopBuyResponse {
    private boolean shoppingSuccess;
    private int gold;
    private int lives;
    private int level;
    private int turn;
}
