package io.akhilennu.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ResponseData.
 */
@Entity
@Table(name = "response_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResponseData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "channel_name")
    private String channelName;

    @JsonIgnoreProperties(value = { "responseData", "utterances", "intentEntities", "bot" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "responseData")
    private Intent intent;

    @JsonIgnoreProperties(value = { "missingEntityResponse", "intent" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "missingEntityResponse")
    private IntentEntity intentEntity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ResponseData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public ResponseData type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public ResponseData content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public ResponseData channelName(String channelName) {
        this.setChannelName(channelName);
        return this;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void setIntent(Intent intent) {
        if (this.intent != null) {
            this.intent.setResponseData(null);
        }
        if (intent != null) {
            intent.setResponseData(this);
        }
        this.intent = intent;
    }

    public ResponseData intent(Intent intent) {
        this.setIntent(intent);
        return this;
    }

    public IntentEntity getIntentEntity() {
        return this.intentEntity;
    }

    public void setIntentEntity(IntentEntity intentEntity) {
        if (this.intentEntity != null) {
            this.intentEntity.setMissingEntityResponse(null);
        }
        if (intentEntity != null) {
            intentEntity.setMissingEntityResponse(this);
        }
        this.intentEntity = intentEntity;
    }

    public ResponseData intentEntity(IntentEntity intentEntity) {
        this.setIntentEntity(intentEntity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponseData)) {
            return false;
        }
        return getId() != null && getId().equals(((ResponseData) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResponseData{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", content='" + getContent() + "'" +
            ", channelName='" + getChannelName() + "'" +
            "}";
    }
}
