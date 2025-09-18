package com.method5.jot.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * NativeLibLoader — class for native lib loader in the Jot SDK. Provides key management and
 * signing; utility helpers.
 */
public class NativeLibLoader {
    private static boolean loaded = false;

    public static synchronized void load() {
        if (loaded) return;
        try {
            String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
            String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

            String resourcePath = getResourcePath(os, arch);

            try (InputStream in = NativeLibLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
                if (in == null) throw new FileNotFoundException("Native library not found in JAR: " + resourcePath);

                Path tempFile = Files.createTempFile((!os.contains("win") ? "lib" : "") + "jot_sr25519-", resourcePath.substring(resourcePath.lastIndexOf(".")));
                tempFile.toFile().deleteOnExit();

                try (OutputStream out = Files.newOutputStream(tempFile)) {
                    in.transferTo(out);
                }

                System.load(tempFile.toAbsolutePath().toString());
                loaded = true;
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load native library", e);
        }
    }

    private static String getResourcePath(String os, String arch) {
        String resourcePath;
        if (os.contains("win")) {
            resourcePath = "lib/jot_sr25519-windows-x86_64.dll";
        } else if (os.contains("mac")) {
            resourcePath = arch.contains("aarch64") || arch.contains("arm")
                    ? "lib/libjot_sr25519-macos-aarch64.dylib"
                    : "lib/libjot_sr25519-macos-x86_64.dylib";
        } else {
            resourcePath = arch.contains("aarch64") || arch.contains("arm")
                    ? "lib/libjot_sr25519-linux-aarch64.so"
                    : "lib/libjot_sr25519-linux-x86_64.so";
        }
        return resourcePath;
    }
}