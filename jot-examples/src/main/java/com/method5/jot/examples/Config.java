package com.method5.jot.examples;

import java.util.List;

public class Config {
    // Mnemonic phrase for Jot examples (defaults to //Alice)
    public static final String MNEMONIC_PHRASE = "bottom drive obey lake curtain smoke basket hold race lonely fit walk";

    // RPC servers for Jot examples
    public static final String[] WSS_SERVER = List.of(
            "wss://westend.api.onfinality.io/public-ws",
            "wss://westend.public.curie.radiumblock.co/ws")
            .toArray(String[]::new);

    public static final String[] ASSET_HUB_WSS_SERVER = List.of(
            "wss://asset-hub-westend-rpc.n.dwellir.com",
            "wss://asset-hub-westend.rpc.permanence.io")
            .toArray(String[]::new);
}
