package io.akhilennu.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.akhilennu.chatbot.domain.enumeration.ConversationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Conversation.
 */
@Entity
@Table(name = "conversation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "conversation_id", nullable = false)
    private UUID conversationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ConversationStatus status;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conversation" }, allowSetters = true)
    private Set<ChatMessage> chatMessages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conversation" }, allowSetters = true)
    private Set<SlotValue> slotValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Conversation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getConversationId() {
        return this.conversationId;
    }

    public Conversation conversationId(UUID conversationId) {
        this.setConversationId(conversationId);
        return this;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public ConversationStatus getStatus() {
        return this.status;
    }

    public Conversation status(ConversationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ConversationStatus status) {
        this.status = status;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public Conversation channelName(String channelName) {
        this.setChannelName(channelName);
        return this;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Conversation startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Conversation endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Set<ChatMessage> getChatMessages() {
        return this.chatMessages;
    }

    public void setChatMessages(Set<ChatMessage> chatMessages) {
        if (this.chatMessages != null) {
            this.chatMessages.forEach(i -> i.setConversation(null));
        }
        if (chatMessages != null) {
            chatMessages.forEach(i -> i.setConversation(this));
        }
        this.chatMessages = chatMessages;
    }

    public Conversation chatMessages(Set<ChatMessage> chatMessages) {
        this.setChatMessages(chatMessages);
        return this;
    }

    public Conversation addChatMessages(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
        chatMessage.setConversation(this);
        return this;
    }

    public Conversation removeChatMessages(ChatMessage chatMessage) {
        this.chatMessages.remove(chatMessage);
        chatMessage.setConversation(null);
        return this;
    }

    public Set<SlotValue> getSlotValues() {
        return this.slotValues;
    }

    public void setSlotValues(Set<SlotValue> slotValues) {
        if (this.slotValues != null) {
            this.slotValues.forEach(i -> i.setConversation(null));
        }
        if (slotValues != null) {
            slotValues.forEach(i -> i.setConversation(this));
        }
        this.slotValues = slotValues;
    }

    public Conversation slotValues(Set<SlotValue> slotValues) {
        this.setSlotValues(slotValues);
        return this;
    }

    public Conversation addSlotValues(SlotValue slotValue) {
        this.slotValues.add(slotValue);
        slotValue.setConversation(this);
        return this;
    }

    public Conversation removeSlotValues(SlotValue slotValue) {
        this.slotValues.remove(slotValue);
        slotValue.setConversation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conversation)) {
            return false;
        }
        return getId() != null && getId().equals(((Conversation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conversation{" +
            "id=" + getId() +
            ", conversationId='" + getConversationId() + "'" +
            ", status='" + getStatus() + "'" +
            ", channelName='" + getChannelName() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}
