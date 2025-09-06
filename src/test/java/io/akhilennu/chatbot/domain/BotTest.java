package io.akhilennu.chatbot.domain;

import static io.akhilennu.chatbot.domain.BotTestSamples.*;
import static io.akhilennu.chatbot.domain.BotTrainingTestSamples.*;
import static io.akhilennu.chatbot.domain.IntentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bot.class);
        Bot bot1 = getBotSample1();
        Bot bot2 = new Bot();
        assertThat(bot1).isNotEqualTo(bot2);

        bot2.setId(bot1.getId());
        assertThat(bot1).isEqualTo(bot2);

        bot2 = getBotSample2();
        assertThat(bot1).isNotEqualTo(bot2);
    }

    @Test
    void intentsTest() {
        Bot bot = getBotRandomSampleGenerator();
        Intent intentBack = getIntentRandomSampleGenerator();

        bot.addIntents(intentBack);
        assertThat(bot.getIntents()).containsOnly(intentBack);
        assertThat(intentBack.getBot()).isEqualTo(bot);

        bot.removeIntents(intentBack);
        assertThat(bot.getIntents()).doesNotContain(intentBack);
        assertThat(intentBack.getBot()).isNull();

        bot.intents(new HashSet<>(Set.of(intentBack)));
        assertThat(bot.getIntents()).containsOnly(intentBack);
        assertThat(intentBack.getBot()).isEqualTo(bot);

        bot.setIntents(new HashSet<>());
        assertThat(bot.getIntents()).doesNotContain(intentBack);
        assertThat(intentBack.getBot()).isNull();
    }

    @Test
    void botTrainingsTest() {
        Bot bot = getBotRandomSampleGenerator();
        BotTraining botTrainingBack = getBotTrainingRandomSampleGenerator();

        bot.addBotTrainings(botTrainingBack);
        assertThat(bot.getBotTrainings()).containsOnly(botTrainingBack);
        assertThat(botTrainingBack.getBot()).isEqualTo(bot);

        bot.removeBotTrainings(botTrainingBack);
        assertThat(bot.getBotTrainings()).doesNotContain(botTrainingBack);
        assertThat(botTrainingBack.getBot()).isNull();

        bot.botTrainings(new HashSet<>(Set.of(botTrainingBack)));
        assertThat(bot.getBotTrainings()).containsOnly(botTrainingBack);
        assertThat(botTrainingBack.getBot()).isEqualTo(bot);

        bot.setBotTrainings(new HashSet<>());
        assertThat(bot.getBotTrainings()).doesNotContain(botTrainingBack);
        assertThat(botTrainingBack.getBot()).isNull();
    }
}
