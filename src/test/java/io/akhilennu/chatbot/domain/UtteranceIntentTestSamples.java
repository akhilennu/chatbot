package io.akhilennu.chatbot.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UtteranceIntentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UtteranceIntent getUtteranceIntentSample1() {
        return new UtteranceIntent().id(1L).startIndex(1).endIndex(1);
    }

    public static UtteranceIntent getUtteranceIntentSample2() {
        return new UtteranceIntent().id(2L).startIndex(2).endIndex(2);
    }

    public static UtteranceIntent getUtteranceIntentRandomSampleGenerator() {
        return new UtteranceIntent()
            .id(longCount.incrementAndGet())
            .startIndex(intCount.incrementAndGet())
            .endIndex(intCount.incrementAndGet());
    }
}
