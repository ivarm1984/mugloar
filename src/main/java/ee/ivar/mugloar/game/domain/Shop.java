package ee.ivar.mugloar.game.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class Shop {

    private List<Item> items;

    @Data
    @Builder
    public static class Item {
        private ShopItem type;
        private int price;
        private String name;
    }
}
