package com.method5.jot.examples.query;

import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;
import com.method5.jot.query.SystemRpc;

import java.math.BigInteger;

public class QueryNonceExample {
    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            // Query nonce for wallet
            BigInteger nonce = SystemRpc.accountNextIndex(client, "13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz");

            System.out.println("Nonce: " + nonce);
        }
    }
}
