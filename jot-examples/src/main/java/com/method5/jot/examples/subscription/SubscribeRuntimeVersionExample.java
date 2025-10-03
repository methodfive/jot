package com.method5.jot.examples.subscription;

import com.method5.jot.query.model.RuntimeVersion;
import com.method5.jot.rpc.PolkadotWsClient;
import com.method5.jot.rpc.Subscription;
import com.method5.jot.rpc.SubscriptionType;
import com.method5.jot.examples.ExampleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SubscribeRuntimeVersionExample {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeRuntimeVersionExample.class);

    public static void main(String[] args) throws Exception {
        try (PolkadotWsClient client = new PolkadotWsClient(new String[] { ExampleConstants.WSS_RPC_SERVER }, 1000)) {
            Thread.sleep(100);

            CountDownLatch latch = new CountDownLatch(2);

            // Subscribe to updates to the runtime version
            Subscription<RuntimeVersion> subscription = new Subscription<>(
                    SubscriptionType.RUNTIME_VERSION,
                    client,
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
}
