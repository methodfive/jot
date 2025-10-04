package com.method5.jot.query.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.method5.jot.util.HexToIntDeserializer;

import java.util.List;
import java.util.Objects;

/**
 * BlockHeader — class for block header in the Jot SDK. Provides types and data models.
 */
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
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Digest digest = (Digest) o;
            return Objects.equals(logs, digest.logs);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(logs);
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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BlockHeader that = (BlockHeader) o;
        return number == that.number && Objects.equals(parentHash, that.parentHash) && Objects.equals(stateRoot, that.stateRoot) && Objects.equals(extrinsicsRoot, that.extrinsicsRoot) && Objects.equals(digest, that.digest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentHash, number, stateRoot, extrinsicsRoot, digest);
    }
}