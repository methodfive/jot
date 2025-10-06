package com.method5.jot.examples.subscription;

import com.method5.jot.examples.Config;
import com.method5.jot.query.model.RuntimeVersion;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.rpc.Subscription;
import com.method5.jot.util.ExampleBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SubscribeRuntimeVersionExample extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeRuntimeVersionExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 1000)) {
            execute(api);
        }
    }

    private static void execute(PolkadotWs api) throws Exception {
        logger.info("Subscribe Runtime Version Example");

        CountDownLatch latch = new CountDownLatch(2);

        // Subscribe to updates to the runtime version
        Subscription<RuntimeVersion> subscription = api.subscribe().runtimeVersions(
                runtimeVersion -> {
                    logger.info("New runtime version: {}", runtimeVersion);
                    latch.countDown();
                }
        );

        // Wait until 2 updates are received (or we timeout)
        latch.await(6500, TimeUnit.MILLISECONDS);

        // Unsubscribe
        subscription.unsubscribe();
    }
}
