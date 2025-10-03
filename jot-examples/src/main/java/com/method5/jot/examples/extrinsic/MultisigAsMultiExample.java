package com.method5.jot.examples.extrinsic;

import com.method5.jot.query.AuthorRpc;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.wallet.Wallet;
import com.method5.jot.examples.ExampleConstants;
import com.method5.jot.extrinsic.ExtrinsicSigner;
import com.method5.jot.extrinsic.call.MultisigPallet;
import com.method5.jot.extrinsic.call.SystemPallet;
import com.method5.jot.entity.Weight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class MultisigAsMultiExample {
    private static final Logger logger = LoggerFactory.getLogger(MultisigAsMultiExample.class);

    public static void main(String[] args) throws Exception {
        // Load (or generate) new wallet
        Wallet wallet = Wallet.generate();

        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {
            // Suppose these are your multisig participants
            List<AccountId> signers = Arrays.asList(
                    AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz"),
                    AccountId.fromSS58("12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG")
            );

            // Original call to approve by multisig (could also be a batch)
            byte[] innerCall = SystemPallet.remark(
                    client.getResolver(),
                    "test".getBytes(StandardCharsets.UTF_8) // remark
            );

            // Build call data for Multisig.asMulti (final approval)
            byte[] callData = MultisigPallet.asMulti(
                    client.getResolver(),
                    2,                                    // threshold
                    signers,                                       // signers
                    null,                                          // timeout
                    innerCall,                                     // call to approve
                    new Weight(BigInteger.ZERO, BigInteger.ZERO)   // maxWeight
            );

            // Create and sign extrinsic
            byte[] extrinsic = ExtrinsicSigner.signAndBuild(client,
                    wallet.getSigner(),
                    callData
            );

            // Submit extrinsic to RPC
            String hash = AuthorRpc.submitExtrinsic(client, extrinsic);

            logger.info("Extrinsic hash: {}", hash);
        }
    }
}
