package io.akhilennu.chatbot.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class BotTrainingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BotTraining getBotTrainingSample1() {
        return new BotTraining().id(1L);
    }

    public static BotTraining getBotTrainingSample2() {
        return new BotTraining().id(2L);
    }

    public static BotTraining getBotTrainingRandomSampleGenerator() {
        return new BotTraining().id(longCount.incrementAndGet());
    }
}
