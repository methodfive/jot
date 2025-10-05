package com.method5.jot.extrinsic.call;

import com.method5.jot.extrinsic.ExtrinsicResult;
import com.method5.jot.extrinsic.ExtrinsicSigner;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.HexUtil;

/**
 * Call — Base class for all calls (pallet extrinsics) in the Jot SDK.
 */
public final class Call {
    private final Api api;
    private final byte[] callData;

    public Call(Api api, byte[] callData) {
        this.api = api;
        this.callData = callData;
    }

    public String signAndSend(SigningProvider signer) throws Exception {
        byte[] extrinsic = ExtrinsicSigner.signAndBuild(api,
                signer,
                callData
        );
        return api.query().author().submitExtrinsic(extrinsic);
    }

    public String sign(SigningProvider signer) throws Exception {
        byte[] extrinsic = ExtrinsicSigner.signAndBuild(api,
                signer,
                callData
        );
        return HexUtil.bytesToHex(extrinsic);
    }

    public ExtrinsicResult signAndWaitForResults(SigningProvider signer) throws Exception {
        return signAndWaitForResults(signer, PolkadotWs.Confirmation.BEST, 10000);
    }

    public ExtrinsicResult signAndWaitForResults(SigningProvider signer, PolkadotWs.Confirmation confirmation, long timeoutInMs) throws Exception {
        if(!(api instanceof PolkadotWs)) {
            throw new UnsupportedOperationException("WsApi instance required");
        }

        byte[] extrinsic = ExtrinsicSigner.signAndBuild(api,
                signer,
                callData
        );
        return ((PolkadotWs)api).submitAndWaitForExtrinsic(extrinsic, confirmation, timeoutInMs);
    }

    public byte[] callData() {
        return callData;
    }
}
