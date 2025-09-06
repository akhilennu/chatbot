package io.akhilennu.chatbot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ResponseDataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ResponseData getResponseDataSample1() {
        return new ResponseData().id(1L).type("type1").channelName("channelName1");
    }

    public static ResponseData getResponseDataSample2() {
        return new ResponseData().id(2L).type("type2").channelName("channelName2");
    }

    public static ResponseData getResponseDataRandomSampleGenerator() {
        return new ResponseData()
            .id(longCount.incrementAndGet())
            .type(UUID.randomUUID().toString())
            .channelName(UUID.randomUUID().toString());
    }
}
