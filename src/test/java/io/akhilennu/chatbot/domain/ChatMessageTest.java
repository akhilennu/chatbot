package io.akhilennu.chatbot.domain;

import static io.akhilennu.chatbot.domain.ChatMessageTestSamples.*;
import static io.akhilennu.chatbot.domain.ConversationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChatMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessage.class);
        ChatMessage chatMessage1 = getChatMessageSample1();
        ChatMessage chatMessage2 = new ChatMessage();
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);

        chatMessage2.setId(chatMessage1.getId());
        assertThat(chatMessage1).isEqualTo(chatMessage2);

        chatMessage2 = getChatMessageSample2();
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);
    }

    @Test
    void conversationTest() {
        ChatMessage chatMessage = getChatMessageRandomSampleGenerator();
        Conversation conversationBack = getConversationRandomSampleGenerator();

        chatMessage.setConversation(conversationBack);
        assertThat(chatMessage.getConversation()).isEqualTo(conversationBack);

        chatMessage.conversation(null);
        assertThat(chatMessage.getConversation()).isNull();
    }
}
