package com.method5.jot.examples.extrinsic;

import com.method5.jot.extrinsic.call.ConvictionVotingPallet;
import com.method5.jot.extrinsic.ExtrinsicSigner;
import com.method5.jot.query.AuthorRpc;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.wallet.Wallet;
import com.method5.jot.examples.ExampleConstants;

public class ConvictionVotingRemoveVoteExample {
    public static void main(String[] args) throws Exception {
        // Load (or generate) new wallet
        Wallet wallet = Wallet.generate();

        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            // Build call data for ConvictionVoting.undelegate
            byte[] callData = ConvictionVotingPallet.removeVote(
                    client.getResolver(),
                    null,  // class
                    12345          // referendum #
            );

            // Create and sign extrinsic for the split_abstain vote
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
