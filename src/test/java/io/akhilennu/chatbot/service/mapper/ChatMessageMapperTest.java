package io.akhilennu.chatbot.service.mapper;

import static io.akhilennu.chatbot.domain.ChatMessageAsserts.*;
import static io.akhilennu.chatbot.domain.ChatMessageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatMessageMapperTest {

    private ChatMessageMapper chatMessageMapper;

    @BeforeEach
    void setUp() {
        chatMessageMapper = new ChatMessageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChatMessageSample1();
        var actual = chatMessageMapper.toEntity(chatMessageMapper.toDto(expected));
        assertChatMessageAllPropertiesEquals(expected, actual);
    }
}
