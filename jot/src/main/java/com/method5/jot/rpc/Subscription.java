package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Subscription — class for subscription in the Jot SDK. Provides RPC client / JSON‑RPC
 * integration.
 */
public class Subscription<T> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final SubscriptionType subscriptionType;
    private final Consumer<T> onMessage;
    private final ExecutorService executor;
    private final PolkadotWs client;
    private final String subscriptionID;
    private final MessageParser<T> parser;

    public Subscription(SubscriptionType subscriptionType, PolkadotWs client, Consumer<T> onMessage) throws Exception {
        this.subscriptionType = subscriptionType;
        this.client = client;
        this.onMessage = onMessage;

        this.parser = SubscriptionFactory.getParser(subscriptionType);
        if (this.parser == null) {
            throw new IllegalArgumentException("No parser registered for: " + subscriptionType);
        }

        this.executor = Executors.newFixedThreadPool(10, r -> {
            Thread t = new Thread(r);
            t.setName("subscription-" + subscriptionType.name().toLowerCase() + "-handler");
            t.setDaemon(true);
            return t;
        });

        this.subscriptionID = client.subscribe(subscriptionType.getSubscribeMethod(), objectMapper.createArrayNode(), this::handleMessage);
    }

    public boolean unsubscribe() throws RpcException {
        executor.shutdownNow();
        return client.unsubscribe(subscriptionType.getUnsubscribeMethod(), subscriptionID);
    }

    public void handleMessage(JsonNode message) {
        executor.submit(() -> {
            try {
                T parsed = parser.parse(message);
                onMessage.accept(parsed);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse message for type: " + subscriptionType, e);
            }
        });
    }
}