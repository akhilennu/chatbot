package io.akhilennu.chatbot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IntentEntityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IntentEntity getIntentEntitySample1() {
        return new IntentEntity().id(1L).entityName("entityName1");
    }

    public static IntentEntity getIntentEntitySample2() {
        return new IntentEntity().id(2L).entityName("entityName2");
    }

    public static IntentEntity getIntentEntityRandomSampleGenerator() {
        return new IntentEntity().id(longCount.incrementAndGet()).entityName(UUID.randomUUID().toString());
    }
}
