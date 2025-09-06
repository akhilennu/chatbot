package io.akhilennu.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UtteranceIntent.
 */
@Entity
@Table(name = "utterance_intent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtteranceIntent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_index", nullable = false)
    private Integer startIndex;

    @NotNull
    @Column(name = "end_index", nullable = false)
    private Integer endIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "intent" }, allowSetters = true)
    private Utterance utterance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "missingEntityResponse", "intent" }, allowSetters = true)
    private IntentEntity intentEntity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UtteranceIntent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartIndex() {
        return this.startIndex;
    }

    public UtteranceIntent startIndex(Integer startIndex) {
        this.setStartIndex(startIndex);
        return this;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return this.endIndex;
    }

    public UtteranceIntent endIndex(Integer endIndex) {
        this.setEndIndex(endIndex);
        return this;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public Utterance getUtterance() {
        return this.utterance;
    }

    public void setUtterance(Utterance utterance) {
        this.utterance = utterance;
    }

    public UtteranceIntent utterance(Utterance utterance) {
        this.setUtterance(utterance);
        return this;
    }

    public IntentEntity getIntentEntity() {
        return this.intentEntity;
    }

    public void setIntentEntity(IntentEntity intentEntity) {
        this.intentEntity = intentEntity;
    }

    public UtteranceIntent intentEntity(IntentEntity intentEntity) {
        this.setIntentEntity(intentEntity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtteranceIntent)) {
            return false;
        }
        return getId() != null && getId().equals(((UtteranceIntent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtteranceIntent{" +
            "id=" + getId() +
            ", startIndex=" + getStartIndex() +
            ", endIndex=" + getEndIndex() +
            "}";
    }
}
