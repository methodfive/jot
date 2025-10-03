package com.method5.jot.examples.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class ManualQueryExample {
    private static final Logger logger = LoggerFactory.getLogger(ManualQueryExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {
            // Various manual rpc call examples

            // Request chain name
            String chain = client.send("system_chain", JsonNodeFactory.instance.arrayNode()).asText();
            logger.info("Chain: {}", chain);

            // Request chain version
            String chainVersion = client.send("system_version", JsonNodeFactory.instance.arrayNode()).asText();
            logger.info("Chain Version: {}", chainVersion);

            // Request nonce for a specific wallet
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode params = objectMapper.createArrayNode();
            params.add("12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG");

            BigInteger nonce = client.send("system_accountNextIndex", params).bigIntegerValue();
            logger.info("Nonce for 12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG: {}", nonce.intValue());

            // A full list of available extrinsic can be found here: https://polkadot.js.org/docs/substrate/extrinsics/
        }
    }
}
