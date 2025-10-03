package com.method5.jot.metadata;

import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class MetadataParserTest {
    private static Logger logger = LoggerFactory.getLogger(MetadataParserTest.class);

    private static byte[] metadata;

    @BeforeAll
    public static void initialize() throws IOException {
        String hex = Files.readString(Paths.get("src/test/resources/metadata.hex")).trim();
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }

        metadata = HexUtil.hexToBytes(hex);
    }

    @Test
    public void testParseMetadata() {
        CallIndexResolver resolver = new CallIndexResolver();
        MetadataParser parser = new MetadataParser(resolver);

        parser.parse(metadata);

        byte[] nonExistentCheck = resolver.resolveCallIndex("Null", "null");
        byte[] transferIndex = resolver.resolveCallIndex("Balances", "force_transfer");
        byte[] remarkIndex = resolver.resolveCallIndex("System", "remark");

        assertNull(nonExistentCheck);
        assertNotNull(transferIndex, "Balances.forceTransfer not registered");
        assertNotNull(remarkIndex, "System.remark not registered");

        assertEquals(2, transferIndex.length, "Call index should be 2 bytes");
        assertEquals(2, remarkIndex.length, "Call index should be 2 bytes");

        logger.info("Balances.transfer = [{}, {}]%n", transferIndex[0], transferIndex[1]);
        logger.info("System.remark = [{}, {}]%n", remarkIndex[0], remarkIndex[1]);
    }

    @Test
    public void testDecodeEncodeRoundTrip() {
        CallIndexResolver resolver = new CallIndexResolver();
        MetadataParser parser = new MetadataParser(resolver);

        MetadataV14 metadataV14 = parser.parse(metadata);

        // Test encoding / decoding to see if they match
        byte[] decodedMetadata = metadataV14.encode();
        assertArrayEquals(metadata, decodedMetadata);
    }
}
