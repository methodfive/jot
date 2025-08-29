package com.method5.jot.crypto;

import com.method5.jot.util.NativeLibLoader;

/**
 * Sr25519 — class for sr 25519 in the Jot SDK. Provides key management and signing.
 */
public class Sr25519 {
    private static volatile boolean initialized = false;

    private static synchronized void ensureLoaded() {
        if (!initialized) {
            try {
                NativeLibLoader.load();
                initialized = true;
            } catch (Throwable t) {
                throw new RuntimeException("Failed to load native library for Sr25519", t);
            }
        }
    }

    public static byte[] sign(byte[] seed32, byte[] message) {
        ensureLoaded();
        return nativeSign(seed32, message);
    }

    public static boolean verify(byte[] publicKey, byte[] message, byte[] signature) {
        ensureLoaded();
        return nativeVerify(publicKey, message, signature);
    }

    public static byte[] derivePublicKey(byte[] seed32) {
        ensureLoaded();
        return nativeDerivePublicKey(seed32);
    }

    public static byte[] deriveSeedFromMnemonic(String mnemonic, String password) {
        ensureLoaded();
        return nativeDeriveSeedFromMnemonic(mnemonic, password);
    }

    public static byte[][] generateMnemonicAndSeed() {
        ensureLoaded();
        return nativeGenerateMnemonicAndSeed();
    }

    private static native byte[] nativeSign(byte[] seed32, byte[] message);
    private static native boolean nativeVerify(byte[] publicKey, byte[] message, byte[] signature);
    private static native byte[] nativeDerivePublicKey(byte[] seed32);
    private static native byte[] nativeDeriveSeedFromMnemonic(String mnemonic, String password);
    private static native byte[][] nativeGenerateMnemonicAndSeed();
}