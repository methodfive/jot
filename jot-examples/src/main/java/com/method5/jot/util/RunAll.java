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
            runSafely("ManualQueryExample", () -> ManualQueryExample.execute(api));
            runSafely("QueryAccountAndBalanceExample", () -> QueryAccountAndBalanceExample.execute(api));
            runSafely("QueryBlockExample", () -> QueryBlockExample.execute(api));
            runSafely("QueryBlockHashExample", () -> QueryBlockHashExample.execute(api));
            runSafely("QueryBlockHeaderExample", () -> QueryBlockHeaderExample.execute(api));
            runSafely("QueryBlockEventsExample", () -> QueryBlockEventsExample.execute(api));
            runSafely("QueryFeeInfoExample", () -> QueryFeeInfoExample.execute(api, wallet.getSigner()));
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
            runSafely("ReceiveExtrinsicResultExample", () -> ReceiveExtrinsicResultExample.execute(api, wallet.getSigner()));
            runSafely("BalancesTransferAllowDeathExample", () -> BalancesTransferAllowDeathExample.execute(api, wallet.getSigner()));
            runSafely("BalancesTransferKeepAliveExample", () -> BalancesTransferKeepAliveExample.execute(api, wallet.getSigner()));
            runSafely("ConvictionVotingVoteExample", () -> ConvictionVotingVoteExample.execute(api, wallet.getSigner()));
            runSafely("ConvictionVotingRemoveVoteExample", () -> ConvictionVotingRemoveVoteExample.execute(api, wallet.getSigner()));
            runSafely("MultisigAsMultiExample", () -> MultisigAsMultiExample.execute(api, wallet.getSigner()));
            runSafely("MultisigApproveAsMultiExample", () -> MultisigApproveAsMultiExample.execute(api, wallet.getSigner()));
            runSafely("StakingBondExample", () -> StakingBondExample.execute(api, wallet.getSigner()));
            runSafely("StakingChillExample", () -> StakingChillExample.execute(api, wallet.getSigner()));
            runSafely("StakingNominateExample", () -> StakingNominateExample.execute(api, wallet.getSigner()));
            runSafely("StakingPayoutStakersExample", () -> StakingPayoutStakersExample.execute(api, wallet.getSigner()));
            runSafely("StakingRebondExample", () -> StakingRebondExample.execute(api, wallet.getSigner()));
            runSafely("StakingUnbondExample", () -> StakingUnbondExample.execute(api, wallet.getSigner()));
            runSafely("SystemRemarkExample", () -> SystemRemarkExample.execute(api, wallet.getSigner()));
            runSafely("UtilityBatchExample", () -> UtilityBatchExample.execute(api, wallet.getSigner()));
            runSafely("UtilityBatchAllExample", () -> UtilityBatchAllExample.execute(api, wallet.getSigner()));
            runSafely("UtilityForceBatchExample", () -> UtilityForceBatchExample.execute(api, wallet.getSigner()));
            runSafely("ParsingMetadataExample", () -> ParsingMetadataExample.execute(api));
        }

        runSafely("Ed25519WalletExample", Ed25519WalletExample::execute);
        runSafely("ScaleEncodingExample", ScaleEncodingExample::execute);
        runSafely("AddressConversionExample", AddressConversionExample::execute);
        runSafely("GenericWalletExample", GenericWalletExample::execute);

        try (OfflineApi api = new OfflineApi()) {
            runSafely("OfflineSigningExample", () -> OfflineSigningExample.execute(api, wallet.getSigner()));
        }

        logger.info("All examples finished.");
    }

    private static void runSafely(String name, ThrowingRunnable task) {
        try {
            logger.info("→ Running {}", name);
            task.run();
            logger.info("✓ Completed {}", name);
        } catch (Exception e) {
            logger.error("✗ Failed {}: {}", name, e.getMessage(), e);
        }
    }
}