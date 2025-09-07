package io.akhilennu.chatbot.service.handler;

import io.akhilennu.chatbot.domain.Intent;
import io.akhilennu.chatbot.service.ResponseDataService;
import io.akhilennu.chatbot.service.dto.ResponseDataDTO;
import io.akhilennu.chatbot.service.dto.UtteranceDetailsResponseDTO;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("DIRECT_RESPONSE_handler")
public class DefaultIntentHandler implements IntentHandler {

    ResponseDataService responseDataService;

    public DefaultIntentHandler(ResponseDataService responseDataService) {
        this.responseDataService = responseDataService;
    }

    @Override
    public ResponseDataDTO processUserIntent(
        String botId,
        String message,
        UtteranceDetailsResponseDTO details,
        Intent intent,
        Map<String, String> sessionContext
    ) {
        // direct handling logic
        return responseDataService.findOne(intent.getResponseData().getId()).get();
    }
}
