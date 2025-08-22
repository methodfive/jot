package com.method5.jot.examples.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;

public class ConnectToHttpRpcExample {
    public static void main(String[] args) throws Exception {
        // Connect to HTTP rpc server
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 1000)) {

            // Manual rpc call (see 'extrinsic' package for pre-build call examples)
            JsonNode result = client.send("rpc_methods", JsonNodeFactory.instance.arrayNode());

            System.out.println(result);
        }
    }
}
