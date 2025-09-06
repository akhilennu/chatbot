package io.akhilennu.chatbot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtteranceIntentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtteranceIntentDTO.class);
        UtteranceIntentDTO utteranceIntentDTO1 = new UtteranceIntentDTO();
        utteranceIntentDTO1.setId(1L);
        UtteranceIntentDTO utteranceIntentDTO2 = new UtteranceIntentDTO();
        assertThat(utteranceIntentDTO1).isNotEqualTo(utteranceIntentDTO2);
        utteranceIntentDTO2.setId(utteranceIntentDTO1.getId());
        assertThat(utteranceIntentDTO1).isEqualTo(utteranceIntentDTO2);
        utteranceIntentDTO2.setId(2L);
        assertThat(utteranceIntentDTO1).isNotEqualTo(utteranceIntentDTO2);
        utteranceIntentDTO1.setId(null);
        assertThat(utteranceIntentDTO1).isNotEqualTo(utteranceIntentDTO2);
    }
}
