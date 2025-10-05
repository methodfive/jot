package com.method5.jot.metadata;

/**
 * CacheDirs — class to determine which cache directory to use for the Jot SDK.
 */
public final class CacheDirs {
    private CacheDirs() {}

    public static java.nio.file.Path defaultCacheDir(String appName) {
        // overrides
        String override = System.getProperty("jot.cache.dir");
        if (override == null || override.isBlank()) {
            override = System.getenv("JOT_CACHE_DIR");
        }
        if (override != null && !override.isBlank()) {
            return java.nio.file.Paths.get(override);
        }

        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            String base = firstNonBlank(
                    System.getenv("LOCALAPPDATA"),
                    System.getenv("APPDATA"),
                    System.getProperty("user.home"),
                    System.getProperty("java.io.tmpdir")
            );
            // C:\Users\Me\AppData\Local\Method5\Jot\cache
            return java.nio.file.Paths.get(base, "Method5", "Jot", "cache");
        } else if (os.contains("mac") || os.contains("darwin")) {
            String home = firstNonBlank(
                    System.getProperty("user.home"),
                    System.getProperty("java.io.tmpdir")
            );
            // ~/Library/Caches/com.method5.jot
            return java.nio.file.Paths.get(home, "Library", "Caches", "com.method5.jot");
        } else {
            String xdg = System.getenv("XDG_CACHE_HOME");
            if (xdg != null && !xdg.isBlank()) {
                return java.nio.file.Paths.get(xdg, appName);
            }
            String home = firstNonBlank(
                    System.getProperty("user.home"),
                    System.getProperty("java.io.tmpdir")
            );
            // ~/.cache/jot
            return java.nio.file.Paths.get(home, ".cache", appName);
        }
    }

    static String firstNonBlank(String... opts) {
        for (String s : opts) if (s != null && !s.isBlank()) return s;
        return null;
    }
}