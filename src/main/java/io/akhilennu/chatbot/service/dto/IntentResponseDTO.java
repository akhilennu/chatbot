package io.akhilennu.chatbot.service.dto;

import java.util.List;

public class IntentResponseDTO {

    private String label;
    private double confidence;
    private List<IntentTop3DTO> top3;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public List<IntentTop3DTO> getTop3() {
        return top3;
    }

    public void setTop3(List<IntentTop3DTO> top3) {
        this.top3 = top3;
    }
}
