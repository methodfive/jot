package com.method5.jot.examples.extrinsic;

import com.method5.jot.examples.Config;
import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.rpc.OfflineApi;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class OfflineSigningExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(OfflineSigningExample.class);

    private static BigInteger nonce = BigInteger.ZERO;

    public static void main(String[] args) throws Exception {
        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        // Create offline client
        try (OfflineApi api = new OfflineApi()) {
            execute(api, wallet.getSigner());
        }
    }

    public static void execute(OfflineApi api, SigningProvider signingProvider) throws Exception {
        logger.info("Offline Signing Example");
        logger.info("------------------------");

        // Call to submit and retrieve results for
        Call call = api.tx().system().remark("test");

        // Submit to RPC and wait for result
        String signedExtrinsic = call.signOffline(signingProvider, nonce);

        // Result
        logger.info("Created signed extrinsic for System.remark(\"test\"): {}", signedExtrinsic);
    }

    static
    {
        // Initialize metadata cache on disk if needed
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            api.initializeMetadata();
            nonce = api.query().system().accountNextIndex(Config.WALLET_ADDRESS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
