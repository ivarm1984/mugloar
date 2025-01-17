package ee.ivar.mugloar.game.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Shop {

    private List<Item> items;

    @Data
    @Builder
    @AllArgsConstructor
    public static class Item {
        private ShopItem type;
        private int price;
        private String name;
    }
}
