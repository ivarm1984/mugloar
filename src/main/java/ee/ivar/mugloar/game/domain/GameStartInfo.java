package ee.ivar.mugloar.game.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameStartInfo {
    private String gameId;
    private int lives;
}
