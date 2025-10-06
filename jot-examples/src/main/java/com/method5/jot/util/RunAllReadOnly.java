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

public class RunAllReadOnly {
    private static final Logger logger = LoggerFactory.getLogger(RunAllReadOnly.class);

    public static void main(String[] args) throws Exception {
        logger.info("Executing read-only Jot examples");

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            ManualQueryExample.execute(api);
            QueryAccountAndBalanceExample.execute(api);
            QueryBlockExample.execute(api);
            QueryBlockHashExample.execute(api);
            QueryBlockHeaderExample.execute(api);
            QueryBlockEventsExample.execute(api);
            QueryFinalizedHeadExample.execute(api);
            QueryMetadataExample.execute(api);
            QueryNonceExample.execute(api);
            QueryRuntimeVersionExample.execute(api);
            QuerySystemDetailsExample.execute(api);
            SubscribeAllHeadsExample.execute(api);
            SubscribeBestHeadsExample.execute(api);
            SubscribeFinalizedHeadsExample.execute(api);
            SubscribeLatestHeadsExample.execute(api);
            SubscribeRuntimeVersionExample.execute(api);
            ParsingMetadataExample.execute(api);
        }

        Ed25519WalletExample.execute();
        ScaleEncodingExample.execute();
        AddressConversionExample.execute();
        GenericWalletExample.execute();

        Wallet wallet = Wallet.generate();
        try (OfflineApi api = new OfflineApi()) {
            OfflineSigningExample.execute(api, wallet.getSigner());
        }
    }
}
