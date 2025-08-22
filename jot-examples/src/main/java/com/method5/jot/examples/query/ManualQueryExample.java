package com.method5.jot.examples.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;

import java.math.BigInteger;

public class ManualQueryExample {
    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {
            // Various manual rpc call examples

            // Request chain name
            String chain = client.send("system_chain", JsonNodeFactory.instance.arrayNode()).asText();
            System.out.println("Chain: " + chain);

            // Request chain version
            String chainVersion = client.send("system_version", JsonNodeFactory.instance.arrayNode()).asText();
            System.out.println("Chain Version: " + chainVersion);

            // Request nonce for a specific wallet
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode params = objectMapper.createArrayNode();
            params.add("12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG");

            BigInteger nonce = client.send("system_accountNextIndex", params).bigIntegerValue();
            System.out.println("Nonce for 12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG: " + nonce.intValue());

            // A full list of available extrinsic can be found here: https://polkadot.js.org/docs/substrate/extrinsics/
        }
    }
}
