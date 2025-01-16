package ee.ivar.mugloar.game;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameStartInfo {
    private String gameId;
    private int lives;
}
