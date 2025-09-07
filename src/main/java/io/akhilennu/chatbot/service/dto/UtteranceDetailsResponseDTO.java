package io.akhilennu.chatbot.service.dto;

import java.util.List;

public class UtteranceDetailsResponseDTO {

    private List<EntityResponseDTO> entities;
    private IntentResponseDTO intent;

    public List<EntityResponseDTO> getEntities() {
        return entities;
    }

    public void setEntities(List<EntityResponseDTO> entities) {
        this.entities = entities;
    }

    public IntentResponseDTO getIntent() {
        return intent;
    }

    public void setIntent(IntentResponseDTO intent) {
        this.intent = intent;
    }
}
