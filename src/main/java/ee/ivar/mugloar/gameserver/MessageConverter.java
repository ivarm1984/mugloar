package ee.ivar.mugloar.gameserver;

import ee.ivar.mugloar.game.Message;
import ee.ivar.mugloar.game.Probability;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class MessageConverter {

    public Message convertToMessage(MessageResponse message) {
        MessageResponse decodedResponse = decodeMessage(message);
        return Message.builder()
                .messageId(decodedResponse.getAdId())
                .probability(Probability.fromString(decodedResponse.getProbability()))
                .reward(decodedResponse.getReward())
                .build();
    }

    private MessageResponse decodeMessage(MessageResponse message) {
        if (!isBase64(message.getMessage())) {
            return message;
        }

        return MessageResponse.builder()
                .message(decodeToString(message.getMessage()))
                .adId(decodeToString(message.getAdId()))
                .probability(decodeToString(message.getProbability()))
                .reward(message.getReward())
                .build();
    }

    private static String decodeToString(String input) {
        return new String(Base64.getDecoder().decode(input), UTF_8);
    }

    public static boolean isBase64(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        if (input.length() % 4 != 0) {
            return false;
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(input);
            String encodedString = Base64.getEncoder().encodeToString(decodedBytes);
            return input.equals(encodedString);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
