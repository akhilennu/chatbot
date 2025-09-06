package io.akhilennu.chatbot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConversationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Conversation getConversationSample1() {
        return new Conversation()
            .id(1L)
            .conversationId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .channelName("channelName1");
    }

    public static Conversation getConversationSample2() {
        return new Conversation()
            .id(2L)
            .conversationId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .channelName("channelName2");
    }

    public static Conversation getConversationRandomSampleGenerator() {
        return new Conversation()
            .id(longCount.incrementAndGet())
            .conversationId(UUID.randomUUID())
            .channelName(UUID.randomUUID().toString());
    }
}
