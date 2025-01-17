package ee.ivar.mugloar.gameserver.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopResponse {
    private String id;
    private String name;
    private int cost;
}
