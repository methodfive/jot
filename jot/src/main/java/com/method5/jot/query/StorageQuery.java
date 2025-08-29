package com.method5.jot.query;

import com.method5.jot.events.EventParser;
import com.method5.jot.events.EventRecord;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.query.model.AccountInfo;
import com.method5.jot.rpc.PolkadotClient;
import com.method5.jot.crypto.Hasher;
import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.HexUtil;
import com.method5.jot.util.UnitConverter;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * StorageQuery — class for storage query in the Jot SDK. Provides RPC client / JSON‑RPC
 * integration; chain state / storage queries.
 */
public class StorageQuery {
    public static AccountInfo getAccountInfo(PolkadotClient client, AccountId accountId) throws Exception {
        byte[] key = ArrayUtils.addAll(
                ArrayUtils.addAll(Hasher.twox128("System"), Hasher.twox128("Account")),
                Hasher.hash128Concat(accountId.getPublicKey())
        );

        String storageKeyHex = "0x" + HexUtil.bytesToHex(key);
        byte[] accountInfo = StateRpc.storage(client, storageKeyHex);
        if (accountInfo == null) return new AccountInfo();

        return AccountInfo.decode(new ScaleReader(accountInfo), client.getChainSpec().getTokenDecimals());
    }

    public static BigDecimal getAssetBalance(PolkadotClient client, BigInteger assetId, AccountId accountId, int decimals) throws Exception {
        ScaleWriter scaleWriter = new ScaleWriter();
        scaleWriter.writeInt(assetId.intValue());
        byte[] assetIdEncoded = scaleWriter.toByteArray();

        byte[] key = ArrayUtils.addAll(
                ArrayUtils.addAll(ArrayUtils.addAll(
                                Hasher.twox128("Assets"),
                                Hasher.twox128("Account")),
                        Hasher.hash128Concat(assetIdEncoded)),
                Hasher.hash128Concat(accountId.getPublicKey())
        );

        String storageKeyHex = "0x" + HexUtil.bytesToHex(key);
        byte[] accountInfo = StateRpc.storage(client, storageKeyHex);
        if (accountInfo == null) return BigDecimal.ZERO;

        ScaleReader scaleReader = new ScaleReader(accountInfo);
        return UnitConverter.fromPlanck(scaleReader.readU128(), decimals);
    }

    public static List<EventRecord> getSystemEvents(PolkadotClient client, String blockHash) throws Exception {
        byte[] key = ArrayUtils.addAll(
                Hasher.twox128("System"),
                Hasher.twox128("Events")
        );
        String storageKeyHex = "0x" + HexUtil.bytesToHex(key);

        byte[] eventBytes = StateRpc.storage(client, storageKeyHex, blockHash);
        return EventParser.parse(eventBytes, client.getMetadata(), client.getResolver());
    }
}
