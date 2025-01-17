package ee.ivar.mugloar.gameserver.response;

import lombok.Data;

@Data
public class ShopResponse {
    private String id;
    private String name;
    private int cost;
}
