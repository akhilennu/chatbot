package io.akhilennu.chatbot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SlotValueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlotValueDTO.class);
        SlotValueDTO slotValueDTO1 = new SlotValueDTO();
        slotValueDTO1.setId(1L);
        SlotValueDTO slotValueDTO2 = new SlotValueDTO();
        assertThat(slotValueDTO1).isNotEqualTo(slotValueDTO2);
        slotValueDTO2.setId(slotValueDTO1.getId());
        assertThat(slotValueDTO1).isEqualTo(slotValueDTO2);
        slotValueDTO2.setId(2L);
        assertThat(slotValueDTO1).isNotEqualTo(slotValueDTO2);
        slotValueDTO1.setId(null);
        assertThat(slotValueDTO1).isNotEqualTo(slotValueDTO2);
    }
}
