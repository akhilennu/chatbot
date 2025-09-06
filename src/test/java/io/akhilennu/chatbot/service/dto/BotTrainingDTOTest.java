package io.akhilennu.chatbot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BotTrainingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BotTrainingDTO.class);
        BotTrainingDTO botTrainingDTO1 = new BotTrainingDTO();
        botTrainingDTO1.setId(1L);
        BotTrainingDTO botTrainingDTO2 = new BotTrainingDTO();
        assertThat(botTrainingDTO1).isNotEqualTo(botTrainingDTO2);
        botTrainingDTO2.setId(botTrainingDTO1.getId());
        assertThat(botTrainingDTO1).isEqualTo(botTrainingDTO2);
        botTrainingDTO2.setId(2L);
        assertThat(botTrainingDTO1).isNotEqualTo(botTrainingDTO2);
        botTrainingDTO1.setId(null);
        assertThat(botTrainingDTO1).isNotEqualTo(botTrainingDTO2);
    }
}
