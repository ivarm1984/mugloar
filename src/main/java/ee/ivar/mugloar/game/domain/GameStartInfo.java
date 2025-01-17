package ee.ivar.mugloar.game.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GameStartInfo {
    private String gameId;
    private int lives;
}
