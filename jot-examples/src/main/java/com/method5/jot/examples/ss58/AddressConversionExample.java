package com.method5.jot.examples.ss58;

import com.method5.jot.wallet.SS58;
import com.method5.jot.util.HexUtil;

public class AddressConversionExample {
    public static void main(String[] args) {
        // Extract public key from polkadot address
        byte[] publicKey = SS58.decode("12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG");

        System.out.println("Public key: " + HexUtil.bytesToHex(publicKey));

        // Convert to polkadot address
        String polkadotAddress = SS58.encode(publicKey, 0);

        System.out.println("Polkadot address: " + polkadotAddress);

        // Convert to kusama address
        String kusamaAddress = SS58.encode(publicKey, 2);

        System.out.println("Kusama address: " + kusamaAddress);

        // Convert to substarte address
        String substrateAddress = SS58.encode(publicKey, 42);

        System.out.println("Substrate address: " + substrateAddress);
    }
}
