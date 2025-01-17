package ee.ivar.mugloar.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
@Getter
public enum ShopItem {
    HPOT("hpot"),
    CS("cs"),
    GAS("gas"),
    WAX("wax"),
    TRICKS("tricks"),
    WINGPOT("wingpot"),
    CH("ch"),
    RF("rf"),
    IRON("iron"),
    MTRIX("mtrix"),
    WINGPOTMAX("wingpotmax"),
    UNKNOWN("unknown");

    private String id;

    public static ShopItem fromString(String id) {
        for (ShopItem item : values()) {
            if (id.equalsIgnoreCase(item.id)) {
                return item;
            }
        }
        log.warn("Unknown probability {}", id);
        return UNKNOWN;
    }
}
