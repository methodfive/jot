package com.method5.jot.rpc;

import com.method5.jot.query.model.BlockHeader;
import com.method5.jot.query.model.RuntimeVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public final class Subscriber {
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);

    private final PolkadotWs api;

    public Subscriber(PolkadotWs api) {
        this.api = api;
    }

    public Subscription<BlockHeader> allHeads(Consumer<BlockHeader> onMessage) throws Exception {
        return new Subscription<>(
                SubscriptionType.ALL_HEADS,
                api,
                onMessage
        );
    }

    public Subscription<BlockHeader> finalizedHeads(Consumer<BlockHeader> onMessage) throws Exception {
        return new Subscription<>(
                SubscriptionType.FINALIZED_HEAD,
                api,
                onMessage
        );
    }

    public Subscription<BlockHeader> bestHeads(Consumer<BlockHeader> onMessage) throws Exception {
        return new Subscription<>(
                SubscriptionType.BEST_HEAD,
                api,
                onMessage
        );
    }

    public Subscription<BlockHeader> latestHeads(Consumer<BlockHeader> onMessage) throws Exception {
        return new Subscription<>(
                SubscriptionType.LATEST_HEAD,
                api,
                onMessage
        );
    }

    public Subscription<RuntimeVersion> runtimeVersions(Consumer<RuntimeVersion> onMessage) throws Exception {
        return new Subscription<>(
                SubscriptionType.RUNTIME_VERSION,
                api,
                onMessage
        );
    }

}
