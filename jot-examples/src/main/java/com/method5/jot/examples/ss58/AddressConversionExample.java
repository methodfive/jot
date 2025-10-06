package com.method5.jot.examples.ss58;

import com.method5.jot.util.ExampleBase;
import com.method5.jot.wallet.SS58;
import com.method5.jot.util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressConversionExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(AddressConversionExample.class);

    public static void main(String[] args) {
        execute();
    }

    public static void execute() {
        logger.info("Address Conversion Example");
        logger.info("------------------------");

        // Extract public key from polkadot address
        byte[] publicKey = SS58.decode("12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG");

        logger.info("Public key: {}", HexUtil.bytesToHex(publicKey));

        // Convert to polkadot address
        String polkadotAddress = SS58.encode(publicKey, 0);

        logger.info("Polkadot address: {}", polkadotAddress);

        // Convert to kusama address
        String kusamaAddress = SS58.encode(publicKey, 2);

        logger.info("Kusama address: {}", kusamaAddress);

        // Convert to substarte address
        String substrateAddress = SS58.encode(publicKey, 42);

        logger.info("Substrate address: {}", substrateAddress);
    }
}
