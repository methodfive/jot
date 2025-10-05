package com.method5.jot.metadata;

import com.method5.jot.crypto.Hasher;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static com.method5.jot.metadata.MetadataCache.writeJsonGz;
import static com.method5.jot.metadata.MetadataCache.writeTextGz;
import static org.junit.jupiter.api.Assertions.*;

public class MetadataCacheTest {
    @TempDir
    Path tmpDir;

    @BeforeEach
    void initialize() {
        System.setProperty("jot.cache.dir", tmpDir.toString());
        MetadataCache.clearMemory();
    }

    @AfterEach
    void destroy() {
        System.clearProperty("jot.cache.dir");
        MetadataCache.clearMemory();

        try {
            if (tmpDir != null && Files.exists(tmpDir)) {
                Files.walk(tmpDir)
                        .sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (IOException ignored) {
                            }
                        });
            }
        } catch (IOException ignored) {
        }
    }

    @Test
    void putThenGetok() {
        String genesis = "0x000000000000000000000000000000000000000000000000000000000000abcd";
        long specVer = 42L;
        long txVer = 46L;
        String key = MetadataCache.key(genesis, specVer, txVer);

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
        String key = MetadataCache.key("0x1111", 7L, 33L);
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
}
