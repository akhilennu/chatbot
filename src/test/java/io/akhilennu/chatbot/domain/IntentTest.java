package io.akhilennu.chatbot.domain;

import static io.akhilennu.chatbot.domain.BotTestSamples.*;
import static io.akhilennu.chatbot.domain.IntentEntityTestSamples.*;
import static io.akhilennu.chatbot.domain.IntentTestSamples.*;
import static io.akhilennu.chatbot.domain.ResponseDataTestSamples.*;
import static io.akhilennu.chatbot.domain.UtteranceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class IntentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intent.class);
        Intent intent1 = getIntentSample1();
        Intent intent2 = new Intent();
        assertThat(intent1).isNotEqualTo(intent2);

        intent2.setId(intent1.getId());
        assertThat(intent1).isEqualTo(intent2);

        intent2 = getIntentSample2();
        assertThat(intent1).isNotEqualTo(intent2);
    }

    @Test
    void responseDataTest() {
        Intent intent = getIntentRandomSampleGenerator();
        ResponseData responseDataBack = getResponseDataRandomSampleGenerator();

        intent.setResponseData(responseDataBack);
        assertThat(intent.getResponseData()).isEqualTo(responseDataBack);

        intent.responseData(null);
        assertThat(intent.getResponseData()).isNull();
    }

    @Test
    void utterancesTest() {
        Intent intent = getIntentRandomSampleGenerator();
        Utterance utteranceBack = getUtteranceRandomSampleGenerator();

        intent.addUtterances(utteranceBack);
        assertThat(intent.getUtterances()).containsOnly(utteranceBack);
        assertThat(utteranceBack.getIntent()).isEqualTo(intent);

        intent.removeUtterances(utteranceBack);
        assertThat(intent.getUtterances()).doesNotContain(utteranceBack);
        assertThat(utteranceBack.getIntent()).isNull();

        intent.utterances(new HashSet<>(Set.of(utteranceBack)));
        assertThat(intent.getUtterances()).containsOnly(utteranceBack);
        assertThat(utteranceBack.getIntent()).isEqualTo(intent);

        intent.setUtterances(new HashSet<>());
        assertThat(intent.getUtterances()).doesNotContain(utteranceBack);
        assertThat(utteranceBack.getIntent()).isNull();
    }

    @Test
    void intentEntitiesTest() {
        Intent intent = getIntentRandomSampleGenerator();
        IntentEntity intentEntityBack = getIntentEntityRandomSampleGenerator();

        intent.addIntentEntities(intentEntityBack);
        assertThat(intent.getIntentEntities()).containsOnly(intentEntityBack);
        assertThat(intentEntityBack.getIntent()).isEqualTo(intent);

        intent.removeIntentEntities(intentEntityBack);
        assertThat(intent.getIntentEntities()).doesNotContain(intentEntityBack);
        assertThat(intentEntityBack.getIntent()).isNull();

        intent.intentEntities(new HashSet<>(Set.of(intentEntityBack)));
        assertThat(intent.getIntentEntities()).containsOnly(intentEntityBack);
        assertThat(intentEntityBack.getIntent()).isEqualTo(intent);

        intent.setIntentEntities(new HashSet<>());
        assertThat(intent.getIntentEntities()).doesNotContain(intentEntityBack);
        assertThat(intentEntityBack.getIntent()).isNull();
    }

    @Test
    void botTest() {
        Intent intent = getIntentRandomSampleGenerator();
        Bot botBack = getBotRandomSampleGenerator();

        intent.setBot(botBack);
        assertThat(intent.getBot()).isEqualTo(botBack);

        intent.bot(null);
        assertThat(intent.getBot()).isNull();
    }
}
