package com.method5.jot.examples.extrinsic;

import com.method5.jot.query.AuthorRpc;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.wallet.Wallet;
import com.method5.jot.examples.ExampleConstants;
import com.method5.jot.extrinsic.call.BalancesPallet;
import com.method5.jot.extrinsic.ExtrinsicSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class BalancesTransferAllowDeathExample {
    private static final Logger logger = LoggerFactory.getLogger(BalancesTransferAllowDeathExample.class);

    public static void main(String[] args) throws Exception {
        // Load (or generate) new wallet
        Wallet wallet = Wallet.generate();

        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            // Build call data for Balances.transferAllowDeath
            byte[] callData = BalancesPallet.transferAllowDeath(
                    client.getResolver(),
                    AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz"), // destination address
                    new BigDecimal("0.001")         // amount
            );

            // Create and sign extrinsic
            byte[] extrinsic = ExtrinsicSigner.signAndBuild(client,
                    wallet.getSigner(),
                    callData
            );

            // Submit extrinsic to RPC
            String hash = AuthorRpc.submitExtrinsic(client, extrinsic);

            logger.info("Extrinsic hash: {}", hash);
        }
    }
}
