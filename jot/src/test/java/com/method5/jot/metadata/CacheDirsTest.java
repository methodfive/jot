package com.method5.jot.metadata;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CacheDirsTest {
    private static final Logger logger = LoggerFactory.getLogger(CacheDirsTest.class);

    private String origOsName;
    private String origUserHome;
    private String origTmpDir;
    private String origOverride;

    @BeforeEach
    void saveProps() {
        origOsName   = System.getProperty("os.name");
        origUserHome = System.getProperty("user.home");
        origTmpDir   = System.getProperty("java.io.tmpdir");
        origOverride = System.getProperty("jot.cache.dir");
    }

    @AfterEach
    void restoreProps() {
        setOrClear("os.name", origOsName);
        setOrClear("user.home", origUserHome);
        setOrClear("java.io.tmpdir", origTmpDir);
        setOrClear("jot.cache.dir", origOverride);
    }

    private void setOrClear(String key, String val) {
        if (val == null) System.clearProperty(key);
        else System.setProperty(key, val);
    }

    @Test
    void firstNonBlank() {
        assertNull(CacheDirs.firstNonBlank("","",""));
        assertEquals("1", CacheDirs.firstNonBlank("","1",""));
    }

    @Test
    void usesSystemPropertyOverrideWhenPresent() {
        System.setProperty("jot.cache.dir", "/var/cache/jot-override");
        System.setProperty("os.name", "Linux");
        System.setProperty("user.home", "/home/tester");

        Path got = CacheDirs.defaultCacheDir("jot");
        assertEquals(Paths.get("/var/cache/jot-override"), got);
    }

    @Test
    void windowsFallbacksToUserHomeWhenNoEnv() {
        // Simulate Windows; no LOCALAPPDATA/APPDATA set (we don't modify env in this test)
        System.clearProperty("jot.cache.dir");
        System.setProperty("os.name", "Windows 11");
        System.setProperty("user.home", "C:/Users/Alice");

        Path got = CacheDirs.defaultCacheDir("jot");
        Path expected = Paths.get("C:/Users/Alice", "Method5", "Jot", "cache");
        assertEquals(expected, got);
    }

    @Test
    void macOsDefaultUnderLibraryCaches() {
        System.clearProperty("jot.cache.dir");
        System.setProperty("os.name", "Mac OS X");
        System.setProperty("user.home", "/Users/alice");

        Path got = CacheDirs.defaultCacheDir("jot");
        Path expected = Paths.get("/Users/alice", "Library", "Caches", "com.method5.jot");
        assertEquals(expected, got);
    }

    @Test
    void linuxDefaultUnderDotCacheAppName() {
        System.clearProperty("jot.cache.dir");
        System.setProperty("os.name", "Linux");
        System.setProperty("user.home", "/home/alice");

        Path got = CacheDirs.defaultCacheDir("jot");
        Path expected = Paths.get("/home/alice", ".cache", "jot");
        assertEquals(expected, got);
    }

    @Test
    void linuxFallsBackToTmpWhenNoHome() {
        System.clearProperty("jot.cache.dir");
        System.setProperty("os.name", "Linux");
        System.clearProperty("user.home");
        System.setProperty("java.io.tmpdir", "/tmp/jvm");

        Path got = CacheDirs.defaultCacheDir("jot");
        Path expected = Paths.get("/tmp/jvm", ".cache", "jot");
        assertEquals(expected, got);
    }
}
