package com.method5.jot.examples.extrinsic;

import com.method5.jot.query.AuthorRpc;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.wallet.Wallet;
import com.method5.jot.examples.ExampleConstants;
import com.method5.jot.extrinsic.call.BalancesPallet;
import com.method5.jot.extrinsic.ExtrinsicSigner;
import com.method5.jot.extrinsic.call.UtilityPallet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UtilityForceBatchExample {
    public static void main(String[] args) throws Exception {
        // Load (or generate) new wallet
        Wallet wallet = Wallet.generate();

        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            // Build call data for Utility.forceBatch with two balance transfers included
            byte[] callData = UtilityPallet.forceBatch(
                    client.getResolver(),
                    new ArrayList<>(List.of(  // list of calls to execute
                            BalancesPallet.transferKeepAlive(
                                    client.getResolver(),
                                    AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz"), // destination address,
                                    new BigDecimal("0.001") // amount
                            ),
                            BalancesPallet.transferKeepAlive(
                                    client.getResolver(),
                                    AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz"), // destination address,
                                    new BigDecimal("0.001")         // amount
                            )))
            );

            // Create and sign extrinsic
            byte[] extrinsic = ExtrinsicSigner.signAndBuild(client,
                    wallet.getSigner(),
                    callData
            );

            // Submit extrinsic to RPC
            String hash = AuthorRpc.submitExtrinsic(client, extrinsic);

            System.out.println("Extrinsic hash: " + hash);
        }
    }
}
