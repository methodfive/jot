package com.method5.jot.examples.query;

import com.method5.jot.query.ChainRpc;
import com.method5.jot.query.model.SignedBlock;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.util.HexUtil;
import com.method5.jot.examples.ExampleConstants;

public class QueryBlockExample {
    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            byte[] blockHash = ChainRpc.getBlockHash(client, 0);
            System.out.println("Genesis hash512: " + HexUtil.bytesToHex(blockHash));

            SignedBlock block = ChainRpc.getBlock(client, HexUtil.bytesToHex(blockHash));
            System.out.println("Genesis block: " + block);
        }
    }
}
