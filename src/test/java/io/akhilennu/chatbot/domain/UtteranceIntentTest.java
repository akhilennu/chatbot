package io.akhilennu.chatbot.domain;

import static io.akhilennu.chatbot.domain.IntentEntityTestSamples.*;
import static io.akhilennu.chatbot.domain.UtteranceIntentTestSamples.*;
import static io.akhilennu.chatbot.domain.UtteranceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtteranceIntentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtteranceIntent.class);
        UtteranceIntent utteranceIntent1 = getUtteranceIntentSample1();
        UtteranceIntent utteranceIntent2 = new UtteranceIntent();
        assertThat(utteranceIntent1).isNotEqualTo(utteranceIntent2);

        utteranceIntent2.setId(utteranceIntent1.getId());
        assertThat(utteranceIntent1).isEqualTo(utteranceIntent2);

        utteranceIntent2 = getUtteranceIntentSample2();
        assertThat(utteranceIntent1).isNotEqualTo(utteranceIntent2);
    }

    @Test
    void utteranceTest() {
        UtteranceIntent utteranceIntent = getUtteranceIntentRandomSampleGenerator();
        Utterance utteranceBack = getUtteranceRandomSampleGenerator();

        utteranceIntent.setUtterance(utteranceBack);
        assertThat(utteranceIntent.getUtterance()).isEqualTo(utteranceBack);

        utteranceIntent.utterance(null);
        assertThat(utteranceIntent.getUtterance()).isNull();
    }

    @Test
    void intentEntityTest() {
        UtteranceIntent utteranceIntent = getUtteranceIntentRandomSampleGenerator();
        IntentEntity intentEntityBack = getIntentEntityRandomSampleGenerator();

        utteranceIntent.setIntentEntity(intentEntityBack);
        assertThat(utteranceIntent.getIntentEntity()).isEqualTo(intentEntityBack);

        utteranceIntent.intentEntity(null);
        assertThat(utteranceIntent.getIntentEntity()).isNull();
    }
}
