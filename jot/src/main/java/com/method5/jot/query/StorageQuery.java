package com.method5.jot.query;

import com.method5.jot.crypto.Hasher;
import com.method5.jot.events.EventParser;
import com.method5.jot.events.EventRecord;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.query.model.AccountInfo;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.util.HexUtil;
import com.method5.jot.util.UnitConverter;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * StorageQuery — class for storage query in the Jot SDK. Provides RPC client / JSON‑RPC
 * integration; chain state / storage queries.
 */
public class StorageQuery extends CallOrQuery {
    private static final Logger logger = LoggerFactory.getLogger(StorageQuery.class);

    public StorageQuery(Api api) {
        super(api);
    }

    public AccountInfo accountInfo(AccountId accountId) throws Exception {
        byte[] key = ArrayUtils.addAll(
                ArrayUtils.addAll(Hasher.twox128("System"), Hasher.twox128("Account")),
                Hasher.hash128Concat(accountId.getPublicKey())
        );

        String storageKeyHex = "0x" + HexUtil.bytesToHex(key);
        byte[] accountInfo = api.query().state().storage(storageKeyHex);
        if (accountInfo == null) return new AccountInfo();
        return AccountInfo.decode(new ScaleReader(accountInfo), api.getChainSpec().getTokenDecimals());
    }

    public BigDecimal assetBalance(BigInteger assetId, AccountId accountId, int decimals) throws Exception {
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
        byte[] accountInfo = api.query().state().storage(storageKeyHex);
        if (accountInfo == null) return BigDecimal.ZERO;

        ScaleReader scaleReader = new ScaleReader(accountInfo);
        return UnitConverter.fromPlanck(scaleReader.readU128(), decimals);
    }

    public List<EventRecord> systemEvents(String blockHash) throws Exception {
        byte[] key = ArrayUtils.addAll(
                Hasher.twox128("System"),
                Hasher.twox128("Events")
        );
        String storageKeyHex = "0x" + HexUtil.bytesToHex(key);

        byte[] eventBytes = api.query().state().storage(storageKeyHex, blockHash);
        return EventParser.parse(eventBytes, api.getMetadata(), api.getResolver());
    }
}
