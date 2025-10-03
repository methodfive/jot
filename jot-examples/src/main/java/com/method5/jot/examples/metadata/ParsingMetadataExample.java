package com.method5.jot.examples.metadata;

import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.rpc.PolkadotRpcClient;
import com.method5.jot.examples.ExampleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ParsingMetadataExample {
    private static final Logger logger = LoggerFactory.getLogger(ParsingMetadataExample.class);

    public static void main(String[] args) {
        try (PolkadotRpcClient client = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {

            CallIndexResolver resolver = client.getResolver();
            MetadataV14 metadata = client.getMetadata();

            logger.info("Metadata V14 retrieved and parsed");
            logger.info("{} pallets", metadata.getPallets().size());
            logger.info("{} extrinsic", metadata.getExtrinsics().size());
            logger.info("{} types", metadata.getTypes().size());

            logger.info("Call index for System.remark: {}", Arrays.toString(resolver.resolveCallIndex("System", "remark")));
            logger.info("Call index for Balances.transferAll: {}", Arrays.toString(resolver.resolveCallIndex("Balances", "transfer_all")));
        }
    }
}
