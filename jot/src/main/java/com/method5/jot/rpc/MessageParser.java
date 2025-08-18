package com.method5.jot.rpc;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface MessageParser<T> {
    T parse(JsonNode node);
}
