package com.method5.jot.examples.extrinsic;

import com.method5.jot.examples.ExampleConstants;
import com.method5.jot.extrinsic.ExtrinsicResult;
import com.method5.jot.extrinsic.ExtrinsicSigner;
import com.method5.jot.extrinsic.call.BalancesPallet;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.rpc.PolkadotWsClient;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class ReceiveExtrinsicResultExample {
    private static final Logger logger = LoggerFactory.getLogger(ReceiveExtrinsicResultExample.class);

    public static void main(String[] args) throws Exception {
        // Load (or generate) new wallet
        Wallet wallet = Wallet.generate();

        try (PolkadotWsClient client = new PolkadotWsClient(new String[] { ExampleConstants.WSS_RPC_SERVER }, 30000)) {

            // Build call data for Balances.transferKeepAlive
            byte[] callData = BalancesPallet.transferKeepAlive(
                    client.getResolver(),
                    AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz"),
                    BigDecimal.valueOf(Long.parseLong("99999999999999999")) // very large amount that will fail
            );

            // Create and sign extrinsic
            byte[] extrinsic = ExtrinsicSigner.signAndBuild(client,
                    wallet.getSigner(),
                    callData
            );

            // Submit to RPC and wait for result
            ExtrinsicResult result = client.submitAndWaitForExtrinsic(extrinsic, PolkadotWsClient.Confirmation.BEST, 10000);

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
}
