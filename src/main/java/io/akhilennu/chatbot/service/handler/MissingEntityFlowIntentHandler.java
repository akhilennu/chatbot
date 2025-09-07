package io.akhilennu.chatbot.service.handler;

import io.akhilennu.chatbot.domain.Intent;
import io.akhilennu.chatbot.domain.IntentEntity;
import io.akhilennu.chatbot.service.ResponseDataService;
import io.akhilennu.chatbot.service.dto.EntityResponseDTO;
import io.akhilennu.chatbot.service.dto.ResponseDataDTO;
import io.akhilennu.chatbot.service.dto.UtteranceDetailsResponseDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component("MISSING_ENTITY_FLOW_handler")
public class MissingEntityFlowIntentHandler implements IntentHandler {

    private ResponseDataService responseDataService;

    public MissingEntityFlowIntentHandler(ResponseDataService responseDataService) {
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
        Set<IntentEntity> intentNeededEntities = intent.getIntentEntities();
        List<EntityResponseDTO> entitiesFound = details.getEntities();
        entitiesFound.forEach(e -> sessionContext.put(e.getLabel(), e.getText()));
        IntentEntity missingEntity = null;
        for (IntentEntity entity : intentNeededEntities) {
            if (!sessionContext.containsKey(entity.getEntityName())) {
                missingEntity = entity;
                break;
            }
        }
        if (missingEntity != null) {
            sessionContext.put("ACTIVE_INTENT", intent.getName());
            return responseDataService.findOne(missingEntity.getMissingEntityResponse().getId()).get();
        }
        sessionContext.remove("ACTIVE_INTENT");
        return responseDataService.findOne(intent.getResponseData().getId()).get();
    }
}
