package io.akhilennu.chatbot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SlotValueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SlotValue getSlotValueSample1() {
        return new SlotValue().id(1L).slotName("slotName1").slotValue("slotValue1");
    }

    public static SlotValue getSlotValueSample2() {
        return new SlotValue().id(2L).slotName("slotName2").slotValue("slotValue2");
    }

    public static SlotValue getSlotValueRandomSampleGenerator() {
        return new SlotValue()
            .id(longCount.incrementAndGet())
            .slotName(UUID.randomUUID().toString())
            .slotValue(UUID.randomUUID().toString());
    }
}
