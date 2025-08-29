package com.method5.jot.entity.metadata;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PalletStorage — class for pallet storage in the Jot SDK. Provides runtime metadata decoding;
 * chain state / storage queries; types and data models; pallet call builders.
 */
public class PalletStorage {
    private String prefix;
    private int modifier;
    private List<StorageItem> items;

    public PalletStorage() {
        items = new ArrayList<>();
    }

    public static PalletStorage decode(ScaleReader reader) {
        PalletStorage storage = new PalletStorage();
        storage.setPrefix(reader.readString());

        int itemCount = reader.readCompact().intValue();
        for (int i = 0; i < itemCount; i++) {
            storage.getItems().add(StorageItem.decode(reader));
        }
        return storage;
    }

    public byte[] encode() {
        ScaleWriter scaleWriter = new ScaleWriter();
        scaleWriter.writeString(getPrefix());

        scaleWriter.writeCompact(BigInteger.valueOf(getItems().size()));
        for (StorageItem item : getItems()) {
            scaleWriter.writeBytes(item.encode());
        }
        return scaleWriter.toByteArray();
    }

    public static class StorageItem {
        private String name;
        private int modifier;
        private StorageDefinition definition;
        private byte[] fallback;
        private List<String> docs;

        public StorageItem() {
            docs = new ArrayList<>();
        }

        public static StorageItem decode(ScaleReader reader) {
            StorageItem item = new StorageItem();
            item.setName(reader.readString());
            item.setModifier(reader.readByte());
            item.setDefinition(StorageDefinition.decode(reader));
            item.setFallback(reader.readCompactBytes());
            item.setDocs(reader.readStringList());
            return item;
        }

        public byte[] encode() {
            ScaleWriter scaleWriter = new ScaleWriter();
            scaleWriter.writeString(getName());
            scaleWriter.writeByte(getModifier());
            scaleWriter.writeBytes(getDefinition().encode());
            scaleWriter.writeCompactBytes(getFallback());
            scaleWriter.writeStringList(getDocs());
            return scaleWriter.toByteArray();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getModifier() {
            return modifier;
        }

        public void setModifier(int modifier) {
            this.modifier = modifier;
        }

        public StorageDefinition getDefinition() {
            return definition;
        }

        public void setDefinition(StorageDefinition definition) {
            this.definition = definition;
        }

        public byte[] getFallback() {
            return fallback;
        }

        public void setFallback(byte[] fallback) {
            this.fallback = fallback;
        }

        public List<String> getDocs() {
            return docs;
        }

        public void setDocs(List<String> docs) {
            this.docs = docs;
        }

        @Override
        public String toString() {
            return "StorageItem{" +
                    "name='" + name + '\'' +
                    ", modifier=" + modifier +
                    ", definition=" + definition +
                    ", fallback=" + Arrays.toString(fallback) +
                    ", docs=" + docs +
                    '}';
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public List<StorageItem> getItems() {
        return items;
    }

    public void setItems(List<StorageItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "PalletStorage{" +
                "prefix='" + prefix + '\'' +
                ", modifier=" + modifier +
                ", items=" + items +
                '}';
    }
}