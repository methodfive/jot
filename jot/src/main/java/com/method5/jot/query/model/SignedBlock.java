package com.method5.jot.query.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SignedBlock — class for signed block in the Jot SDK. Provides key management and signing; types
 * and data models.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignedBlock {
    @JsonProperty("block")
    private Block block;

    public SignedBlock() {
        this.block = new Block();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Block {

        public Block() {
            header = new BlockHeader();
            extrinsics = new ArrayList<>();
        }

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
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Block block = (Block) o;
            return Objects.equals(header, block.header) && Objects.equals(extrinsics, block.extrinsics);
        }

        @Override
        public int hashCode() {
            return Objects.hash(header, extrinsics);
        }
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SignedBlock that = (SignedBlock) o;
        return Objects.equals(block, that.block);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(block);
    }
}