package com.method5.jot.examples.query;

import com.method5.jot.query.StateRpc;
import com.method5.jot.query.model.RuntimeVersion;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;

public class QueryRuntimeVersionExample {
    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            RuntimeVersion runtimeVersion = StateRpc.runtimeVersion(client);
            System.out.println("Spec name: " + runtimeVersion.getSpecName());
            System.out.println("Impl name: " + runtimeVersion.getImplName());
            System.out.println("Transaction version: " + runtimeVersion.getTransactionVersion());
            System.out.println("Impl version: " + runtimeVersion.getImplVersion());
            System.out.println("Spec version: " + runtimeVersion.getSpecVersion());
            System.out.println("Authoring version: " + runtimeVersion.getAuthoringVersion());
        }
    }
}
