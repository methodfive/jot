package com.method5.jot.rpc;

/**
 * SubscriptionType — enum for subscription type in the Jot SDK. Provides RPC client / JSON‑RPC
 * integration.
 */
public enum SubscriptionType {
    LATEST_HEAD("chain_subscribeNewHead","chain_unsubscribeNewHead"),
    ALL_HEADS("chain_subscribeAllHeads","chain_unsubscribeAllHeads"),
    BEST_HEAD("chain_subscribeNewHeads","chain_unsubscribeNewHead"),
    FINALIZED_HEAD("chain_subscribeFinalizedHeads","chain_unsubscribeFinalizedHeads"),
    RUNTIME_VERSION("chain_subscribeRuntimeVersion","chain_unsubscribeRuntimeVersion");

    private String subscribeMethod;
    private String unsubscribeMethod;

    SubscriptionType(String subscribeMethod, String unsubscribeMethod) {
        this.subscribeMethod = subscribeMethod;
        this.unsubscribeMethod = unsubscribeMethod;
    }

    public String getSubscribeMethod() {
        return subscribeMethod;
    }

    public void setSubscribeMethod(String subscribeMethod) {
        this.subscribeMethod = subscribeMethod;
    }

    public String getUnsubscribeMethod() {
        return unsubscribeMethod;
    }

    public void setUnsubscribeMethod(String unsubscribeMethod) {
        this.unsubscribeMethod = unsubscribeMethod;
    }
}
