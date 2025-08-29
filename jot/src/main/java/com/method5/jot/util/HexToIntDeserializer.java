package com.method5.jot.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * HexToIntDeserializer — class for hex to int deserializer in the Jot SDK. Provides key management
 * and signing; utility helpers.
 */
public class HexToIntDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String hex = p.getText();
        if (hex.startsWith("0x")) {
            return Integer.parseUnsignedInt(hex.substring(2), 16);
        }
        return Integer.parseInt(hex);
    }
}