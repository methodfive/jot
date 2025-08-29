package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * MessageParser — interface for message parser in the Jot SDK. Provides RPC client / JSON‑RPC
 * integration.
 */
@FunctionalInterface
public interface MessageParser<T> {
    T parse(JsonNode node);
}
