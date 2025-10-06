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

public class RunAll extends ExampleBase {
    private static final Logger logger = LoggerFactory.getLogger(RunAll.class);

    public static void main(String[] args) throws Exception {
        logger.info("Executing all Jot examples");

        Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

        try (PolkadotWs api = new PolkadotWs(Config.WSS_SERVER, 10000)) {
            ManualQueryExample.execute(api);
            QueryAccountAndBalanceExample.execute(api);
            QueryBlockExample.execute(api);
            QueryBlockHashExample.execute(api);
            QueryBlockHeaderExample.execute(api);
            QueryBlockEventsExample.execute(api);
            QueryFeeInfoExample.execute(api, wallet.getSigner());
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
            ReceiveExtrinsicResultExample.execute(api, wallet.getSigner());
            // BalancesTransferAllExample.execute(api, wallet.getSigner());
            BalancesTransferAllowDeathExample.execute(api, wallet.getSigner());
            BalancesTransferKeepAliveExample.execute(api, wallet.getSigner());
            ConvictionVotingVoteExample.execute(api, wallet.getSigner());
            ConvictionVotingRemoveVoteExample.execute(api, wallet.getSigner());
            MultisigAsMultiExample.execute(api, wallet.getSigner());
            MultisigApproveAsMultiExample.execute(api, wallet.getSigner());
            StakingBondExample.execute(api, wallet.getSigner());
            StakingChillExample.execute(api, wallet.getSigner());
            StakingNominateExample.execute(api, wallet.getSigner());
            StakingPayoutStakersExample.execute(api, wallet.getSigner());
            StakingRebondExample.execute(api, wallet.getSigner());
            StakingUnbondExample.execute(api, wallet.getSigner());
            SystemRemarkExample.execute(api, wallet.getSigner());
            UtilityBatchExample.execute(api, wallet.getSigner());
            UtilityBatchAllExample.execute(api, wallet.getSigner());
            UtilityForceBatchExample.execute(api, wallet.getSigner());
            ParsingMetadataExample.execute(api);
        }

        Ed25519WalletExample.execute();
        ScaleEncodingExample.execute();
        AddressConversionExample.execute();
        GenericWalletExample.execute();

        try (OfflineApi api = new OfflineApi()) {
            OfflineSigningExample.execute(api, wallet.getSigner());
        }
    }
}
