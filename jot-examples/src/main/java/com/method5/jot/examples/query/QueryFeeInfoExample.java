package com.method5.jot.examples.query;

import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import com.method5.jot.examples.ExampleConstants;
import com.method5.jot.extrinsic.ExtrinsicSigner;
import com.method5.jot.extrinsic.call.SystemPallet;
import com.method5.jot.query.model.FeeInfo;
import com.method5.jot.query.PaymentRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class QueryFeeInfoExample {
    private static final Logger logger = LoggerFactory.getLogger(QueryFeeInfoExample.class);

    public static void main(String[] args) throws Exception {
        // Load (or generate) new wallet
        Wallet wallet = Wallet.generate();

        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            // Build call data for System.remark
            byte[] callData = SystemPallet.remark(
                    client.getResolver(),
                    "test".getBytes(StandardCharsets.UTF_8)
            );

            // Create and sign extrinsic
            byte[] extrinsic = ExtrinsicSigner.signAndBuild(client,
                    wallet.getSigner(),
                    callData
            );

            // Query fee info for extrinsic
            FeeInfo feeInfo = PaymentRpc.queryInfo(client, "0x"+HexUtil.bytesToHex(extrinsic));

            logger.info("Weight (ref_time): {}", feeInfo.getWeight().refTime());
            logger.info("Weight (proof_size): {}", feeInfo.getWeight().proofSize());
            logger.info("Partial Fee: {}", feeInfo.getPartialFee());
        }
    }
}
