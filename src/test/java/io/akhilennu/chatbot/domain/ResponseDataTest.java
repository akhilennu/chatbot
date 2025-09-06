package io.akhilennu.chatbot.domain;

import static io.akhilennu.chatbot.domain.IntentEntityTestSamples.*;
import static io.akhilennu.chatbot.domain.IntentTestSamples.*;
import static io.akhilennu.chatbot.domain.ResponseDataTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResponseDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResponseData.class);
        ResponseData responseData1 = getResponseDataSample1();
        ResponseData responseData2 = new ResponseData();
        assertThat(responseData1).isNotEqualTo(responseData2);

        responseData2.setId(responseData1.getId());
        assertThat(responseData1).isEqualTo(responseData2);

        responseData2 = getResponseDataSample2();
        assertThat(responseData1).isNotEqualTo(responseData2);
    }

    @Test
    void intentTest() {
        ResponseData responseData = getResponseDataRandomSampleGenerator();
        Intent intentBack = getIntentRandomSampleGenerator();

        responseData.setIntent(intentBack);
        assertThat(responseData.getIntent()).isEqualTo(intentBack);
        assertThat(intentBack.getResponseData()).isEqualTo(responseData);

        responseData.intent(null);
        assertThat(responseData.getIntent()).isNull();
        assertThat(intentBack.getResponseData()).isNull();
    }

    @Test
    void intentEntityTest() {
        ResponseData responseData = getResponseDataRandomSampleGenerator();
        IntentEntity intentEntityBack = getIntentEntityRandomSampleGenerator();

        responseData.setIntentEntity(intentEntityBack);
        assertThat(responseData.getIntentEntity()).isEqualTo(intentEntityBack);
        assertThat(intentEntityBack.getMissingEntityResponse()).isEqualTo(responseData);

        responseData.intentEntity(null);
        assertThat(responseData.getIntentEntity()).isNull();
        assertThat(intentEntityBack.getMissingEntityResponse()).isNull();
    }
}
