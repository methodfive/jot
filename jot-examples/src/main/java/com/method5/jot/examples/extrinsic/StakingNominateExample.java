package com.method5.jot.examples.extrinsic;

import com.method5.jot.examples.Config;
import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.wallet.Wallet;
import com.method5.jot.entity.MultiAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StakingNominateExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(StakingNominateExample.class);

    public static void main(String[] args) throws Exception {
        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api, wallet.getSigner());
        }
    }

    public static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
        logger.info("Staking Nominate Example");
        logger.info("------------------------");

        // Nomination targets
        List<MultiAddress> nominationTargets = new ArrayList<>(List.of(
                MultiAddress.fromSS58("114SUbKCXjmb9czpWTtS3JANSmNRwVa4mmsMrWYpRG1kDH5"),
                MultiAddress.fromSS58("11uMPbeaEDJhUxzU4ZfWW9VQEsryP9XqFcNRfPdYda6aFWJ"),
                MultiAddress.fromSS58("1737bipUqNUHYjUB5HCezyYqto5ZjFiMSXNAX8fWktnD5AS")
        ));

        Call call = api.tx().staking().nominate(nominationTargets);

        String hash = call.signAndSend(signingProvider);

        logger.info("Extrinsic hash: {}", hash);
    }
}
