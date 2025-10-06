package com.method5.jot.examples.extrinsic;

import com.method5.jot.examples.Config;
import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StakingPayoutStakersExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(StakingPayoutStakersExample.class);

    public static void main(String[] args) throws Exception {
        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api, wallet.getSigner());
        }
    }

    public static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
        logger.info("Payout Stakers Example");
        logger.info("------------------------");

        // Stash
        AccountId stash = AccountId.fromSS58("114SUbKCXjmb9czpWTtS3JANSmNRwVa4mmsMrWYpRG1kDH5");
        // Era
        int era = 1872;

        Call call = api.tx().staking().payoutStakers(stash, era);

        String hash = call.signAndSend(signingProvider);

        logger.info("Extrinsic hash: {}", hash);
    }
}
