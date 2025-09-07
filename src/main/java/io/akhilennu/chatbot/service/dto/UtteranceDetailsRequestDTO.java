package io.akhilennu.chatbot.service.dto;

public class UtteranceDetailsRequestDTO {

    private Long botId;
    private String sentence;

    public Long getBotId() {
        return botId;
    }

    public void setBotId(Long botId) {
        this.botId = botId;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
