package com.method5.jot.examples.extrinsic;

import com.method5.jot.extrinsic.call.ConvictionVotingPallet;
import com.method5.jot.extrinsic.ExtrinsicSigner;
import com.method5.jot.extrinsic.model.AccountVote;
import com.method5.jot.extrinsic.model.Conviction;
import com.method5.jot.query.AuthorRpc;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.wallet.Wallet;
import com.method5.jot.examples.ExampleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class ConvictionVotingVoteExample {
    private static final Logger logger = LoggerFactory.getLogger(ConvictionVotingVoteExample.class);

    public static void main(String[] args) throws Exception {
        // Load (or generate) new wallet
        Wallet wallet = Wallet.generate();

        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            // Build call data for standard vote
            byte[] callData = ConvictionVotingPallet.vote(
                    client.getResolver(),
                    10234,                                        // referendum #
                    AccountVote.standard(true,                              // vote
                            Conviction.NONE,                                    // conviction
                            new BigDecimal("0.001") // vote amount
            ));

            // Build call data for split vote
            callData = ConvictionVotingPallet.vote(
                    client.getResolver(),
                    10234,                                       // referendum #
                    AccountVote.split(
                            new BigDecimal("100"),  // total aye
                            new BigDecimal("50")   //  total nay
                    ));

            // Build call data for split abstain vote
            callData = ConvictionVotingPallet.vote(
                    client.getResolver(),
                    10234,                                         // referendum #
                    AccountVote.splitAbstain(
                            new BigDecimal("100"),  //  total aye
                            new BigDecimal("50"),   //  total nay
                            new BigDecimal("26")    //  total abstain
                    ));

            // Create and sign extrinsic for the split_abstain vote
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
