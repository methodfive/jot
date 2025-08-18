package com.method5.jot.spec;

import java.util.Arrays;

public class ChainSpec {
    private String id;
    private String name;
    private int specVersion;
    private int tokenDecimals;
    private String tokenSymbol;
    private int transactionVersion;
    private byte[] genesisHash;
    private byte[] metadata;
    private int ss58Prefix;

    public ChainSpec() {
        genesisHash = new byte[32];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTokenDecimals() {
        return tokenDecimals;
    }

    public void setTokenDecimals(int tokenDecimals) {
        this.tokenDecimals = tokenDecimals;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public byte[] getMetadata() {
        return metadata;
    }

    public int getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(int specVersion) {
        this.specVersion = specVersion;
    }

    public int getTransactionVersion() {
        return transactionVersion;
    }

    public void setTransactionVersion(int transactionVersion) {
        this.transactionVersion = transactionVersion;
    }

    public byte[] getGenesisHash() {
        return genesisHash;
    }

    public void setGenesisHash(byte[] genesisHash) {
        this.genesisHash = genesisHash;
    }

    public void setMetadata(byte[] metadata) {
        this.metadata = metadata;
    }

    public int getSs58Prefix() {
        return ss58Prefix;
    }

    public void setSs58Prefix(int ss58Prefix) {
        this.ss58Prefix = ss58Prefix;
    }

    @Override
    public String toString() {
        return "ChainSpec{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", specVersion=" + specVersion +
                ", tokenDecimals=" + tokenDecimals +
                ", tokenSymbol='" + tokenSymbol + '\'' +
                ", transactionVersion=" + transactionVersion +
                ", genesisHash=" + Arrays.toString(genesisHash) +
                ", metadata=" + Arrays.toString(metadata) +
                ", ss58Prefix=" + ss58Prefix +
                '}';
    }
}
