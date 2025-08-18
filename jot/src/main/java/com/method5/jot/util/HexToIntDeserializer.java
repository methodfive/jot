package com.method5.jot.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

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