package com.method5.jot.examples.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.method5.jot.rpc.RpcException;
import com.method5.jot.util.ExampleBase;
import com.method5.jot.examples.Config;
import com.method5.jot.rpc.PolkadotWs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class ManualQueryExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(ManualQueryExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    public static void execute(PolkadotWs api) throws RpcException {
        logger.info("Manual Query Example");
        logger.info("------------------------");

        // Various manual rpc call examples

        // Request chain name
        String chain = api.send("system_chain", JsonNodeFactory.instance.arrayNode()).asText();
        logger.info("Found chain: {}", chain);

        // Request chain version
        String chainVersion = api.send("system_version", JsonNodeFactory.instance.arrayNode()).asText();
        logger.info("Found chain version: {}", chainVersion);

        // Request nonce for a specific wallet
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode params = objectMapper.createArrayNode();
        params.add("12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG");

        BigInteger nonce = api.send("system_accountNextIndex", params).bigIntegerValue();
        logger.info("Found nonce for 12T8sPbDjkP3MafjbKJhZAzHGXMPNDEMqc6H96bvfi5AJGkG: {}", nonce.intValue());

        // A full list of available extrinsic can be found here: https://polkadot.js.org/docs/substrate/extrinsics/
    }
}
