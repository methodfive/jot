package com.method5.jot.examples.extrinsic;

import com.method5.jot.examples.Config;
import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class StakingBondExtraExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(StakingBondExtraExample.class);

    public static void main(String[] args) throws Exception {
        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api, wallet.getSigner());
        }
    }

    public static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
        logger.info("Staking Bond Extra Example");
        logger.info("------------------------");

        // Amount
        BigDecimal amount = new BigDecimal("0.01");

        Call call = api.tx().staking().bondExtra(amount);

        String hash = call.signAndSend(signingProvider);

        logger.info("Extrinsic hash: {}", hash);
    }
}
