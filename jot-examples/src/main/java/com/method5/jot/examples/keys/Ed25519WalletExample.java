package com.method5.jot.examples.keys;

import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;

import java.nio.charset.StandardCharsets;

public class Ed25519WalletExample {
    public static void main(String[] args) throws Exception {
        // Generate new wallet using ED25519 keys
        Wallet wallet = Wallet.generate(Wallet.KeyType.ED25519);

        System.out.println("Mnemonic Phrase: Not available for ed25519");
        System.out.println("Key type: " + wallet.getKeyType());
        System.out.println("Private key bytes: " + HexUtil.bytesToHex(wallet.getPrivateKeyBytes()));
        System.out.println("Public key: " + HexUtil.bytesToHex(wallet.getSigner().getPublicKey()));
        System.out.println("Substrate Address: " + wallet.getAddress(42));
        System.out.println("Polkadot Address: " + wallet.getAddress(0));

        byte[] payload = "test".getBytes(StandardCharsets.UTF_8);

        // Sign message
        byte[] signature = wallet.sign(payload);

        // Verify signature
        boolean valid = wallet.verify(payload, signature);
    }
}
