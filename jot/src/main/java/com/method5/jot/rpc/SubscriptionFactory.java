package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.method5.jot.query.model.BlockHeader;
import com.method5.jot.query.model.RuntimeVersion;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionFactory {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final static Map<SubscriptionType, MessageParser<?>> parsers = new HashMap<>();

    static {
        register(SubscriptionType.LATEST_HEAD, SubscriptionFactory::parseBlockHeader);
        register(SubscriptionType.ALL_HEADS, SubscriptionFactory::parseBlockHeader);
        register(SubscriptionType.BEST_HEAD, SubscriptionFactory::parseBlockHeader);
        register(SubscriptionType.FINALIZED_HEAD, SubscriptionFactory::parseBlockHeader);
        register(SubscriptionType.RUNTIME_VERSION, SubscriptionFactory::parseRuntimeVersion);
    }

    @SuppressWarnings("unchecked")
    public static <T> MessageParser<T> getParser(SubscriptionType subscriptionType) {
        return (MessageParser<T>) parsers.get(subscriptionType);
    }

    public static <T> void register(SubscriptionType subscriptionType, MessageParser<T> parser) {
        parsers.put(subscriptionType, parser);
    }

    private static BlockHeader parseBlockHeader(JsonNode node) {
        try {
            return objectMapper.treeToValue(node, BlockHeader.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse BlockHeader", e);
        }
    }

    private static RuntimeVersion parseRuntimeVersion(JsonNode node) {
        try {
            return objectMapper.treeToValue(node, RuntimeVersion.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse RuntimeVersion", e);
        }
    }
}
