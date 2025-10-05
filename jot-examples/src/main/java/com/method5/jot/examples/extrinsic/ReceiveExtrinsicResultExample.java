package com.method5.jot.examples.extrinsic;

import com.method5.jot.examples.Config;
import com.method5.jot.extrinsic.ExtrinsicResult;
import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiveExtrinsicResultExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(ReceiveExtrinsicResultExample.class);

    public static void main(String[] args) throws Exception {
        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api, wallet.getSigner());
        }
    }

    private static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
        // Call to submit and retrieve results for
        Call call = api.tx().system().remark("test");

        // Submit to RPC and wait for result
        ExtrinsicResult result = call.signAndWaitForResults(signingProvider);

        // Result
        logger.info("Successful: {}", result.isSuccess());

        // Individual events related to extrinsic
        logger.info("Events: {}", result.getEvents());

        // Failure reason (if it failed)
        if (!result.isSuccess() && result.getError() != null) {
            logger.error("Failed because: {}", result.getError().toHuman());
        }
    }
}
