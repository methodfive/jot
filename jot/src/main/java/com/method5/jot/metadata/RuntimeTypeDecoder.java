package com.method5.jot.metadata;

import com.method5.jot.entity.metadata.MetadataType;
import com.method5.jot.entity.metadata.MetadataTypeField;
import com.method5.jot.entity.metadata.MetadataTypeVariants;
import com.method5.jot.entity.variant.*;
import com.method5.jot.scale.ScaleReader;
import com.method5.jot.entity.PrimitiveType;

import java.util.*;

public class RuntimeTypeDecoder {
    private final List<MetadataType> types;

    public RuntimeTypeDecoder(List<MetadataType> types) {
        this.types = types;
    }

    public Object decodeValue(int typeId, ScaleReader reader) {
        MetadataType type = resolveType(typeId);

        return switch (type.getDef()) {
            case Primitive ignored -> decodePrimitive(type, reader);
            case Compact ignored -> reader.readCompact();
            case Array ignored -> decodeArray(type, reader);
            case Tuple ignored -> decodeTuple(type, reader);
            case Sequence ignored -> decodeSequence(type, reader);
            case Bits ignored -> throw new UnsupportedOperationException("Bit sequence not supported yet");
            case MetadataTypeVariants ignored -> decodeEnumVariant(type, reader);
            case Composite ignored -> decodeComposite(type, reader);
            case null, default -> throw new UnsupportedOperationException("Unknown type kind: " + typeId);
        };
    }

    private MetadataType resolveType(int id) {
        return types.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown type ID: " + id));
    }

    private List<Object> decodeTuple(MetadataType type, ScaleReader reader) {
        List<Integer> types = ((Tuple)type.getDef()).getTypes();
        List<Object> values = new ArrayList<>();
        for (Integer typeID : types) {
            values.add(decodeValue(typeID, reader));
        }
        return values;
    }

    private Object decodePrimitive(MetadataType type, ScaleReader reader) {
        PrimitiveType primitiveType = ((Primitive)type.getDef()).getType();

        if(primitiveType == PrimitiveType.BOOLEAN) {
            return reader.readBoolean();
        } if(primitiveType == PrimitiveType.CHAR) {
            return reader.readChar();
        } if(primitiveType == PrimitiveType.STRING) {
            return reader.readString();
        } if(primitiveType == PrimitiveType.U8) {
            return reader.readByte();
        } if(primitiveType == PrimitiveType.U16) {
            return reader.readU16();
        } if(primitiveType == PrimitiveType.U32) {
            return reader.readU32();
        } if(primitiveType == PrimitiveType.U64) {
            return reader.readU64();
        } if(primitiveType == PrimitiveType.U128) {
            return reader.readU128();
        } if(primitiveType == PrimitiveType.U256) {
            return reader.readU256();
        } if(primitiveType == PrimitiveType.I8) {
            return reader.readI8();
        } if(primitiveType == PrimitiveType.I16) {
            return reader.readI16();
        } if(primitiveType == PrimitiveType.I32) {
            return reader.readI32();
        } if(primitiveType == PrimitiveType.I64) {
            return reader.readI64();
        } if(primitiveType == PrimitiveType.I128) {
            return reader.readI128();
        } if(primitiveType == PrimitiveType.I256) {
            return reader.readI256();
        } else {
            throw new UnsupportedOperationException("Unsupported primitive");
        }
    }

    private List<Object> decodeSequence(MetadataType type, ScaleReader reader) {
        int length = reader.readCompact().intValue();
        int elementTypeId = ((Sequence)type.getDef()).getType();
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            result.add(decodeValue(elementTypeId, reader));
        }
        return result;
    }

    private List<Object> decodeArray(MetadataType type, ScaleReader reader) {
        int len = ((Array)type.getDef()).getLength();
        int elementTypeId = ((Array)type.getDef()).getType();
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            result.add(decodeValue(elementTypeId, reader));
        }
        return result;
    }

    private Map<String, Object> decodeComposite(MetadataType type, ScaleReader reader) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (MetadataTypeField field : ((Composite)type.getDef()).getFields()) {
            String name = field.getName() != null ? field.getName() : "field" + field.getType();
            Object value = decodeValue(field.getType(), reader);
            map.put(name, value);
        }
        return map;
    }

    private Map<String, Object> decodeEnumVariant(MetadataType type, ScaleReader reader) {
        int index = reader.readByte();
        MetadataTypeVariants.Variant variant = ((MetadataTypeVariants)type.getDef()).getVariants().get(index);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("_variant", variant.getName());

        for (int i = 0; i < variant.getFields().size(); i++) {
            MetadataTypeField field = variant.getFields().get(i);
            String fieldName = field.getName() != null ? field.getName() : "field" + i;
            Object value = decodeValue(field.getType(), reader);
            result.put(fieldName, value);
        }

        return result;
    }

    public static class TypeAndValue {
        private int type;
        private Object value;
        public TypeAndValue(int type, Object value) {
            this.type = type;
            this.value = value;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "TypeAndValue{" +
                    "type=" + type +
                    ", value=" + value +
                    '}';
        }
    }
}
