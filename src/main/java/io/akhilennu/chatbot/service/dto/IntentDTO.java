package io.akhilennu.chatbot.service.dto;

import io.akhilennu.chatbot.domain.enumeration.IntentRespType;
import io.akhilennu.chatbot.domain.enumeration.IntentType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.akhilennu.chatbot.domain.Intent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntentDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private IntentType type;

    private IntentRespType respType;

    private ResponseDataDTO responseData;

    private Set<IntentEntityDTO> intentEntities;

    private BotDTO bot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IntentType getType() {
        return type;
    }

    public void setType(IntentType type) {
        this.type = type;
    }

    public IntentRespType getRespType() {
        return respType;
    }

    public void setRespType(IntentRespType respType) {
        this.respType = respType;
    }

    public ResponseDataDTO getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseDataDTO responseData) {
        this.responseData = responseData;
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
        if (!(o instanceof IntentDTO)) {
            return false;
        }

        IntentDTO intentDTO = (IntentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, intentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", respType='" + getRespType() + "'" +
            ", responseData=" + getResponseData() +
            ", bot=" + getBot() +
            "}";
    }

    public Set<IntentEntityDTO> getIntentEntities() {
        return intentEntities;
    }

    public void setIntentEntities(Set<IntentEntityDTO> intentEntities) {
        this.intentEntities = intentEntities;
    }
}
