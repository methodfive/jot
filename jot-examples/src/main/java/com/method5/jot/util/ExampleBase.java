package com.method5.jot.util;

public class ExampleBase {
    static {
        System.setProperty("slf4j.internal.verbosity", "WARN");
    }

    @FunctionalInterface
    interface ThrowingRunnable {
        void run() throws Exception;
    }
}
