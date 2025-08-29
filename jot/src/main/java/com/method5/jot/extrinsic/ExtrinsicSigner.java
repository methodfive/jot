package com.method5.jot.extrinsic;

import com.method5.jot.crypto.Hasher;
import com.method5.jot.wallet.SS58;
import com.method5.jot.query.ChainRpc;
import com.method5.jot.query.model.SignedBlock;
import com.method5.jot.rpc.PolkadotClient;
import com.method5.jot.scale.ScaleWriter;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.entity.Mortality;
import com.method5.jot.query.SystemRpc;
import com.method5.jot.util.HexUtil;

import java.math.BigInteger;

/**
 * ExtrinsicSigner — class for extrinsic signer in the Jot SDK. Provides RPC client / JSON‑RPC
 * integration; key management and signing; extrinsic construction and submission.
 */
public class ExtrinsicSigner {
    public static byte[] signAndBuild(
            PolkadotClient client,
            SigningProvider signingProvider,
            byte[] callData
    ) throws Exception {
        byte[] finalizedBlockHash = ChainRpc.getFinalizedHead(client);
        SignedBlock block = ChainRpc.getBlock(client, HexUtil.bytesToHex(finalizedBlockHash));
        int blockNumber = block.getBlock().getHeader().getNumber();

        return signAndBuild(
                callData,
                SystemRpc.accountNextIndex(client, SS58.encode(signingProvider.getPublicKey(), 0)),
                BigInteger.ZERO,
                signingProvider,
                Mortality.mortal(blockNumber),
                client.getChainSpec().getSpecVersion(),
                client.getChainSpec().getTransactionVersion(),
                client.getChainSpec().getGenesisHash(),
                ChainRpc.getBlockHash(client, blockNumber));
    }

    public static byte[] signAndBuild(
            byte[] callData,
            BigInteger nonce,
            BigInteger tip,
            SigningProvider signingProvider,
            Mortality mortality,
            int specVersion,
            int transactionVersion,
            byte[] genesisHash,
            byte[] blockHash
    ) {
        try {
            byte[] rawPayload = generateSigningPayload(false, callData, nonce, tip, mortality, specVersion, transactionVersion, genesisHash, blockHash);

            byte[] signature = signingProvider.sign(rawPayload);

            return SignedExtrinsicBuilder.build(callData, signingProvider, mortality, signature, nonce, tip);

        } catch (Exception e) {
            throw new RuntimeException("Failed to sign and build extrinsic", e);
        }
    }

    public static byte[] generateSigningPayload(boolean includeCallDataLength, byte[] callData, BigInteger nonce, BigInteger tip, Mortality mortality, int specVersion, int transactionVersion, byte[] genesisHash, byte[] blockHash) {
        // Construct signing payload
        ScaleWriter payload = new ScaleWriter();

        if(includeCallDataLength) {
            payload.writeCompact(BigInteger.valueOf(callData.length));
        }

        payload.writeBytes(callData);

        payload.writeBytes(mortality.encode());
        payload.writeCompact(nonce);
        payload.writeCompact(tip);

        payload.writeByte(0); //mode

        payload.writeInt(specVersion);
        payload.writeInt(transactionVersion);
        payload.writeBytes(genesisHash);
        payload.writeBytes(blockHash);

        payload.writeByte(0); //metadata hash512 (len)

        byte[] rawPayload = payload.toByteArray();

        return rawPayload.length > 256 ? Hasher.hash256(rawPayload) : rawPayload;
    }
}
