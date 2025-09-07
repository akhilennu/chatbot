package io.akhilennu.chatbot.service.dto;

public class IntentTop3DTO {

    private String label;
    private double confidence;

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
}
