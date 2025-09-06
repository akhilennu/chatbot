package io.akhilennu.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BotTraining.
 */
@Entity
@Table(name = "bot_training")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BotTraining implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "training_time", nullable = false)
    private Instant trainingTime;

    @Column(name = "accuracy")
    private Double accuracy;

    @Column(name = "precision")
    private Double precision;

    @Column(name = "f_1_score")
    private Double f1Score;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "intents", "botTrainings" }, allowSetters = true)
    private Bot bot;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BotTraining id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTrainingTime() {
        return this.trainingTime;
    }

    public BotTraining trainingTime(Instant trainingTime) {
        this.setTrainingTime(trainingTime);
        return this;
    }

    public void setTrainingTime(Instant trainingTime) {
        this.trainingTime = trainingTime;
    }

    public Double getAccuracy() {
        return this.accuracy;
    }

    public BotTraining accuracy(Double accuracy) {
        this.setAccuracy(accuracy);
        return this;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Double getPrecision() {
        return this.precision;
    }

    public BotTraining precision(Double precision) {
        this.setPrecision(precision);
        return this;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    public Double getf1Score() {
        return this.f1Score;
    }

    public BotTraining f1Score(Double f1Score) {
        this.setf1Score(f1Score);
        return this;
    }

    public void setf1Score(Double f1Score) {
        this.f1Score = f1Score;
    }

    public Boolean getActive() {
        return this.active;
    }

    public BotTraining active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Bot getBot() {
        return this.bot;
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public BotTraining bot(Bot bot) {
        this.setBot(bot);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BotTraining)) {
            return false;
        }
        return getId() != null && getId().equals(((BotTraining) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BotTraining{" +
            "id=" + getId() +
            ", trainingTime='" + getTrainingTime() + "'" +
            ", accuracy=" + getAccuracy() +
            ", precision=" + getPrecision() +
            ", f1Score=" + getf1Score() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
