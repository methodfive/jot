package com.method5.jot.rpc;

/**
 * RpcException — class for rpc exception in the Jot SDK. Provides RPC client / JSON‑RPC
 * integration.
 */
public class RpcException extends Exception {
    public RpcException(String error) {
        super(error);
    }

    public RpcException(String error, Exception e) {
        super(error, e);
    }
}
