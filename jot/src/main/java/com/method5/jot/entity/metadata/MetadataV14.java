package com.method5.jot.entity.metadata;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MetadataV14 {
    private int magic;
    private int version;
    private List<MetadataType> types;
    private List<MetadataPallet> pallets;
    private int extrinsicType;
    private int extrinsicVersion;
    private List<MetadataExtrinsic> extrinsics;
    private int runtimeType;

    public MetadataV14() {
        types = new ArrayList<>();
        pallets = new ArrayList<>();
        extrinsics = new ArrayList<>();
    }

    public static MetadataV14 decode(byte[] input) {
        ScaleReader reader = new ScaleReader(input);
        MetadataV14 metadata = new MetadataV14();
        metadata.setMagic(Integer.reverseBytes(reader.readInt()));
        metadata.setVersion(reader.readByte());

        int typeCount = reader.readCompact().intValue();
        for (int i = 0; i < typeCount; i++) {
            metadata.getTypes().add(MetadataType.decode(reader));
        }

        int palletCount = reader.readCompact().intValue();
        for (int i = 0; i < palletCount; i++) {
            metadata.getPallets().add(MetadataPallet.decode(reader));
        }

        metadata.setExtrinsicType(reader.readCompact().intValue());
        metadata.setExtrinsicVersion(reader.readByte());

        int extrinsicCount = reader.readCompact().intValue();
        for (int i = 0; i < extrinsicCount; i++) {
            metadata.getExtrinsics().add(MetadataExtrinsic.decode(reader));
        }

        metadata.setRuntimeType(reader.readCompact().intValue());

        return metadata;
    }

    public byte[] encode() {
        ScaleWriter writer = new ScaleWriter();

        writer.writeInt(Integer.reverseBytes(this.magic));
        writer.writeByte(this.version);

        writer.writeCompact(BigInteger.valueOf(this.types.size()));
        for (MetadataType type : this.types) {
            writer.writeBytes(type.encode());
        }

        writer.writeCompact(BigInteger.valueOf(this.pallets.size()));
        for (MetadataPallet pallet : this.pallets) {
            writer.writeBytes(pallet.encode());
        }

        writer.writeCompact(BigInteger.valueOf(this.extrinsicType));
        writer.writeByte(this.extrinsicVersion);

        writer.writeCompact(BigInteger.valueOf(this.extrinsics.size()));
        for (MetadataExtrinsic extrinsic : this.extrinsics) {
            writer.writeBytes(extrinsic.encode());
        }

        writer.writeCompact(BigInteger.valueOf(this.runtimeType));

        return writer.toByteArray();
    }

    public CallInfo findCall(int palletIndex, int callIndex) {
        for (MetadataPallet pallet : this.pallets) {
            if (pallet.getIndex() == palletIndex && pallet.getCall() != null) {
                int typeId = pallet.getCall().getType();

                MetadataType callType = types.stream()
                        .filter(t -> t.getId() == typeId)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Call type not found: " + typeId));

                if(!(callType.getDef() instanceof MetadataTypeVariants variantsCall)) {
                    throw new IllegalStateException("Call type " + typeId + " is not a variant");
                }

                List<MetadataTypeVariants.Variant> variants = variantsCall.getVariants();
                if (callIndex >= variants.size()) {
                    throw new IllegalArgumentException("Invalid callIndex for " + pallet.getName());
                }

                MetadataTypeVariants.Variant variant = variants.stream()
                        .filter(t -> t.getIndex() == callIndex)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Call index not found: " + callIndex));

                CallInfo info = new CallInfo();
                info.setPalletName(pallet.getName());
                info.setCallName(variant.getName());

                for (MetadataTypeField field : variant.getFields()) {
                    MetadataType argType = types.stream()
                            .filter(t -> t.getId() == field.getType())
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Arg type not found: " + field.getType()));

                    String typeName = argType.getPath() != null && !argType.getPath().isEmpty()
                            ? String.join(".", argType.getPath())
                            : "type#" + argType.getId();

                    info.getArgs().add(typeName);
                    info.getArgTypes().add(argType.getId());
                }

                return info;
            }
        }
        throw new IllegalArgumentException("Pallet index not found: " + palletIndex);
    }

    public List<MetadataTypeField> getEventFields(int palletIndex, int eventIndex) {
        for (MetadataPallet pallet : this.pallets) {
            if (pallet.getIndex() == palletIndex && pallet.getEvent() != null) {

                MetadataType eventType = types.stream()
                        .filter(t -> t.getId() == pallet.getEvent().getType())
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Event type not found: " + pallet.getEvent().getType()));

                if(eventType.getDef() == null) {
                    continue;
                }

                if(!(eventType.getDef() instanceof MetadataTypeVariants variantsCall)) {
                    throw new IllegalStateException("Event type " + eventType + " is not a variant");
                }

                List<MetadataTypeVariants.Variant> variants = variantsCall.getVariants();
                for(MetadataTypeVariants.Variant variant : variants) {
                    if(variant.getIndex() == eventIndex) {
                        return variant.getFields();
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    public static class CallInfo {
        private String palletName;
        private String callName;
        private List<String> args; // list of type names
        private List<Integer> argTypes;

        public CallInfo() {
            args = new ArrayList<>();
            argTypes = new ArrayList<>();
        }

        public String getPalletName() {
            return palletName;
        }

        public void setPalletName(String palletName) {
            this.palletName = palletName;
        }

        public String getCallName() {
            return callName;
        }

        public void setCallName(String callName) {
            this.callName = callName;
        }

        public List<String> getArgs() {
            return args;
        }

        public void setArgs(List<String> args) {
            this.args = args;
        }

        public List<Integer> getArgTypes() {
            return argTypes;
        }

        public void setArgTypes(List<Integer> argTypes) {
            this.argTypes = argTypes;
        }

        @Override
        public String toString() {
            return "CallInfo{" +
                    "palletName='" + palletName + '\'' +
                    ", callName='" + callName + '\'' +
                    ", args=" + args +
                    ", argTypes=" + argTypes +
                    '}';
        }
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<MetadataType> getTypes() {
        return types;
    }

    public void setTypes(List<MetadataType> types) {
        this.types = types;
    }

    public List<MetadataPallet> getPallets() {
        return pallets;
    }

    public void setPallets(List<MetadataPallet> pallets) {
        this.pallets = pallets;
    }

    public List<MetadataExtrinsic> getExtrinsics() {
        return extrinsics;
    }

    public void setExtrinsics(List<MetadataExtrinsic> extrinsics) {
        this.extrinsics = extrinsics;
    }

    public int getRuntimeType() {
        return runtimeType;
    }

    public void setRuntimeType(int runtimeType) {
        this.runtimeType = runtimeType;
    }

    public int getExtrinsicVersion() {
        return extrinsicVersion;
    }

    public void setExtrinsicVersion(int extrinsicVersion) {
        this.extrinsicVersion = extrinsicVersion;
    }

    public int getExtrinsicType() {
        return extrinsicType;
    }

    public void setExtrinsicType(int extrinsicType) {
        this.extrinsicType = extrinsicType;
    }

    @Override
    public String toString() {
        return "MetadataV14{" +
                "magic=" + magic +
                ", version=" + version +
                ", types=" + types +
                ", pallets=" + pallets +
                ", extrinsicType=" + extrinsicType +
                ", extrinsicVersion=" + extrinsicVersion +
                ", extrinsics=" + extrinsics +
                ", runtimeType=" + runtimeType +
                '}';
    }
}