package com.method5.jot.examples.query;

import com.method5.jot.query.StateRpc;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.util.HexUtil;
import com.method5.jot.examples.ExampleConstants;

public class QueryMetadataExample {
    public static void main(String[] args) throws Exception {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            byte[] metadata = StateRpc.metadata(client);
            System.out.println("Metadata: " + HexUtil.bytesToHex(metadata));

            // See ParsingMetadataExample.java for parsing
        }
    }
}
