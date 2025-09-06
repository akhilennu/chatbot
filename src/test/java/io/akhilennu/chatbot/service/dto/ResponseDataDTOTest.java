package io.akhilennu.chatbot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResponseDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResponseDataDTO.class);
        ResponseDataDTO responseDataDTO1 = new ResponseDataDTO();
        responseDataDTO1.setId(1L);
        ResponseDataDTO responseDataDTO2 = new ResponseDataDTO();
        assertThat(responseDataDTO1).isNotEqualTo(responseDataDTO2);
        responseDataDTO2.setId(responseDataDTO1.getId());
        assertThat(responseDataDTO1).isEqualTo(responseDataDTO2);
        responseDataDTO2.setId(2L);
        assertThat(responseDataDTO1).isNotEqualTo(responseDataDTO2);
        responseDataDTO1.setId(null);
        assertThat(responseDataDTO1).isNotEqualTo(responseDataDTO2);
    }
}
