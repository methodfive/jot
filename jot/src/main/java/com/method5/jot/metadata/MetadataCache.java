package com.method5.jot.metadata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.method5.jot.crypto.Hasher;
import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.spec.ChainSpec;
import com.method5.jot.util.HexUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * MetadataCache — class for caching metadata in the Jot SDK.
 */
public final class MetadataCache {
    private MetadataCache() {}

    private static final ObjectMapper M = new ObjectMapper();
    private static final Map<String, CachedBundle> MEM = new ConcurrentHashMap<>();
    private static final Path ROOT = CacheDirs.defaultCacheDir("jot");

    public static final class CachedBundle {
        public ChainSpec chainSpec;
        public String metadataHex;
        public String metadataHash;

        // Parsed metadata kept in-memory only (never written to disk)
        public transient MetadataV14 metadata;
    }

    public static String key(String genesisHash, long specVersion) {
        return genesisHash + "-" + specVersion;
    }

    public static CachedBundle get(String key) {
        CachedBundle b = MEM.get(key);
        if (b != null) return b;

        Path dir = ROOT.resolve(key);
        Path manifest = dir.resolve("manifest.json");
        if (!Files.exists(manifest)) return null;

        try {
            Map<String, Object> man = M.readValue(Files.readString(manifest), new TypeReference<>() {});
            String mh = (String) man.get("metadataHash");

            ChainSpec cs = readJsonGz(dir.resolve("chainspec.json.gz"), ChainSpec.class);
            String metadataHex = readTextGz(dir.resolve("metadata.hex.gz"));

            // integrity check against stored hash
            String actualHash = HexUtil.bytesToHex(Hasher.hash256(HexUtil.hexToBytes(metadataHex)));
            if (!actualHash.equalsIgnoreCase(mh)) {
                return null; // stale or tampered
            }

            CachedBundle out = new CachedBundle();
            out.chainSpec = cs;
            out.metadataHex = metadataHex;
            out.metadataHash = mh;

            MEM.put(key, out);
            return out;
        } catch (Exception e) {
            return null;
        }
    }

    public static void put(String key, ChainSpec cs, MetadataV14 md, String metadataHex) {
        Path dir = ROOT.resolve(key);
        try {
            Files.createDirectories(dir);

            writeJsonGz(dir.resolve("chainspec.json.gz"), cs);
            writeTextGz(dir.resolve("metadata.hex.gz"), metadataHex);

            String mh = HexUtil.bytesToHex(Hasher.hash256(HexUtil.hexToBytes(metadataHex)));
            Map<String, Object> manifest = Map.of(
                    "version", 2,
                    "metadataHash", mh
            );
            Files.writeString(dir.resolve("manifest.json"), M.writeValueAsString(manifest));

            CachedBundle out = new CachedBundle();
            out.chainSpec = cs;
            out.metadataHex = metadataHex;
            out.metadataHash = mh;
            out.metadata = md; // in-memory only
            MEM.put(key, out);
        } catch (IOException e) {
            // Non-fatal: fail open
        }
    }

    public static void clearMemory() {
        MEM.clear();
    }

    private static <T> T readJsonGz(Path p, Class<T> type) throws IOException {
        try (InputStream in = new GZIPInputStream(Files.newInputStream(p))) {
            return M.readValue(in, type);
        }
    }

    private static <T> void writeJsonGz(Path p, T obj) throws IOException {
        try (OutputStream out = new GZIPOutputStream(Files.newOutputStream(p))) {
            M.writeValue(out, obj);
        }
    }

    private static String readTextGz(Path p) throws IOException {
        try (InputStream in = new GZIPInputStream(Files.newInputStream(p))) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static void writeTextGz(Path p, String s) throws IOException {
        try (OutputStream out = new GZIPOutputStream(Files.newOutputStream(p))) {
            out.write(s.getBytes(StandardCharsets.UTF_8));
        }
    }
}
