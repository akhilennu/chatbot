package io.akhilennu.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bot.
 */
@Entity
@Table(name = "bot")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bot")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "responseData", "utterances", "intentEntities", "bot" }, allowSetters = true)
    private Set<Intent> intents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bot")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bot" }, allowSetters = true)
    private Set<BotTraining> botTrainings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bot id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Bot name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Bot description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Intent> getIntents() {
        return this.intents;
    }

    public void setIntents(Set<Intent> intents) {
        if (this.intents != null) {
            this.intents.forEach(i -> i.setBot(null));
        }
        if (intents != null) {
            intents.forEach(i -> i.setBot(this));
        }
        this.intents = intents;
    }

    public Bot intents(Set<Intent> intents) {
        this.setIntents(intents);
        return this;
    }

    public Bot addIntents(Intent intent) {
        this.intents.add(intent);
        intent.setBot(this);
        return this;
    }

    public Bot removeIntents(Intent intent) {
        this.intents.remove(intent);
        intent.setBot(null);
        return this;
    }

    public Set<BotTraining> getBotTrainings() {
        return this.botTrainings;
    }

    public void setBotTrainings(Set<BotTraining> botTrainings) {
        if (this.botTrainings != null) {
            this.botTrainings.forEach(i -> i.setBot(null));
        }
        if (botTrainings != null) {
            botTrainings.forEach(i -> i.setBot(this));
        }
        this.botTrainings = botTrainings;
    }

    public Bot botTrainings(Set<BotTraining> botTrainings) {
        this.setBotTrainings(botTrainings);
        return this;
    }

    public Bot addBotTrainings(BotTraining botTraining) {
        this.botTrainings.add(botTraining);
        botTraining.setBot(this);
        return this;
    }

    public Bot removeBotTrainings(BotTraining botTraining) {
        this.botTrainings.remove(botTraining);
        botTraining.setBot(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bot)) {
            return false;
        }
        return getId() != null && getId().equals(((Bot) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bot{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
