package com.method5.jot.examples.query;

import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;
import com.method5.jot.query.SystemRpc;
import com.method5.jot.query.model.ChainType;
import com.method5.jot.query.model.SystemHealth;
import com.method5.jot.query.model.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuerySystemDetailsExample {
    private static final Logger logger = LoggerFactory.getLogger(QuerySystemDetailsExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            String chain = SystemRpc.chain(client);
            logger.info("Chain: {}", chain);

            String name = SystemRpc.name(client);
            logger.info("Node name: {}", name);

            ChainType chainType = SystemRpc.chainType(client);
            logger.info("Chain type: {}", chainType.getValue());

            SystemHealth systemHealth = SystemRpc.health(client);
            logger.info("System health: {}", systemHealth);

            String version = SystemRpc.version(client);
            logger.info("Node version: {}", version);

            SystemProperties properties = SystemRpc.properties(client);
            logger.info("System properties: {}", properties);
        }
    }
}
