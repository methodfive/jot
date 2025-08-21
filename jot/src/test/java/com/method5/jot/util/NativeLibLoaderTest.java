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
}