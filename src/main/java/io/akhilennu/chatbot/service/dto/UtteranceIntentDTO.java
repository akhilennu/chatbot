package io.akhilennu.chatbot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.akhilennu.chatbot.domain.UtteranceIntent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtteranceIntentDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer startIndex;

    @NotNull
    private Integer endIndex;

    private UtteranceDTO utterance;

    private IntentEntityDTO intentEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public UtteranceDTO getUtterance() {
        return utterance;
    }

    public void setUtterance(UtteranceDTO utterance) {
        this.utterance = utterance;
    }

    public IntentEntityDTO getIntentEntity() {
        return intentEntity;
    }

    public void setIntentEntity(IntentEntityDTO intentEntity) {
        this.intentEntity = intentEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtteranceIntentDTO)) {
            return false;
        }

        UtteranceIntentDTO utteranceIntentDTO = (UtteranceIntentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utteranceIntentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtteranceIntentDTO{" +
            "id=" + getId() +
            ", startIndex=" + getStartIndex() +
            ", endIndex=" + getEndIndex() +
            ", utterance=" + getUtterance() +
            ", intentEntity=" + getIntentEntity() +
            "}";
    }
}
