package com.method5.jot.examples.keys;

import com.method5.jot.examples.query.QueryBlockEventsExample;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class Ed25519WalletExample {
    private static final Logger logger = LoggerFactory.getLogger(Ed25519WalletExample.class);

    public static void main(String[] args) throws Exception {
        // Generate new wallet using ED25519 keys
        Wallet wallet = Wallet.generate(Wallet.KeyType.ED25519);

        logger.info("Mnemonic Phrase: Not available for ed25519");
        logger.info("Key type: {}", wallet.getKeyType());
        logger.info("Private key bytes: {}", HexUtil.bytesToHex(wallet.getPrivateKeyBytes()));
        logger.info("Public key: {}", HexUtil.bytesToHex(wallet.getSigner().getPublicKey()));
        logger.info("Substrate Address: {}", wallet.getAddress(42));
        logger.info("Polkadot Address: {}", wallet.getAddress(0));

        byte[] payload = "test".getBytes(StandardCharsets.UTF_8);

        // Sign message
        byte[] signature = wallet.sign(payload);

        // Verify signature
        boolean valid = wallet.verify(payload, signature);
    }
}
