package com.method5.jot.examples.query;

import com.method5.jot.query.ChainRpc;
import com.method5.jot.query.model.BlockHeader;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.util.HexUtil;
import com.method5.jot.examples.ExampleConstants;

public class QueryBlockHeaderExample {
    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            byte[] blockHash = ChainRpc.getFinalizedHead(client);
            System.out.println("Finalized head hash512: " + HexUtil.bytesToHex(blockHash));

            BlockHeader blockHeader = ChainRpc.getHeader(client, HexUtil.bytesToHex(blockHash));
            System.out.println("Finalized block header: " + blockHeader);
        }
    }
}
