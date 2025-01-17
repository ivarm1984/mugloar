package ee.ivar.mugloar.gameserver;

import ee.ivar.mugloar.game.domain.Message;
import ee.ivar.mugloar.game.domain.Probability;
import ee.ivar.mugloar.gameserver.response.MessageResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MessageConverterTest {

    private MessageConverter messageConverter = new MessageConverter();

    @Test
    void shouldConvertMessageResponse() {
        // given
        MessageResponse messageResponse = MessageResponse.builder()
                .adId("mess1")
                .probability("Hmmm....")
                .reward(200)
                .build();

        // when
        Message message = messageConverter.convertToMessage(messageResponse);

        // then
        assertThat(message.getMessageId()).isEqualTo("mess1");
        assertThat(message.getProbability()).isEqualTo(Probability.HMM);
        assertThat(message.getReward()).isEqualTo(200);
    }

    @Test
    void shouldConvertMessageResponseWithBase64Values() {
        // given
        MessageResponse messageResponse = MessageResponse.builder()
                .adId("QTF5dHBYOHg=")
                .probability("R2FtYmxl")
                .message("SW52ZXN0aWdhdGUgU2VwdGVtYmVyIFdvb2Ryb3cgYW5kIGZpbmQgb3V0IHRoZWlyIHJlbGF0aW9uIHRvIHRoZSBtYWdpYyBidWNrZXQu")
                .reward(200)
                .build();

        // when
        Message message = messageConverter.convertToMessage(messageResponse);

        // then
        assertThat(message.getMessageId()).isEqualTo("A1ytpX8x");
        assertThat(message.getProbability()).isEqualTo(Probability.GAMBLE);
        assertThat(message.getReward()).isEqualTo(200);
    }

    @Test
    void shouldConvertMessageResponseWithRoti13Values() {
        // given
        MessageResponse messageResponse = MessageResponse.builder()
                .adId("8QADf7nP")
                .probability("Fhvpvqr zvffvba")
                .message("Xvyy Rxv̇a Unapbpx jvgu ohpxrg naq znxr Pnryvan Qrnqzna sebz zbhagnvaf va Qnexsver gb gnxr gur oynzr")
                .reward(200)
                .build();

        // when
        Message message = messageConverter.convertToMessage(messageResponse);

        // then
        assertThat(message.getMessageId()).isEqualTo("8DNQs7aC");
        assertThat(message.getProbability()).isEqualTo(Probability.SUICIDE_MISSION);
        assertThat(message.getReward()).isEqualTo(200);
    }

}