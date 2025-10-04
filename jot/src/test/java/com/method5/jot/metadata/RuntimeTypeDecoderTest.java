package com.method5.jot.metadata;

import com.method5.jot.entity.PrimitiveType;
import com.method5.jot.entity.metadata.MetadataType;
import com.method5.jot.entity.metadata.MetadataTypeField;
import com.method5.jot.entity.metadata.MetadataTypeVariants;
import com.method5.jot.entity.variant.*;
import com.method5.jot.scale.ScaleReader;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class RuntimeTypeDecoderTest {
    private static final Logger logger = LoggerFactory.getLogger(RuntimeTypeDecoderTest.class);

    private static MetadataType primitive(int id, PrimitiveType type) {
        return new MetadataType(id, new Primitive(type));
    }

    private static MetadataType tuple(int id, List<Integer> typeIds) {
        return new MetadataType(id, new Tuple(typeIds));
    }

    private static MetadataType array(int id, int len, int elementType) {
        return new MetadataType(id, new Array(len, elementType));
    }

    private static MetadataType sequence(int id, int elementType) {
        return new MetadataType(id, new Sequence(elementType));
    }

    private static MetadataType compact(int id) {
        return new MetadataType(id, new Compact(0)); // type inside is unused
    }

    @Test
    void testDecodePrimitiveU8() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readByte()).thenReturn((byte) 42);

        MetadataType type = primitive(1, PrimitiveType.U8);
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(type));

        Object result = decoder.decodeValue(1, reader);
        assertEquals((byte) 42, result);
    }

    @Test
    void testDecodePrimitiveU16() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readU16()).thenReturn(42);

        MetadataType type = primitive(1, PrimitiveType.U16);
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(type));

        Object result = decoder.decodeValue(1, reader);
        assertEquals(42, result);
    }

    @Test
    void testDecodePrimitiveU64() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readU64()).thenReturn((long)42);

        MetadataType type = primitive(1, PrimitiveType.U64);
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(type));

        Object result = decoder.decodeValue(1, reader);
        assertEquals((long)42, result);
    }

    @Test
    void testDecodePrimitiveU128() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readU128()).thenReturn(BigInteger.valueOf(42));

        MetadataType type = primitive(1, PrimitiveType.U128);
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(type));

        Object result = decoder.decodeValue(1, reader);
        assertEquals(BigInteger.valueOf(42), result);
    }

    @Test
    void testDecodePrimitiveU256() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readU256()).thenReturn(BigInteger.valueOf(42));

        MetadataType type = primitive(1, PrimitiveType.U256);
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(type));

        Object result = decoder.decodeValue(1, reader);
        assertEquals(BigInteger.valueOf(42), result);
    }

    @Test
    void testDecodePrimitiveI64() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readI64()).thenReturn((long)42);

        MetadataType type = primitive(1, PrimitiveType.I64);
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(type));

        Object result = decoder.decodeValue(1, reader);
        assertEquals((long)42, result);
    }

    @Test
    void testDecodePrimitiveI128() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readI128()).thenReturn(BigInteger.valueOf(42));

        MetadataType type = primitive(1, PrimitiveType.I128);
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(type));

        Object result = decoder.decodeValue(1, reader);
        assertEquals(BigInteger.valueOf(42), result);
    }

    @Test
    void testDecodePrimitiveI256() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readI256()).thenReturn(BigInteger.valueOf(42));

        MetadataType type = primitive(1, PrimitiveType.I256);
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(type));

        Object result = decoder.decodeValue(1, reader);
        assertEquals(BigInteger.valueOf(42), result);
    }

    @Test
    void testDecodeChar() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readChar()).thenReturn('B');

        MetadataType type = primitive(1, PrimitiveType.CHAR);
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(type));

        Object result = decoder.decodeValue(1, reader);
        assertEquals('B', result);
    }

    @Test
    void testDecodeTuple() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readU32()).thenReturn(100L);
        when(reader.readBoolean()).thenReturn(true);

        MetadataType u32 = primitive(1, PrimitiveType.U32);
        MetadataType bool = primitive(2, PrimitiveType.BOOLEAN);
        MetadataType tuple = tuple(3, List.of(1, 2));

        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(u32, bool, tuple));
        List<Object> values = (List<Object>) decoder.decodeValue(3, reader);

        assertEquals(List.of(100L, true), values);
    }

    @Test
    void testDecodeArray() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readI8()).thenReturn((byte) 1, (byte) 2, (byte) 3);

        MetadataType i8 = primitive(1, PrimitiveType.I8);
        MetadataType arr = array(2, 3, 1);

        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(i8, arr));
        List<Object> result = (List<Object>) decoder.decodeValue(2, reader);

        assertEquals(List.of((byte) 1, (byte) 2, (byte) 3), result);
    }

    @Test
    void testDecodeSequence() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readCompact()).thenReturn(BigInteger.valueOf(2));
        when(reader.readI16()).thenReturn((short) 10, (short) 20);

        MetadataType i16 = primitive(1, PrimitiveType.I16);
        MetadataType seq = sequence(2, 1);

        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(i16, seq));
        List<Object> result = (List<Object>) decoder.decodeValue(2, reader);

        assertEquals(List.of((short) 10, (short) 20), result);
    }

    @Test
    void testDecodeCompactInt() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readCompact()).thenReturn(BigInteger.TEN);

        MetadataType compact = compact(1);
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(compact));

        Object result = decoder.decodeValue(1, reader);
        assertEquals(BigInteger.TEN, result);
    }

    @Test
    void testDecodeComposite() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readString()).thenReturn("alice");

        MetadataType nameType = primitive(1, PrimitiveType.STRING);
        MetadataTypeField nameField = new MetadataTypeField("name", 1);
        MetadataType composite = new MetadataType(2, new Composite(List.of(nameField)));

        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(nameType, composite));
        Map<String, Object> result = (Map<String, Object>) decoder.decodeValue(2, reader);

        assertEquals("alice", result.get("name"));
    }

    @Test
    void testDecodeEnumVariant() {
        ScaleReader reader = mock(ScaleReader.class);
        when(reader.readByte()).thenReturn((byte) 0);
        when(reader.readI32()).thenReturn(123);

        MetadataType i32 = new MetadataType(1, new Primitive(PrimitiveType.I32));
        MetadataTypeField field = new MetadataTypeField("val", 1);
        MetadataTypeVariants.Variant variant = new MetadataTypeVariants.Variant("MyVariant", 0, List.of(field));
        MetadataType enumType = new MetadataType(2, new MetadataTypeVariants(List.of(variant)));

        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(i32, enumType));
        Map<String, Object> result = (Map<String, Object>) decoder.decodeValue(2, reader);

        assertEquals("MyVariant", result.get("_variant"));
        assertEquals(123, result.get("val"));
    }

    @Test
    void testDecodeUnknownTypeThrows() {
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of());
        assertThrows(IllegalArgumentException.class, () ->
                decoder.decodeValue(99, mock(ScaleReader.class))
        );
    }

    @Test
    void testUnsupportedBitTypeThrows() {
        MetadataType bitType = new MetadataType(1, new Bits());
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(bitType));
        assertThrows(UnsupportedOperationException.class, () ->
                decoder.decodeValue(1, mock(ScaleReader.class))
        );
    }

    @Test
    void testUnsupportedPrimitiveThrows() {
        MetadataType broken = new MetadataType(1, new Primitive(null));
        RuntimeTypeDecoder decoder = new RuntimeTypeDecoder(List.of(broken));
        assertThrows(UnsupportedOperationException.class, () ->
                decoder.decodeValue(1, mock(ScaleReader.class))
        );
    }

    @Test
    void testTypeAndValueCreation() {
        RuntimeTypeDecoder.TypeAndValue typeAndValue = new RuntimeTypeDecoder.TypeAndValue(0, null);
        assertEquals(0, typeAndValue.getType());
        assertNull(typeAndValue.getValue());

        typeAndValue.setType(1);
        typeAndValue.setValue("test");

        assertEquals(1, typeAndValue.getType());
        assertEquals("test", typeAndValue.getValue());

        logger.info(typeAndValue.toString());
    }
}
