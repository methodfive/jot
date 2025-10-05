package com.method5.jot.examples.extrinsic;

import com.method5.jot.examples.Config;
import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UtilityForceBatchExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(UtilityForceBatchExample.class);

    public static void main(String[] args) throws Exception {
        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api, wallet.getSigner());
        }
    }

    private static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
        // Calls to batch
        List<Call> innerCalls = List.of(
                api.tx().system().remark("test"),
                api.tx().system().remark("test2"));

        Call call = api.tx().utility().forceBatch(innerCalls);

        String hash = call.signAndSend(signingProvider);

        logger.info("Extrinsic hash: {}", hash);
    }
}
