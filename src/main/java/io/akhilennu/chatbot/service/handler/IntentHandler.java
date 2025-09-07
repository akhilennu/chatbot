package io.akhilennu.chatbot.service.handler;

import io.akhilennu.chatbot.domain.Intent;
import io.akhilennu.chatbot.service.dto.ResponseDataDTO;
import io.akhilennu.chatbot.service.dto.UtteranceDetailsResponseDTO;
import java.util.Map;

public interface IntentHandler {
    ResponseDataDTO processUserIntent(
        String botId,
        String message,
        UtteranceDetailsResponseDTO details,
        Intent intent,
        Map<String, String> sessionContext
    );
}
