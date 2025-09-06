package io.akhilennu.chatbot.service.mapper;

import static io.akhilennu.chatbot.domain.SlotValueAsserts.*;
import static io.akhilennu.chatbot.domain.SlotValueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SlotValueMapperTest {

    private SlotValueMapper slotValueMapper;

    @BeforeEach
    void setUp() {
        slotValueMapper = new SlotValueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSlotValueSample1();
        var actual = slotValueMapper.toEntity(slotValueMapper.toDto(expected));
        assertSlotValueAllPropertiesEquals(expected, actual);
    }
}
