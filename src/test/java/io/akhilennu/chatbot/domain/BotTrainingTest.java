package io.akhilennu.chatbot.domain;

import static io.akhilennu.chatbot.domain.BotTestSamples.*;
import static io.akhilennu.chatbot.domain.BotTrainingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.akhilennu.chatbot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BotTrainingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BotTraining.class);
        BotTraining botTraining1 = getBotTrainingSample1();
        BotTraining botTraining2 = new BotTraining();
        assertThat(botTraining1).isNotEqualTo(botTraining2);

        botTraining2.setId(botTraining1.getId());
        assertThat(botTraining1).isEqualTo(botTraining2);

        botTraining2 = getBotTrainingSample2();
        assertThat(botTraining1).isNotEqualTo(botTraining2);
    }

    @Test
    void botTest() {
        BotTraining botTraining = getBotTrainingRandomSampleGenerator();
        Bot botBack = getBotRandomSampleGenerator();

        botTraining.setBot(botBack);
        assertThat(botTraining.getBot()).isEqualTo(botBack);

        botTraining.bot(null);
        assertThat(botTraining.getBot()).isNull();
    }
}
