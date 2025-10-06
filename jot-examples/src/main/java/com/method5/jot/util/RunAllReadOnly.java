package com.method5.jot.util;

import com.method5.jot.examples.Config;
import com.method5.jot.examples.extrinsic.*;
import com.method5.jot.examples.keys.Ed25519WalletExample;
import com.method5.jot.examples.keys.GenericWalletExample;
import com.method5.jot.examples.metadata.ParsingMetadataExample;
import com.method5.jot.examples.query.*;
import com.method5.jot.examples.scale.ScaleEncodingExample;
import com.method5.jot.examples.ss58.AddressConversionExample;
import com.method5.jot.examples.subscription.*;
import com.method5.jot.rpc.OfflineApi;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunAllReadOnly extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(RunAllReadOnly.class);

    public static void main(String[] args) throws Exception {
        logger.info("Executing all Jot examples");

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            runSafely("ManualQueryExample", () -> ManualQueryExample.execute(api));
            runSafely("QueryAccountAndBalanceExample", () -> QueryAccountAndBalanceExample.execute(api));
            runSafely("QueryBlockExample", () -> QueryBlockExample.execute(api));
            runSafely("QueryBlockHashExample", () -> QueryBlockHashExample.execute(api));
            runSafely("QueryBlockHeaderExample", () -> QueryBlockHeaderExample.execute(api));
            runSafely("QueryBlockEventsExample", () -> QueryBlockEventsExample.execute(api));
            runSafely("QueryFinalizedHeadExample", () -> QueryFinalizedHeadExample.execute(api));
            runSafely("QueryMetadataExample", () -> QueryMetadataExample.execute(api));
            runSafely("QueryNonceExample", () -> QueryNonceExample.execute(api));
            runSafely("QueryRuntimeVersionExample", () -> QueryRuntimeVersionExample.execute(api));
            runSafely("QuerySystemDetailsExample", () -> QuerySystemDetailsExample.execute(api));
            runSafely("SubscribeAllHeadsExample", () -> SubscribeAllHeadsExample.execute(api));
            runSafely("SubscribeBestHeadsExample", () -> SubscribeBestHeadsExample.execute(api));
            runSafely("SubscribeFinalizedHeadsExample", () -> SubscribeFinalizedHeadsExample.execute(api));
            runSafely("SubscribeLatestHeadsExample", () -> SubscribeLatestHeadsExample.execute(api));
            runSafely("SubscribeRuntimeVersionExample", () -> SubscribeRuntimeVersionExample.execute(api));
            runSafely("ParsingMetadataExample", () -> ParsingMetadataExample.execute(api));
        }

        runSafely("Ed25519WalletExample", Ed25519WalletExample::execute);
        runSafely("ScaleEncodingExample", ScaleEncodingExample::execute);
        runSafely("AddressConversionExample", AddressConversionExample::execute);
        runSafely("GenericWalletExample", GenericWalletExample::execute);

        Wallet wallet = Wallet.generate();
        try (OfflineApi api = new OfflineApi()) {
            runSafely("OfflineSigningExample", () -> OfflineSigningExample.execute(api, wallet.getSigner()));
        }

        logger.info("All examples finished.");
    }

    private static void runSafely(String name, RunAll.ThrowingRunnable task) {
        try {
            logger.info("→ Running {}", name);
            task.run();
            logger.info("✓ Completed {}", name);
        } catch (Exception e) {
            logger.error("✗ Failed {}: {}", name, e.getMessage(), e);
        }
    }
}
