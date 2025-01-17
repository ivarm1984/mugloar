package ee.ivar.mugloar.gameserver;

import ee.ivar.mugloar.game.domain.Message;
import ee.ivar.mugloar.game.domain.Probability;
import ee.ivar.mugloar.gameserver.response.MessageResponse;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.stream.Collectors;

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
        if (isBase64(message.getMessage())) {
            return MessageResponse.builder()
                    .message(decodeToString(message.getMessage()))
                    .adId(decodeToString(message.getAdId()))
                    .probability(decodeToString(message.getProbability()))
                    .reward(message.getReward())
                    .build();
        }
        if (isRot13(message.getProbability())) {
            return MessageResponse.builder()
                    .message(rot13(message.getMessage()))
                    .adId(rot13(message.getAdId()))
                    .probability(rot13(message.getProbability()))
                    .reward(message.getReward())
                    .build();
        }

        return message;
    }

    public static boolean isRot13(String input) {
        String decoded = rot13(input);

        return Probability.fromString(decoded) != Probability.UNKNOWN;
    }

    private static String rot13(String input) {
        return input.chars()
                .map(c -> {
                    if ('A' <= c && c <= 'Z') {
                        return 'A' + (c - 'A' + 13) % 26;
                    } else if ('a' <= c && c <= 'z') {
                        return 'a' + (c - 'a' + 13) % 26;
                    } else {
                        return c; // Non-alphabetic characters are unchanged
                    }
                })
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
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
