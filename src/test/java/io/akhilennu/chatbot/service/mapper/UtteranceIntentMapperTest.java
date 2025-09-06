package io.akhilennu.chatbot.service.mapper;

import static io.akhilennu.chatbot.domain.UtteranceIntentAsserts.*;
import static io.akhilennu.chatbot.domain.UtteranceIntentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtteranceIntentMapperTest {

    private UtteranceIntentMapper utteranceIntentMapper;

    @BeforeEach
    void setUp() {
        utteranceIntentMapper = new UtteranceIntentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUtteranceIntentSample1();
        var actual = utteranceIntentMapper.toEntity(utteranceIntentMapper.toDto(expected));
        assertUtteranceIntentAllPropertiesEquals(expected, actual);
    }
}
