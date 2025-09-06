package io.akhilennu.chatbot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.akhilennu.chatbot.domain.SlotValue} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SlotValueDTO implements Serializable {

    private Long id;

    @NotNull
    private String slotName;

    @NotNull
    private String slotValue;

    private ConversationDTO conversation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getSlotValue() {
        return slotValue;
    }

    public void setSlotValue(String slotValue) {
        this.slotValue = slotValue;
    }

    public ConversationDTO getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDTO conversation) {
        this.conversation = conversation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SlotValueDTO)) {
            return false;
        }

        SlotValueDTO slotValueDTO = (SlotValueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, slotValueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SlotValueDTO{" +
            "id=" + getId() +
            ", slotName='" + getSlotName() + "'" +
            ", slotValue='" + getSlotValue() + "'" +
            ", conversation=" + getConversation() +
            "}";
    }
}
