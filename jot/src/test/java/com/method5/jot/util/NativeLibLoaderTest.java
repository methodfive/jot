package com.method5.jot.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NativeLibLoaderTest {
    @Test
    void testLoadNativeLibraryDoesNotThrow() {
        assertDoesNotThrow(NativeLibLoader::load, "Native library should load without throwing");
    }

    @Test
    void testLibraryActuallyLoads() {
        try {
            NativeLibLoader.load();
            assertTrue(true);
        } catch (Exception e) {
            fail("Expected native lib to load but got: " + e.getMessage(), e);
        }
    }

    @Test
    void testLoadTwiceIsSafe() {
        assertDoesNotThrow(() -> {
            NativeLibLoader.load();
            NativeLibLoader.load();
        }, "Calling load() multiple times should be safe");
    }

    @Test
    void testGetResourcePath() {
        assertEquals("lib/jot_sr25519-windows-x86_64.dll", NativeLibLoader.getResourcePath("win",""));
        assertEquals("lib/libjot_sr25519-macos-aarch64.dylib", NativeLibLoader.getResourcePath("mac","aarch64"));
        assertEquals("lib/libjot_sr25519-macos-x86_64.dylib", NativeLibLoader.getResourcePath("mac","unknown"));
        assertEquals("lib/libjot_sr25519-linux-aarch64.so", NativeLibLoader.getResourcePath("other","aarch64"));
        assertEquals("lib/libjot_sr25519-linux-x86_64.so", NativeLibLoader.getResourcePath("other","other"));
    }
}