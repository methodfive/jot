package com.method5.jot.examples.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.method5.jot.rpc.PolkadotWsClient;
import com.method5.jot.examples.ExampleConstants;

public class ConnectToWssExample {
    public static void main(String[] args) throws Exception {
        // Connect to WSS rpc server
        try (PolkadotWsClient client = new PolkadotWsClient(new String[] { ExampleConstants.WSS_RPC_SERVER }, 1000)) {

            // Manual rpc call (see 'extrinsic' package for pre-build call examples)
            JsonNode result = client.send("rpc_methods", JsonNodeFactory.instance.arrayNode());

            System.out.println(result);
        }
    }
}
