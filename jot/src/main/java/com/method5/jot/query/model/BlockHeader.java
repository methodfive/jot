package com.method5.jot.query.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.method5.jot.util.HexToIntDeserializer;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockHeader {
    @JsonProperty("parentHash")
    private String parentHash;

    @JsonProperty("number")
    @JsonDeserialize(using = HexToIntDeserializer.class)
    private int number;

    @JsonProperty("stateRoot")
    private String stateRoot;

    @JsonProperty("extrinsicsRoot")
    private String extrinsicsRoot;

    @JsonProperty("digest")
    private Digest digest;

    public BlockHeader() {
    }

    public BlockHeader(String hash, int number) {
        this.number = number;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Digest {
        @JsonProperty("logs")
        public List<String> logs;

        public List<String> getLogs() {
            return logs;
        }

        public void setLogs(List<String> logs) {
            this.logs = logs;
        }

        @Override
        public String toString() {
            return "Digest{" +
                    "logs=" + logs +
                    '}';
        }
    }

    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStateRoot() {
        return stateRoot;
    }

    public void setStateRoot(String stateRoot) {
        this.stateRoot = stateRoot;
    }

    public String getExtrinsicsRoot() {
        return extrinsicsRoot;
    }

    public void setExtrinsicsRoot(String extrinsicsRoot) {
        this.extrinsicsRoot = extrinsicsRoot;
    }

    public Digest getDigest() {
        return digest;
    }

    public void setDigest(Digest digest) {
        this.digest = digest;
    }

    @Override
    public String toString() {
        return "BlockHeader{" +
                "parentHash='" + parentHash + '\'' +
                ", number=" + number +
                ", stateRoot='" + stateRoot + '\'' +
                ", extrinsicsRoot='" + extrinsicsRoot + '\'' +
                ", digest=" + digest +
                '}';
    }
}