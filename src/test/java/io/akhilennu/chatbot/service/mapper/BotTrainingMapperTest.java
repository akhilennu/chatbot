package io.akhilennu.chatbot.service.mapper;

import static io.akhilennu.chatbot.domain.BotTrainingAsserts.*;
import static io.akhilennu.chatbot.domain.BotTrainingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BotTrainingMapperTest {

    private BotTrainingMapper botTrainingMapper;

    @BeforeEach
    void setUp() {
        botTrainingMapper = new BotTrainingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBotTrainingSample1();
        var actual = botTrainingMapper.toEntity(botTrainingMapper.toDto(expected));
        assertBotTrainingAllPropertiesEquals(expected, actual);
    }
}
