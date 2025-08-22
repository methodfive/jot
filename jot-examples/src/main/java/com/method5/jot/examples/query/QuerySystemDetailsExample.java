package com.method5.jot.examples.query;

import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;
import com.method5.jot.query.SystemRpc;
import com.method5.jot.query.model.ChainType;
import com.method5.jot.query.model.SystemHealth;
import com.method5.jot.query.model.SystemProperties;

public class QuerySystemDetailsExample {
    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            String chain = SystemRpc.chain(client);
            System.out.println("Chain: " + chain);

            String name = SystemRpc.name(client);
            System.out.println("Node name: " + name);

            ChainType chainType = SystemRpc.chainType(client);
            System.out.println("Chain type: " + chainType.getValue());

            SystemHealth systemHealth = SystemRpc.health(client);
            System.out.println("System health: " + systemHealth);

            String version = SystemRpc.version(client);
            System.out.println("Node version: " + version);

            SystemProperties properties = SystemRpc.properties(client);
            System.out.println("System properties: " + properties);
        }
    }
}
