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

public class BalancesTransferAllExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(BalancesTransferAllExample.class);

    public static void main(String[] args) throws Exception {
        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api, wallet.getSigner());
        }
    }

    public static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
        logger.info("Balances Transfer Example");
        logger.info("------------------------");

        // Destination address
        AccountId destination = AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz");
        // Keep alive
        boolean keepAlive = true;

        Call call = api.tx().balances().transferAll(destination, keepAlive);

        String hash = call.signAndSend(signingProvider);

        logger.info("Extrinsic hash: {}", hash);
    }
}
