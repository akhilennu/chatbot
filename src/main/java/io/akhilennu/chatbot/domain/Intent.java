package io.akhilennu.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.akhilennu.chatbot.domain.enumeration.IntentRespType;
import io.akhilennu.chatbot.domain.enumeration.IntentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Intent.
 */
@Entity
@Table(name = "intent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Intent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private IntentType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "resp_type")
    private IntentRespType respType;

    @JsonIgnoreProperties(value = { "intent", "intentEntity" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ResponseData responseData;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "intent" }, allowSetters = true)
    private Set<Utterance> utterances = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "missingEntityResponse", "intent" }, allowSetters = true)
    private Set<IntentEntity> intentEntities = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "intents", "botTrainings" }, allowSetters = true)
    private Bot bot;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Intent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Intent name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IntentType getType() {
        return this.type;
    }

    public Intent type(IntentType type) {
        this.setType(type);
        return this;
    }

    public void setType(IntentType type) {
        this.type = type;
    }

    public IntentRespType getRespType() {
        return this.respType;
    }

    public Intent respType(IntentRespType respType) {
        this.setRespType(respType);
        return this;
    }

    public void setRespType(IntentRespType respType) {
        this.respType = respType;
    }

    public ResponseData getResponseData() {
        return this.responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public Intent responseData(ResponseData responseData) {
        this.setResponseData(responseData);
        return this;
    }

    public Set<Utterance> getUtterances() {
        return this.utterances;
    }

    public void setUtterances(Set<Utterance> utterances) {
        if (this.utterances != null) {
            this.utterances.forEach(i -> i.setIntent(null));
        }
        if (utterances != null) {
            utterances.forEach(i -> i.setIntent(this));
        }
        this.utterances = utterances;
    }

    public Intent utterances(Set<Utterance> utterances) {
        this.setUtterances(utterances);
        return this;
    }

    public Intent addUtterances(Utterance utterance) {
        this.utterances.add(utterance);
        utterance.setIntent(this);
        return this;
    }

    public Intent removeUtterances(Utterance utterance) {
        this.utterances.remove(utterance);
        utterance.setIntent(null);
        return this;
    }

    public Set<IntentEntity> getIntentEntities() {
        return this.intentEntities;
    }

    public void setIntentEntities(Set<IntentEntity> intentEntities) {
        if (this.intentEntities != null) {
            this.intentEntities.forEach(i -> i.setIntent(null));
        }
        if (intentEntities != null) {
            intentEntities.forEach(i -> i.setIntent(this));
        }
        this.intentEntities = intentEntities;
    }

    public Intent intentEntities(Set<IntentEntity> intentEntities) {
        this.setIntentEntities(intentEntities);
        return this;
    }

    public Intent addIntentEntities(IntentEntity intentEntity) {
        this.intentEntities.add(intentEntity);
        intentEntity.setIntent(this);
        return this;
    }

    public Intent removeIntentEntities(IntentEntity intentEntity) {
        this.intentEntities.remove(intentEntity);
        intentEntity.setIntent(null);
        return this;
    }

    public Bot getBot() {
        return this.bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public Intent bot(Bot bot) {
        this.setBot(bot);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Intent)) {
            return false;
        }
        return getId() != null && getId().equals(((Intent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Intent{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", respType='" + getRespType() + "'" +
            "}";
    }
}
