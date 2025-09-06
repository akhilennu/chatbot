package io.akhilennu.chatbot.service.mapper;

import static io.akhilennu.chatbot.domain.ResponseDataAsserts.*;
import static io.akhilennu.chatbot.domain.ResponseDataTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResponseDataMapperTest {

    private ResponseDataMapper responseDataMapper;

    @BeforeEach
    void setUp() {
        responseDataMapper = new ResponseDataMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getResponseDataSample1();
        var actual = responseDataMapper.toEntity(responseDataMapper.toDto(expected));
        assertResponseDataAllPropertiesEquals(expected, actual);
    }
}
