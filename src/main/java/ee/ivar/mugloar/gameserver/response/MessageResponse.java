package ee.ivar.mugloar.gameserver.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponse {

    private String adId;
    private String message;
    private int reward;
    private int expiresIn;
    private String encrypted;
    private String probability;

}
