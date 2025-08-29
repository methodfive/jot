package com.method5.jot.query.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * SignedBlock — class for signed block in the Jot SDK. Provides key management and signing; types
 * and data models.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignedBlock {
    @JsonProperty("block")
    private Block block;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Block {
        @JsonProperty("header")
        private BlockHeader header;

        @JsonProperty("extrinsics")
        private List<String> extrinsics;

        public BlockHeader getHeader() {
            return header;
        }

        public void setHeader(BlockHeader header) {
            this.header = header;
        }

        public List<String> getExtrinsics() {
            return extrinsics;
        }

        public void setExtrinsics(List<String> extrinsics) {
            this.extrinsics = extrinsics;
        }

        @Override
        public String toString() {
            return "Block{" +
                    "header=" + header +
                    ", extrinsics=" + extrinsics +
                    '}';
        }
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "SignedBlock{" +
                "block=" + block +
                '}';
    }
}