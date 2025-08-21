package com.method5.jot.wallet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {
    @Test
    public void testGenerateAndExportMnemonic() throws Exception {
        Wallet wallet = Wallet.generate();

        String mnemonic = wallet.getMnemonic();
        assertNotNull(mnemonic);
        assertTrue(mnemonic.split(" ").length >= 12);

        String address = wallet.getAddress();
        assertNotNull(address);

        assertEquals(Wallet.KeyType.SR25519, wallet.getKeyType());
        assertNull(wallet.getKeyPair());
        assertNotNull(wallet.getSigner());
    }

    @Test
    public void testPolkadotJsCompatibility() {
        Wallet wallet = Wallet.fromMnemonic("denial cheap device purpose inflict super column inhale quarter candy junk fetch");
        assertEquals("5EnnzBahDRDtj6JCAZND9QVYnuepGTEYvFVzVG17KDirzTdA", wallet.getAddress(42));
        assertEquals("13j68Wqm5CVNAdJi8CRDHZKheXeTxkngzkEUeYzTsJkPApnA", wallet.getAddress(0));
    }

    @Test
    public void testImportMnemonicMatchesKeys() throws Exception {
        Wallet wallet1 = Wallet.generate();
        String mnemonic = wallet1.getMnemonic();

        Wallet wallet2 = Wallet.fromMnemonic(mnemonic);
        assertEquals(wallet1.getAddress(), wallet2.getAddress());
        assertArrayEquals(wallet1.getSigner().getPublicKey(), wallet2.getSigner().getPublicKey());
    }

    @Test
    public void testImportSeedMatchesKeys() throws Exception {
        Wallet wallet1 = Wallet.generate();
        byte[] seed = wallet1.getSeed();

        Wallet wallet2 = Wallet.fromSr25519Seed(seed);
        assertEquals(wallet1.getAddress(), wallet2.getAddress());
        assertArrayEquals(wallet1.getSigner().getPublicKey(), wallet2.getSigner().getPublicKey());
    }

    @Test
    public void testSaveAndLoadSr25519(@TempDir Path tempDir) throws Exception {
        File file = new File(tempDir.toAbsolutePath() + "/wallet.dat");
        Wallet wallet1 = Wallet.generate();
        wallet1.saveToFile(file);

        Wallet wallet2 = Wallet.fromFile(file);
        assertEquals(wallet1.getAddress(), wallet2.getAddress());
        assertArrayEquals(wallet1.getSigner().getPublicKey(), wallet2.getSigner().getPublicKey());
    }

    @Test
    public void testSignVerifyEd25519() throws Exception {
        Wallet wallet = Wallet.generate(Wallet.KeyType.ED25519);
        byte[] payload = new byte[] { 3,5,2,4,7,3};

        wallet.verify(payload, wallet.sign(payload));
    }
    @Test
    public void testSaveAndLoadEd25519(@TempDir Path tempDir) throws Exception {
        File file = new File(tempDir.toAbsolutePath() + "/wallet.dat");

        Wallet wallet1 = Wallet.generate(Wallet.KeyType.ED25519);
        wallet1.saveToFile(file);

        Wallet wallet2 = Wallet.fromFile(file);

        assertEquals(wallet1.getAddress(), wallet2.getAddress());
        assertArrayEquals(wallet1.getSigner().getPublicKey(), wallet2.getSigner().getPublicKey());
        assertArrayEquals(wallet1.getKeyPair().getPrivate().getEncoded(), wallet2.getKeyPair().getPrivate().getEncoded());
        assertArrayEquals(wallet1.getKeyPair().getPublic().getEncoded(), wallet2.getKeyPair().getPublic().getEncoded());
    }
}
