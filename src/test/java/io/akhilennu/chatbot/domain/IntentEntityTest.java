package io.akhilennu.chatbot.domain;

import static io.akhilennu.chatbot.domain.IntentEntityTestSamples.*;
import static io.akhilennu.chatbot.domain.IntentTestSamples.*;
import static io.akhilennu.chatbot.domain.ResponseDataTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntentEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntentEntity.class);
        IntentEntity intentEntity1 = getIntentEntitySample1();
        IntentEntity intentEntity2 = new IntentEntity();
        assertThat(intentEntity1).isNotEqualTo(intentEntity2);

        intentEntity2.setId(intentEntity1.getId());
        assertThat(intentEntity1).isEqualTo(intentEntity2);

        intentEntity2 = getIntentEntitySample2();
        assertThat(intentEntity1).isNotEqualTo(intentEntity2);
    }

    @Test
    void missingEntityResponseTest() {
        IntentEntity intentEntity = getIntentEntityRandomSampleGenerator();
        ResponseData responseDataBack = getResponseDataRandomSampleGenerator();

        intentEntity.setMissingEntityResponse(responseDataBack);
        assertThat(intentEntity.getMissingEntityResponse()).isEqualTo(responseDataBack);

        intentEntity.missingEntityResponse(null);
        assertThat(intentEntity.getMissingEntityResponse()).isNull();
    }

    @Test
    void intentTest() {
        IntentEntity intentEntity = getIntentEntityRandomSampleGenerator();
        Intent intentBack = getIntentRandomSampleGenerator();

        intentEntity.setIntent(intentBack);
        assertThat(intentEntity.getIntent()).isEqualTo(intentBack);

        intentEntity.intent(null);
        assertThat(intentEntity.getIntent()).isNull();
    }
}
