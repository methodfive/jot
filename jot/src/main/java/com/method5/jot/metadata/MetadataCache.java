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
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * MetadataCache — class for caching metadata in the Jot SDK.
 */
public final class MetadataCache {
    private MetadataCache() {}

    private static final ObjectMapper M = new ObjectMapper();
    private static final Map<String, CachedBundle> MEM = new ConcurrentHashMap<>();

    public static final class CachedBundle {
        public ChainSpec chainSpec;
        public String metadataHex;
        public String metadataHash;
        public String genesisHash;
        public long specVersion;
        public long txVersion;

        // Parsed metadata kept in-memory only (never written to disk)
        public transient MetadataV14 metadata;
    }

    // Create cache key: "<genesisHash>-<specVersion>-<txVersion>"
    public static String key(String genesisHash, long specVersion, long txVersion) {
        return genesisHash + "-" + specVersion + "-" + txVersion;
    }

    public static CachedBundle get(String key) {
        CachedBundle b = MEM.get(key);
        if (b != null) return b;

        final Path ROOT = CacheDirs.defaultCacheDir("jot");
        Path dir = ROOT.resolve(key);
        Path manifest = dir.resolve("manifest.json");
        if (!Files.exists(manifest)) return null;

        try {
            Map<String, Object> man = M.readValue(Files.readString(manifest), new TypeReference<>() {});
            String mh = (String) man.get("metadataHash");

            ChainSpec cs = readJsonGz(dir.resolve("chainspec.json.gz"), ChainSpec.class);
            String metadataHex = readTextGz(dir.resolve("metadata.hex.gz"));

            // integrity check
            String actualHash = HexUtil.bytesToHex(Hasher.hash256(HexUtil.hexToBytes(metadataHex)));
            if (!actualHash.equalsIgnoreCase(mh)) {
                return null; // stale or tampered
            }

            CachedBundle out = new CachedBundle();
            out.chainSpec = cs;
            out.metadataHex = metadataHex;
            out.metadataHash = mh;

            // extract from key
            ParsedKey parts = parseKey(key);
            out.genesisHash = parts.genesis;
            out.specVersion = parts.specVersion;
            out.txVersion = parts.txVersion;

            MEM.put(key, out);
            return out;
        } catch (Exception e) {
            return null;
        }
    }

    public static void put(String key, ChainSpec cs, MetadataV14 md, String metadataHex) {
        final Path ROOT = CacheDirs.defaultCacheDir("jot");
        Path dir = ROOT.resolve(key);
        try {
            Files.createDirectories(dir);

            writeJsonGz(dir.resolve("chainspec.json.gz"), cs);
            writeTextGz(dir.resolve("metadata.hex.gz"), metadataHex);

            String mh = HexUtil.bytesToHex(Hasher.hash256(HexUtil.hexToBytes(metadataHex)));

            ParsedKey parts = parseKey(key);

            Map<String, Object> manifest = Map.of(
                    "version", 2,
                    "metadataHash", mh,
                    "txVersion", parts.txVersion
            );
            Files.writeString(dir.resolve("manifest.json"), M.writeValueAsString(manifest));

            CachedBundle out = new CachedBundle();
            out.chainSpec = cs;
            out.metadataHex = metadataHex;
            out.metadataHash = mh;
            out.metadata = md;
            out.genesisHash = parts.genesis;
            out.specVersion = parts.specVersion;
            out.txVersion = parts.txVersion;

            MEM.put(key, out);
        } catch (IOException e) {
            // Non-fatal: fail open
        }
    }

    public static void clearMemory() {
        MEM.clear();
    }

    public static CachedBundle getLatest() {
        return getLatest(null);
    }

    static CachedBundle getLatest(String genesisFilter) {
        final Path ROOT = CacheDirs.defaultCacheDir("jot");
        if (!Files.isDirectory(ROOT)) return null;

        try (Stream<Path> dirs = Files.list(ROOT)) {
            var best = dirs
                    .filter(Files::isDirectory)
                    .map(Path::getFileName)
                    .map(Path::toString) // key string: <genesis>-<spec>-<tx>
                    .filter(k -> genesisFilter == null || k.startsWith(genesisFilter + "-"))
                    .map(k -> {
                        Path manifest = ROOT.resolve(k).resolve("manifest.json");
                        long ts;
                        try {
                            ts = Files.exists(manifest)
                                    ? Files.getLastModifiedTime(manifest).toMillis()
                                    : Long.MIN_VALUE;
                        } catch (IOException e) {
                            ts = Long.MIN_VALUE;
                        }
                        ParsedKey pk = parseKey(k);
                        return new KeyInfo(k, ts, pk.specVersion, pk.txVersion);
                    })
                    .max(Comparator
                            .comparingLong((KeyInfo ki) -> ki.lastModified)
                            .thenComparingLong(ki -> ki.specVersion)
                            .thenComparingLong(ki -> ki.txVersion))
                    .orElse(null);

            return (best == null) ? null : get(best.key);
        } catch (IOException e) {
            return null;
        }
    }

    private static final class KeyInfo {
        final String key;
        final long lastModified;
        final long specVersion;
        final long txVersion;

        KeyInfo(String key, long lastModified, long specVersion, long txVersion) {
            this.key = key;
            this.lastModified = lastModified;
            this.specVersion = specVersion;
            this.txVersion = txVersion;
        }
    }

    private static final class ParsedKey {
        final String genesis;
        final long specVersion;
        final long txVersion;
        ParsedKey(String g, long s, long t) {
            this.genesis = g;
            this.specVersion = s;
            this.txVersion = t;
        }
    }

    private static ParsedKey parseKey(String key) {
        // Expect format: genesis-spec-tx
        String[] parts = key.split("-", 3);
        if (parts.length < 3) return new ParsedKey(key, -1, -1);

        String genesis = parts[0];
        long spec = parseLongSafe(parts[1]);
        long tx = parseLongSafe(parts[2]);
        return new ParsedKey(genesis, spec, tx);
    }

    private static long parseLongSafe(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static <T> T readJsonGz(Path p, Class<T> type) throws IOException {
        try (InputStream in = new GZIPInputStream(Files.newInputStream(p))) {
            return M.readValue(in, type);
        }
    }

    static <T> void writeJsonGz(Path p, T obj) throws IOException {
        try (OutputStream out = new GZIPOutputStream(Files.newOutputStream(p))) {
            M.writeValue(out, obj);
        }
    }

    private static String readTextGz(Path p) throws IOException {
        try (InputStream in = new GZIPInputStream(Files.newInputStream(p))) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    static void writeTextGz(Path p, String s) throws IOException {
        try (OutputStream out = new GZIPOutputStream(Files.newOutputStream(p))) {
            out.write(s.getBytes(StandardCharsets.UTF_8));
        }
    }
}