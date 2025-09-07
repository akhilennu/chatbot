package io.akhilennu.chatbot.service.handler;

import io.akhilennu.chatbot.domain.Intent;
import io.akhilennu.chatbot.service.dto.ResponseDataDTO;
import io.akhilennu.chatbot.service.dto.UtteranceDetailsResponseDTO;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("QUESTIONNAIRE_handler")
public class QuestionnaireIntentHandler implements IntentHandler {

    @Override
    public ResponseDataDTO processUserIntent(
        String botId,
        String message,
        UtteranceDetailsResponseDTO details,
        Intent intent,
        Map<String, String> sessionContext
    ) {
        // direct handling logic
        return null;
    }
}
