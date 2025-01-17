package ee.ivar.mugloar.game.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Message {
    private String messageId;
    private int reward;
    private Probability probability;
}
