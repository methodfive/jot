package com.method5.jot.wallet;

import com.method5.jot.crypto.Sr25519;
import com.method5.jot.crypto.Ed25519;
import com.method5.jot.signing.Ed25519Signer;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.signing.Sr25519Signer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Wallet — class for wallet in the Jot SDK. Provides key management and signing.
 */
public class Wallet {
    public enum KeyType { SR25519, ED25519, ECDSA }

    private final SigningProvider signer;
    private byte[] seed;            // Only for sr25519
    private byte[] privateKeyBytes; // Only for ed25519
    private final KeyPair keyPair;        // Only for ed25519
    private final KeyType keyType;
    private String mnemonic;
    private final String address;

    private static final ReentrantLock lock = new ReentrantLock();

    private Wallet(SigningProvider signer, byte[] privateKeyBytes, KeyPair keyPair) {
        this.signer = signer;
        this.privateKeyBytes = privateKeyBytes;
        this.keyPair = keyPair;
        this.keyType = KeyType.ED25519;
        this.address = SS58.encode(signer.getPublicKey(), 0);
        this.mnemonic = null;
    }

    private Wallet(SigningProvider signer, byte[] seed, String mnemonic) {
        this.signer = signer;
        this.seed = seed;
        this.mnemonic = mnemonic;
        this.keyType = KeyType.SR25519;
        this.address = SS58.encode(signer.getPublicKey(), 0);
        this.keyPair = null;
    }

    public static Wallet fromSr25519Seed(byte[] seed) {
        lock.lock();
        try {
            return new Wallet(new Sr25519Signer(seed), seed, (String)null);
        } finally {
            lock.unlock();
        }
    }

    public static Wallet fromEd25519PrivateKey(byte[] privateKey) throws Exception {
        lock.lock();
        try {
            KeyPair keyPair = Ed25519.loadKeyPairFromPrivateKey(privateKey);
            return new Wallet(new Ed25519Signer(keyPair), privateKey, keyPair);
        } finally {
            lock.unlock();
        }
    }

    public static Wallet fromMnemonic(String mnemonic) {
        lock.lock();
        try {
            byte[] seed = Sr25519.deriveSeedFromMnemonic(mnemonic, "");

            Wallet wallet = fromSr25519Seed(seed);
            wallet.setMnemonic(mnemonic);
            return wallet;
        } finally {
            lock.unlock();
        }
    }

    public static Wallet generate() throws Exception {
        return generate(KeyType.SR25519);
    }

    public static Wallet generate(KeyType keyType) throws Exception {
        lock.lock();
        try {
            if(keyType == KeyType.ED25519) {
                Ed25519.KeyPairAndPrivateKeyBytes generated = Ed25519.generate();
                return fromEd25519PrivateKey(generated.privateKey());
            } else if(keyType == KeyType.SR25519) {
                byte[][] result = Sr25519.generateMnemonicAndSeed();
                String mnemonic = new String(result[0], StandardCharsets.UTF_8);
                byte[] seed = result[1];

                SigningProvider signer = new Sr25519Signer(seed);
                return new Wallet(signer, seed, mnemonic);
            } else {
                throw new UnsupportedOperationException("Unsupported key type: " + keyType);
            }
        } finally {
            lock.unlock();
        }
    }

    public byte[] sign(byte[] payload) throws Exception {
        lock.lock();
        try {
            return signer.sign(payload);
        } finally {
            lock.unlock();
        }
    }

    public boolean verify(byte[] payload, byte[] signature) {
        lock.lock();
        try {
            return signer.verify(payload, signature);
        } finally {
            lock.unlock();
        }
    }

    public void saveToFile(File file) throws IOException {
        lock.lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String prefix = (keyType == KeyType.SR25519) ? "SR:" : "ED:";
            byte[] value = (keyType == KeyType.SR25519) ? seed : privateKeyBytes;
            writer.write(prefix + Base64.getEncoder().encodeToString(value));
        } finally {
            lock.unlock();
        }
    }

    public static Wallet fromFile(File file) throws Exception {
        lock.lock();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null || (!line.startsWith("SR:") && !line.startsWith("ED:"))) {
                throw new IOException("Invalid wallet file format.");
            }
            String encoded = line.substring(3);
            byte[] data = Base64.getDecoder().decode(encoded);
            if (line.startsWith("SR:")) {
                return fromSr25519Seed(data);
            } else {
                return fromEd25519PrivateKey(data);
            }
        } finally {
            lock.unlock();
        }
    }

    public SigningProvider getSigner() {
        return signer;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress(int prefix) {
        return SS58.encode(signer.getPublicKey(), prefix);
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public byte[] getSr25519Seed() {
        return seed;
    }

    public KeyPair getEd25519KeyPair() {
        return keyPair;
    }

    public String getMnemonic() {
        if(keyType != KeyType.SR25519) {
            throw new UnsupportedOperationException("Unsupported key type for mnemonic: " + keyType);
        }
        return mnemonic;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public byte[] getSeed() {
        return seed;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public byte[] getPrivateKeyBytes() {
        return privateKeyBytes;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "signer=" + signer +
                ", seed=" + Arrays.toString(seed) +
                ", privateKeyBytes=" + Arrays.toString(privateKeyBytes) +
                ", keyPair=" + keyPair +
                ", keyType=" + keyType +
                ", mnemonic='" + mnemonic + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
