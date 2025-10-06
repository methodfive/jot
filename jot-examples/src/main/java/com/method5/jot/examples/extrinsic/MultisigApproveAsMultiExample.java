package com.method5.jot.examples.extrinsic;

import com.method5.jot.entity.Timepoint;
import com.method5.jot.examples.Config;
import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.wallet.Wallet;
import com.method5.jot.entity.Weight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class MultisigApproveAsMultiExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(MultisigApproveAsMultiExample.class);

    public static void main(String[] args) throws Exception {
        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api, wallet.getSigner());
        }
    }

    public static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
        logger.info("Multisig Approve As Multi Example");
        logger.info("------------------------");

        // Multisig participants
        List<AccountId> signers = Arrays.asList(
                AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz"),
                AccountId.fromSS58("12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG")
        );
        // Threshold
        int threshold = 2;
        // Inner call to approve
        Call innerCall = api.tx().system().remark("test");
        // Timepoint
        Timepoint timepoint = null;
        // Max Weight
        Weight maxWeight = new Weight(BigInteger.ZERO, BigInteger.ZERO);

        Call call = api.tx().multisig().approveAsMulti(threshold, signers, timepoint, innerCall, maxWeight);

        String hash = call.signAndSend(signingProvider);

        logger.info("Extrinsic hash: {}", hash);
    }
}
