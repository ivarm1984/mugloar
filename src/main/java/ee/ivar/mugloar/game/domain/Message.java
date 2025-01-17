package ee.ivar.mugloar.game.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private String messageId;
    private int reward;
    private Probability probability;
}
