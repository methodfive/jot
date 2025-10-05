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

import java.math.BigDecimal;

public class BalancesTransferKeepAliveExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(BalancesTransferKeepAliveExample.class);

    public static void main(String[] args) throws Exception {
        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api, wallet.getSigner());
        }
    }

    private static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
        logger.info("Balances Transfer Keep Alive Example");

        // Destination address
        AccountId destination = AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz");
        // Amount
        BigDecimal amount = new BigDecimal("0.001");

        Call call = api.tx().balances().transferKeepAlive(destination, amount);

        String hash = call.signAndSend(signingProvider);

        logger.info("Extrinsic hash: {}", hash);
    }
}
