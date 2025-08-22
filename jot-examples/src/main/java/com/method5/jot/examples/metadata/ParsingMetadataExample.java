package com.method5.jot.examples.metadata;

import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;

import java.util.Arrays;

public class ParsingMetadataExample {
    public static void main(String[] args) {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            CallIndexResolver resolver = client.getResolver();
            MetadataV14 metadata = client.getMetadata();

            System.out.println("Metadata V14 retrieved and parsed");
            System.out.println(metadata.getPallets().size() + " pallets");
            System.out.println(metadata.getExtrinsics().size() + " extrinsic");
            System.out.println(metadata.getTypes().size() + " types");

            System.out.println("Call index for System.remark: " + Arrays.toString(resolver.resolveCallIndex("System", "remark")));
            System.out.println("Call index for Balances.transferAll: " + Arrays.toString(resolver.resolveCallIndex("Balances", "transfer_all")));

        }
    }
}
