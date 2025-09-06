package io.akhilennu.chatbot.domain;

import static io.akhilennu.chatbot.domain.ConversationTestSamples.*;
import static io.akhilennu.chatbot.domain.SlotValueTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SlotValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlotValue.class);
        SlotValue slotValue1 = getSlotValueSample1();
        SlotValue slotValue2 = new SlotValue();
        assertThat(slotValue1).isNotEqualTo(slotValue2);

        slotValue2.setId(slotValue1.getId());
        assertThat(slotValue1).isEqualTo(slotValue2);

        slotValue2 = getSlotValueSample2();
        assertThat(slotValue1).isNotEqualTo(slotValue2);
    }

    @Test
    void conversationTest() {
        SlotValue slotValue = getSlotValueRandomSampleGenerator();
        Conversation conversationBack = getConversationRandomSampleGenerator();

        slotValue.setConversation(conversationBack);
        assertThat(slotValue.getConversation()).isEqualTo(conversationBack);

        slotValue.conversation(null);
        assertThat(slotValue.getConversation()).isNull();
    }
}
