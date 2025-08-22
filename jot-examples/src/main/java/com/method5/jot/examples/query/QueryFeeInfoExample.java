package com.method5.jot.examples.query;

import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import com.method5.jot.examples.ExampleConstants;
import com.method5.jot.extrinsic.ExtrinsicSigner;
import com.method5.jot.extrinsic.call.SystemPallet;
import com.method5.jot.query.model.FeeInfo;
import com.method5.jot.query.PaymentRpc;

import java.nio.charset.StandardCharsets;

public class QueryFeeInfoExample {
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

            System.out.println("Weight (ref_time): " + feeInfo.getWeight().refTime());
            System.out.println("Weight (proof_size): " + feeInfo.getWeight().proofSize());
            System.out.println("Partial Fee: " + feeInfo.getPartialFee());
        }
    }
}
