package io.akhilennu.chatbot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link io.akhilennu.chatbot.domain.BotTraining} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BotTrainingDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant trainingTime;

    private Double accuracy;

    private Double precision;

    private Double f1Score;

    private Boolean active;

    private BotDTO bot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(Instant trainingTime) {
        this.trainingTime = trainingTime;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Double getPrecision() {
        return precision;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    public Double getf1Score() {
        return f1Score;
    }

    public void setf1Score(Double f1Score) {
        this.f1Score = f1Score;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BotDTO getBot() {
        return bot;
    }

    public void setBot(BotDTO bot) {
        this.bot = bot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BotTrainingDTO)) {
            return false;
        }

        BotTrainingDTO botTrainingDTO = (BotTrainingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, botTrainingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BotTrainingDTO{" +
            "id=" + getId() +
            ", trainingTime='" + getTrainingTime() + "'" +
            ", accuracy=" + getAccuracy() +
            ", precision=" + getPrecision() +
            ", f1Score=" + getf1Score() +
            ", active='" + getActive() + "'" +
            ", bot=" + getBot() +
            "}";
    }
}
