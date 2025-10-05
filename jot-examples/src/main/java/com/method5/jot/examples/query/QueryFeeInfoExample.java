package com.method5.jot.examples.query;

import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import com.method5.jot.examples.Config;
import com.method5.jot.extrinsic.ExtrinsicSigner;
import com.method5.jot.query.model.FeeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryFeeInfoExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(QueryFeeInfoExample.class);

    public static void main(String[] args) throws Exception {
        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api, wallet.getSigner());
        }
    }

    private static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
        logger.info("Query Fee Info Example");

        // Build call for System.remark
        Call call = api.tx().system().remark("test");

        // Create and sign extrinsic
        byte[] extrinsic = ExtrinsicSigner.signAndBuild(api,
                signingProvider,
                call.callData()
        );

        // Query fee info for extrinsic
        FeeInfo feeInfo = api.query().payment().queryInfo("0x"+HexUtil.bytesToHex(extrinsic));

        logger.info("Weight (ref_time): {}", feeInfo.getWeight().refTime());
        logger.info("Weight (proof_size): {}", feeInfo.getWeight().proofSize());
        logger.info("Partial Fee: {}", feeInfo.getPartialFee());
    }
}
