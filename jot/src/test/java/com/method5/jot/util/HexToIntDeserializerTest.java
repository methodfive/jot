package com.method5.jot.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HexToIntDeserializerTest {
    private final HexToIntDeserializer deserializer = new HexToIntDeserializer();

    @Test
    void testDeserializeHex() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(parser.getText()).thenReturn("0x1f");

        Integer result = deserializer.deserialize(parser, ctxt);
        assertEquals(31, result);
    }

    @Test
    void testDeserializeDecimal() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(parser.getText()).thenReturn("42");

        Integer result = deserializer.deserialize(parser, ctxt);
        assertEquals(42, result);
    }

    @Test
    void testDeserializeHexWithLeadingZeros() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(parser.getText()).thenReturn("0x0000000A");

        Integer result = deserializer.deserialize(parser, ctxt);
        assertEquals(10, result);
    }

    @Test
    void testDeserializeInvalidStringThrows() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(parser.getText()).thenReturn("xyz");

        assertThrows(NumberFormatException.class, () ->
                deserializer.deserialize(parser, ctxt));
    }

    @Test
    void testDeserializeHexMissingPrefixFallsBackToDecimal() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        when(parser.getText()).thenReturn("ff"); // decimal "ff" = NumberFormatException

        assertThrows(NumberFormatException.class, () ->
                deserializer.deserialize(parser, ctxt));
    }
}
