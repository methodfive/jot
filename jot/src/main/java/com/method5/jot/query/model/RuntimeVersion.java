package com.method5.jot.query.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RuntimeVersion — class for runtime version in the Jot SDK. Provides extrinsic construction and
 * submission; runtime metadata decoding; types and data models.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuntimeVersion {
    @JsonProperty("specName")
    private String specName;

    @JsonProperty("implName")
    private String implName;

    @JsonProperty("authoringVersion")
    private int authoringVersion;

    @JsonProperty("specVersion")
    private int specVersion;

    @JsonProperty("implVersion")
    private int implVersion;

    @JsonProperty("apis")
    private Object apis;

    @JsonProperty("transactionVersion")
    private int transactionVersion;

    @JsonProperty("stateVersion")
    private int stateVersion;

    public RuntimeVersion() {
    }

    public RuntimeVersion(String specName, int specVersion, int transactionVersion) {
        this.specName = specName;
        this.specVersion = specVersion;
        this.transactionVersion = transactionVersion;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getImplName() {
        return implName;
    }

    public void setImplName(String implName) {
        this.implName = implName;
    }

    public int getAuthoringVersion() {
        return authoringVersion;
    }

    public void setAuthoringVersion(int authoringVersion) {
        this.authoringVersion = authoringVersion;
    }

    public int getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(int specVersion) {
        this.specVersion = specVersion;
    }

    public int getImplVersion() {
        return implVersion;
    }

    public void setImplVersion(int implVersion) {
        this.implVersion = implVersion;
    }

    public Object getApis() {
        return apis;
    }

    public void setApis(Object apis) {
        this.apis = apis;
    }

    public int getTransactionVersion() {
        return transactionVersion;
    }

    public void setTransactionVersion(int transactionVersion) {
        this.transactionVersion = transactionVersion;
    }

    public int getStateVersion() {
        return stateVersion;
    }

    public void setStateVersion(int stateVersion) {
        this.stateVersion = stateVersion;
    }

    @Override
    public String toString() {
        return "RuntimeVersion{" +
                "specName='" + specName + '\'' +
                ", implName='" + implName + '\'' +
                ", authoringVersion=" + authoringVersion +
                ", specVersion=" + specVersion +
                ", implVersion=" + implVersion +
                ", transactionVersion=" + transactionVersion +
                ", stateVersion=" + stateVersion +
                '}';
    }
}