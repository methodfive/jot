package com.method5.jot.entity;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MultiSignatureTest {
    @Test
    void testEncodeDecodeEd25519() {
        MultiSignature multiSignature = new MultiSignature(Wallet.KeyType.ED25519, HexUtil.bytesToHex(new byte[64]));

        MultiSignature decoded = MultiSignature.decode(new ScaleReader(multiSignature.encode()));

        assertEquals(HexUtil.bytesToHex(new byte[64]), decoded.getSignature());
        assertEquals(Wallet.KeyType.ED25519, decoded.getType());
    }

    @Test
    void testEncodeDecodeEcdsa() {
        MultiSignature multiSignature = new MultiSignature(Wallet.KeyType.ECDSA, HexUtil.bytesToHex(new byte[64]));

        MultiSignature decoded = MultiSignature.decode(new ScaleReader(multiSignature.encode()));

        assertEquals(HexUtil.bytesToHex(new byte[64]), decoded.getSignature());
        assertEquals(Wallet.KeyType.ECDSA, decoded.getType());
    }
}
