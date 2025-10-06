package com.method5.jot.examples.metadata;

import com.method5.jot.entity.metadata.MetadataV14;
import com.method5.jot.examples.Config;
import com.method5.jot.metadata.CallIndexResolver;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.util.ExampleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ParsingMetadataExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(ParsingMetadataExample.class);

    public static void main(String[] args) {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            execute(api);
        }
    }

    public static void execute(PolkadotWs api) {
        logger.info("Parsing Metadata Example");
        logger.info("------------------------");

        CallIndexResolver resolver = api.getResolver();
        MetadataV14 metadata = api.getMetadata();

        logger.info("Metadata V14 retrieved and parsed");
        logger.info("Found {} pallets", metadata.getPallets().size());
        logger.info("Found {} extrinsic", metadata.getExtrinsics().size());
        logger.info("Found {} types", metadata.getTypes().size());

        logger.info("Determined call index for System.remark: {}", Arrays.toString(resolver.resolveCallIndex("System", "remark")));
        logger.info("Determined call index for Balances.transferAll: {}", Arrays.toString(resolver.resolveCallIndex("Balances", "transfer_all")));
    }
}
