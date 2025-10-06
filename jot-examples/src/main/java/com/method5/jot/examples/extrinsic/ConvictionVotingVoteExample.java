package com.method5.jot.examples.extrinsic;

import com.method5.jot.examples.Config;
import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.extrinsic.model.AccountVote;
import com.method5.jot.extrinsic.model.Conviction;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class ConvictionVotingVoteExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(ConvictionVotingVoteExample.class);

    public static void main(String[] args) throws Exception {
        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api, wallet.getSigner());
        }
    }

    public static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
        logger.info("Conviction Voting Vote Example");
        logger.info("------------------------");

        // Referendum index
        int referendumIndex = 12345;
        // Vote (Standard, split or split/abstain)
        AccountVote vote = AccountVote.standard(true, // vote
                Conviction.NONE, // conviction
                new BigDecimal("0.001") // vote amount
        );

        Call call = api.tx().convictionVoting().vote(referendumIndex, vote);

        String hash = call.signAndSend(signingProvider);

        logger.info("Extrinsic hash: {}", hash);
    }
}
