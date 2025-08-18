package com.method5.jot.rpc;

public class RpcException extends Exception {
    public RpcException(String error) {
        super(error);
    }

    public RpcException(String error, Exception e) {
        super(error, e);
    }
}
