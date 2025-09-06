package io.akhilennu.chatbot.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.akhilennu.chatbot.domain.IntentEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntentEntityDTO implements Serializable {

    private Long id;

    private String entityName;

    private ResponseDataDTO missingEntityResponse;

    private IntentDTO intent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public ResponseDataDTO getMissingEntityResponse() {
        return missingEntityResponse;
    }

    public void setMissingEntityResponse(ResponseDataDTO missingEntityResponse) {
        this.missingEntityResponse = missingEntityResponse;
    }

    public IntentDTO getIntent() {
        return intent;
    }

    public void setIntent(IntentDTO intent) {
        this.intent = intent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntentEntityDTO)) {
            return false;
        }

        IntentEntityDTO intentEntityDTO = (IntentEntityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, intentEntityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntentEntityDTO{" +
            "id=" + getId() +
            ", entityName='" + getEntityName() + "'" +
            ", missingEntityResponse=" + getMissingEntityResponse() +
            ", intent=" + getIntent() +
            "}";
    }
}
