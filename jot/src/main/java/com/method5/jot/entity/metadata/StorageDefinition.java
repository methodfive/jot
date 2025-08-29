package com.method5.jot.entity.metadata;

import com.method5.jot.entity.StorageEntryType;
import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * StorageDefinition — class for storage definition in the Jot SDK. Provides runtime metadata
 * decoding; chain state / storage queries; types and data models.
 */
public class StorageDefinition {
    private StorageType details;

    public static StorageDefinition decode(ScaleReader reader) {
        StorageDefinition definition = new StorageDefinition();
        StorageEntryType storageType = StorageEntryType.fromId(reader.readByte());

        if(storageType == StorageEntryType.PLAIN) {
            PlainStorageType plainStorage = new PlainStorageType();
            plainStorage.setType(reader.readCompact().intValue());
            definition.setDetails(plainStorage);
        } else if(storageType == StorageEntryType.MAP) {
            MapStorageType mapStorage = new MapStorageType();
            int hasherLen = reader.readCompact().intValue();
            for(int i = 0; i < hasherLen; i++) {
                mapStorage.getHasher().add((int)reader.readByte());
            }
            mapStorage.setKey(reader.readCompact().intValue());
            mapStorage.setValue(reader.readCompact().intValue());
            definition.setDetails(mapStorage);
        } else {
            throw new UnsupportedOperationException("Unsupported storage type: " + storageType);
        }
        return definition;
    }

    public byte[] encode() {
        ScaleWriter scaleWriter = new ScaleWriter();

        if(details instanceof PlainStorageType plainStorage) {
            scaleWriter.writeByte(0);
            scaleWriter.writeCompact(BigInteger.valueOf(plainStorage.getType()));
        } else {
            MapStorageType mapStorage = (MapStorageType) details;

            scaleWriter.writeByte(1);

            scaleWriter.writeCompact(BigInteger.valueOf(mapStorage.getHasher().size()));
            for(Integer hasher : mapStorage.getHasher()) {
                scaleWriter.writeByte(hasher);
            }
            scaleWriter.writeCompact(BigInteger.valueOf(mapStorage.getKey()));
            scaleWriter.writeCompact(BigInteger.valueOf(mapStorage.getValue()));
        }
        return scaleWriter.toByteArray();
    }

    public interface StorageType {
    }

    public static class PlainStorageType implements StorageType {
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "PlainStorageType{" +
                    "type=" + type +
                    '}';
        }
    }

    public static class MapStorageType implements StorageType {
        private List<Integer> hasher;
        private int key;
        private int value;

        public MapStorageType() {
            this.hasher = new ArrayList<>();
        }

        public List<Integer> getHasher() {
            return hasher;
        }

        public void setHasher(List<Integer> hasher) {
            this.hasher = hasher;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "MapStorageType{" +
                    "hasher=" + hasher +
                    ", key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    public StorageType getDetails() {
        return details;
    }

    public void setDetails(StorageType details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "StorageDefinition{" +
                "details=" + details +
                '}';
    }
}
