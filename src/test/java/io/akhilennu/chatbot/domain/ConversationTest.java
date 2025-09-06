package io.akhilennu.chatbot.domain;

import static io.akhilennu.chatbot.domain.ChatMessageTestSamples.*;
import static io.akhilennu.chatbot.domain.ConversationTestSamples.*;
import static io.akhilennu.chatbot.domain.SlotValueTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ConversationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conversation.class);
        Conversation conversation1 = getConversationSample1();
        Conversation conversation2 = new Conversation();
        assertThat(conversation1).isNotEqualTo(conversation2);

        conversation2.setId(conversation1.getId());
        assertThat(conversation1).isEqualTo(conversation2);

        conversation2 = getConversationSample2();
        assertThat(conversation1).isNotEqualTo(conversation2);
    }

    @Test
    void chatMessagesTest() {
        Conversation conversation = getConversationRandomSampleGenerator();
        ChatMessage chatMessageBack = getChatMessageRandomSampleGenerator();

        conversation.addChatMessages(chatMessageBack);
        assertThat(conversation.getChatMessages()).containsOnly(chatMessageBack);
        assertThat(chatMessageBack.getConversation()).isEqualTo(conversation);

        conversation.removeChatMessages(chatMessageBack);
        assertThat(conversation.getChatMessages()).doesNotContain(chatMessageBack);
        assertThat(chatMessageBack.getConversation()).isNull();

        conversation.chatMessages(new HashSet<>(Set.of(chatMessageBack)));
        assertThat(conversation.getChatMessages()).containsOnly(chatMessageBack);
        assertThat(chatMessageBack.getConversation()).isEqualTo(conversation);

        conversation.setChatMessages(new HashSet<>());
        assertThat(conversation.getChatMessages()).doesNotContain(chatMessageBack);
        assertThat(chatMessageBack.getConversation()).isNull();
    }

    @Test
    void slotValuesTest() {
        Conversation conversation = getConversationRandomSampleGenerator();
        SlotValue slotValueBack = getSlotValueRandomSampleGenerator();

        conversation.addSlotValues(slotValueBack);
        assertThat(conversation.getSlotValues()).containsOnly(slotValueBack);
        assertThat(slotValueBack.getConversation()).isEqualTo(conversation);

        conversation.removeSlotValues(slotValueBack);
        assertThat(conversation.getSlotValues()).doesNotContain(slotValueBack);
        assertThat(slotValueBack.getConversation()).isNull();

        conversation.slotValues(new HashSet<>(Set.of(slotValueBack)));
        assertThat(conversation.getSlotValues()).containsOnly(slotValueBack);
        assertThat(slotValueBack.getConversation()).isEqualTo(conversation);

        conversation.setSlotValues(new HashSet<>());
        assertThat(conversation.getSlotValues()).doesNotContain(slotValueBack);
        assertThat(slotValueBack.getConversation()).isNull();
    }
}
