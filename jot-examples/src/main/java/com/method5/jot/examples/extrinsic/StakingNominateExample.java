package com.method5.jot.examples.extrinsic;

import com.method5.jot.query.AuthorRpc;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.wallet.Wallet;
import com.method5.jot.examples.ExampleConstants;
import com.method5.jot.extrinsic.ExtrinsicSigner;
import com.method5.jot.extrinsic.call.StakingPallet;
import com.method5.jot.entity.MultiAddress;

import java.util.ArrayList;
import java.util.List;

public class StakingNominateExample {
    public static void main(String[] args) throws Exception {
        // Load (or generate) new wallet
        Wallet wallet = Wallet.generate();

        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            // Build call data for Staking.nominate
            byte[] callData = StakingPallet.nominate(
                    client.getResolver(),
                    new ArrayList<>(List.of(               // nomination targets
                        MultiAddress.fromSS58("114SUbKCXjmb9czpWTtS3JANSmNRwVa4mmsMrWYpRG1kDH5"),
                        MultiAddress.fromSS58("11uMPbeaEDJhUxzU4ZfWW9VQEsryP9XqFcNRfPdYda6aFWJ"),
                        MultiAddress.fromSS58("1737bipUqNUHYjUB5HCezyYqto5ZjFiMSXNAX8fWktnD5AS")
                    ))
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
