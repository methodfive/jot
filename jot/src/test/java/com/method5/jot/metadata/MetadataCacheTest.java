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
import java.nio.file.attribute.FileTime;
import java.time.Instant;

import static com.method5.jot.metadata.MetadataCache.writeJsonGz;
import static com.method5.jot.metadata.MetadataCache.writeTextGz;
import static org.junit.jupiter.api.Assertions.*;

public class MetadataCacheTest {
    @TempDir
    Path tmpDir;

    @BeforeEach
    void initialize() {
        System.setProperty("jot.cache.dir", tmpDir.toString());
        MetadataCache.clear();
    }

    @AfterEach
    void destroy() {
        System.clearProperty("jot.cache.dir");
        MetadataCache.clear();
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
        assertEquals(genesis, got.genesisHash);
        assertEquals(specVer, got.specVersion);
        assertEquals(txVer, got.txVersion);

        String expectedHash = HexUtil.bytesToHex(Hasher.hash256(HexUtil.hexToBytes(metadataHex)));
        assertEquals(expectedHash.toLowerCase(), got.metadataHash.toLowerCase());

        MetadataCache.clearMemory();

        // from disk
        got = MetadataCache.get(key);
        assertNotNull(got);
        assertNull(got.chainSpec);
        assertEquals(metadataHex, got.metadataHex);
        assertEquals(genesis, got.genesisHash);
        assertEquals(specVer, got.specVersion);
        assertEquals(txVer, got.txVersion);

        expectedHash = HexUtil.bytesToHex(Hasher.hash256(HexUtil.hexToBytes(metadataHex)));
        assertEquals(expectedHash.toLowerCase(), got.metadataHash.toLowerCase());
    }

    @Test
    void corruptedManifestHash() throws IOException {
        String key = MetadataCache.key("0x1111", 7L, 33L);
        Path dir = tmpDir.resolve(key);
        Files.createDirectories(dir);

        writeJsonGz(dir.resolve("chainspec.json.gz"), null);

        String metadataHex = "a1b2c3d4";
        writeTextGz(dir.resolve("metadata.hex.gz"), metadataHex);

        String badHash = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        String manifestJson = "{\"version\":2,\"metadataHash\":\"" + badHash + "\"}";
        Files.writeString(dir.resolve("manifest.json"), manifestJson, StandardCharsets.UTF_8);

        MetadataCache.CachedBundle got = MetadataCache.get(key);
        assertNull(got);
    }

    @Test
    void getLatest() throws Exception {
        String GEN_A = "0xaaaa";
        String GEN_B = "0xbbbb";

        makeBundleWithTimestamp(GEN_A, 41, 1, "aa", minutesAgo(3));
        makeBundleWithTimestamp(GEN_A, 42, 1, "ab", minutesAgo(2));

        String newestKey = MetadataCache.key(GEN_B, 40, 9);
        makeBundleWithTimestamp(GEN_B, 40, 9, "ac", minutesAgo(1));

        MetadataCache.clearMemory();

        MetadataCache.CachedBundle latest = MetadataCache.getLatest();

        assertNotNull(latest);
        assertEquals(newestKey, MetadataCache.key(latest.genesisHash, latest.specVersion, latest.txVersion));
        assertEquals(GEN_B, latest.genesisHash);
        assertEquals(40L, latest.specVersion);
        assertEquals(9L, latest.txVersion);
    }

    @Test
    void getLatestByGenesis() throws Exception {
        String GEN = "0xcccc";
        long ts = minutesAgo(5);

        makeBundleWithTimestamp(GEN, 41, 1, "ba", ts);
        makeBundleWithTimestamp(GEN, 42, 1, "bb", ts);
        String expectedKey = MetadataCache.key(GEN, 42, 2);
        makeBundleWithTimestamp(GEN, 42, 2, "bc", ts);

        MetadataCache.clearMemory();

        MetadataCache.CachedBundle latestForGenesis = MetadataCache.getLatest(GEN);

        assertNotNull(latestForGenesis);
        assertEquals(expectedKey, MetadataCache.key(
                latestForGenesis.genesisHash,
                latestForGenesis.specVersion,
                latestForGenesis.txVersion
        ));
        assertEquals(GEN, latestForGenesis.genesisHash);
        assertEquals(42L, latestForGenesis.specVersion);
        assertEquals(2L, latestForGenesis.txVersion);
    }

    private void makeBundleWithTimestamp(String genesis, long spec, long tx, String metadataHex, long millis)
            throws Exception {
        String key = MetadataCache.key(genesis, spec, tx);
        Path dir = tmpDir.resolve(key);
        Files.createDirectories(dir);

        writeJsonGz(dir.resolve("chainspec.json.gz"), null);
        writeTextGz(dir.resolve("metadata.hex.gz"), metadataHex);

        String mh = HexUtil.bytesToHex(Hasher.hash256(HexUtil.hexToBytes(metadataHex)));
        String manifestJson = "{\"version\":2,\"metadataHash\":\"" + mh + "\",\"txVersion\":" + tx + "}";
        Path manifest = dir.resolve("manifest.json");
        Files.writeString(manifest, manifestJson, StandardCharsets.UTF_8);

        Files.setLastModifiedTime(manifest, FileTime.from(Instant.ofEpochMilli(millis)));
    }

    private static long minutesAgo(int minutes) {
        return System.currentTimeMillis() - minutes * 60_000L;
    }
}
