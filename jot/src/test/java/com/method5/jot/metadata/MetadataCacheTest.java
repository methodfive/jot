package com.method5.jot.metadata;

import com.method5.jot.crypto.Hasher;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class MetadataCacheTest {
    @TempDir
    Path tmpDir;

    private String origOverride;

    @BeforeEach
    void initialize() {
        origOverride = System.getProperty("jot.cache.dir");
        System.setProperty("jot.cache.dir", tmpDir.toString());
    }

    @AfterEach
    void destroy() {
        if (origOverride == null) System.clearProperty("jot.cache.dir");
        else System.setProperty("jot.cache.dir", origOverride);
    }

    @Test
    void putThenGetok() {
        String genesis = "0x000000000000000000000000000000000000000000000000000000000000abcd";
        long specVer = 42L;
        String key = MetadataCache.key(genesis, specVer);

        String metadataHex = "deadbeef00aa55";

        MetadataCache.put(key, null, null, metadataHex);

        // from memory
        MetadataCache.CachedBundle got = MetadataCache.get(key);
        assertNotNull(got);
        assertNull(got.chainSpec);
        assertEquals(metadataHex, got.metadataHex);

        String expectedHash = HexUtil.bytesToHex(Hasher.hash256(HexUtil.hexToBytes(metadataHex)));
        assertEquals(expectedHash.toLowerCase(), got.metadataHash.toLowerCase());

        MetadataCache.clearMemory();

        // from disk
        got = MetadataCache.get(key);
        assertNotNull(got);
        assertNull(got.chainSpec);
        assertEquals(metadataHex, got.metadataHex);

        expectedHash = HexUtil.bytesToHex(Hasher.hash256(HexUtil.hexToBytes(metadataHex)));
        assertEquals(expectedHash.toLowerCase(), got.metadataHash.toLowerCase());
    }

    @Test
    void corruptedManifestHash() throws IOException {
        String key = MetadataCache.key("0x1111", 7L);
        Path root = tmpDir;
        Path dir = root.resolve(key);
        Files.createDirectories(dir);

        writeJsonGz(dir.resolve("chainspec.json.gz"), "null");

        String metadataHex = "a1b2c3d4";
        writeTextGz(dir.resolve("metadata.hex.gz"), metadataHex);

        String badHash = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        String manifestJson = "{\"version\":2,\"metadataHash\":\"" + badHash + "\"}";
        Files.writeString(dir.resolve("manifest.json"), manifestJson, StandardCharsets.UTF_8);

        MetadataCache.CachedBundle got = MetadataCache.get(key);
        assertNull(got);
    }

    private static void writeJsonGz(Path p, String rawJson) throws IOException {
        try (OutputStream out = new GZIPOutputStream(Files.newOutputStream(p))) {
            out.write(rawJson.getBytes(StandardCharsets.UTF_8));
        }
    }

    private static void writeTextGz(Path p, String text) throws IOException {
        try (OutputStream out = new GZIPOutputStream(Files.newOutputStream(p))) {
            out.write(text.getBytes(StandardCharsets.UTF_8));
        }
    }
}
