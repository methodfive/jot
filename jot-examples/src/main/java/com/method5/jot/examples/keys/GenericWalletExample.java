package com.method5.jot.examples.keys;

import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class GenericWalletExample {
    private static final Logger logger = LoggerFactory.getLogger(GenericWalletExample.class);

    public static void main(String[] args) throws Exception {
        // Generate new wallet using SR25519 keys
        Wallet wallet = Wallet.generate();

        logger.info("Mnemonic Phrase: {}", wallet.getMnemonic());
        logger.info("Key type: {}", wallet.getKeyType());
        logger.info("Seed: {}", HexUtil.bytesToHex(wallet.getSeed()));
        logger.info("Public key: {}", HexUtil.bytesToHex(wallet.getSigner().getPublicKey()));
        logger.info("Substrate Address: {}", wallet.getAddress(42));
        logger.info("Polkadot Address: {}", wallet.getAddress(0));

        // Load wallet from mnemonic phrase
        Wallet wallet2 = Wallet.fromMnemonic("vault kitten aunt legal vacant issue grape art verify chest fun shadow");

        // Load wallet from seed
        Wallet wallet3 = Wallet.fromSr25519Seed(HexUtil.hexToBytes("990913f068a6139997eedb0f11c2783fd41ab78f6f5c7934723050535c60b4cb"));

        byte[] payload = "test".getBytes(StandardCharsets.UTF_8);

        // Sign message
        byte[] signature = wallet.sign(payload);

        // Verify signature
        boolean valid = wallet.verify(payload, signature);
    }
}
