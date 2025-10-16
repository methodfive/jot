package com.method5.jot.examples;

import java.util.List;

public class Config {
    // Mnemonic phrase for Jot examples (defaults to 5FLiJC6HyT8f25aLtXtFpAeVPpa2jVFWPBhwLyearfXnCirq)
    public static final String WALLET_ADDRESS = "5FLiJC6HyT8f25aLtXtFpAeVPpa2jVFWPBhwLyearfXnCirq";
    public static final String MNEMONIC_PHRASE = "true this awkward beyond stand second garment throw bless two online hurry";

    // RPC servers for Jot examples
    public static final String[] WSS_SERVER = List.of(
            "wss://westend.api.onfinality.io/public-ws",
            "wss://westend.public.curie.radiumblock.co/ws")
            .toArray(String[]::new);

    public static final String[] ASSET_HUB_WSS_SERVER = List.of(
            "wss://asset-hub-westend-rpc.n.dwellir.com",
            "wss://asset-hub-westend.rpc.permanence.io")
            .toArray(String[]::new);

    public static final String[] FREQUENCY_WSS_SERVER = List.of(
        "ws://localhost:9944"
        ).toArray(String[]::new);
}
