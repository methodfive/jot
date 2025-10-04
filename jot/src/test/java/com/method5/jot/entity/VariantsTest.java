package com.method5.jot.entity;

import com.method5.jot.entity.metadata.MetadataTypeField;
import com.method5.jot.entity.variant.*;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VariantsTest {
    private static final Logger logger = LoggerFactory.getLogger(VariantsTest.class);

    @Test
    void testArrays() {
        Array array = new Array(123, 5);
        assertEquals(123, array.getLength());
        assertEquals(5, array.getType());

        array.setLength(321);
        assertEquals(321, array.getLength());

        array.setType(20);
        assertEquals(20, array.getType());

        assertEquals("034101000050", HexUtil.bytesToHex(array.encode()));

        logger.info(array.toString());
    }

    @Test
    void testBits() {
        Bits bits = new Bits();
        assertEquals(0, bits.getOrder());
        assertEquals(0, bits.getType());

        bits.setOrder(321);
        assertEquals(321, bits.getOrder());

        bits.setType(20);
        assertEquals(20, bits.getType());

        assertEquals("07500505", HexUtil.bytesToHex(bits.encode()));

        logger.info(bits.toString());
    }

    @Test
    void testTuple() {
        Tuple tuple = new Tuple(new ArrayList<>());
        assertEquals(0, tuple.getTypes().size());

        tuple.setTypes(new ArrayList<>(List.of(1,2,3)));
        assertEquals(3, tuple.getTypes().size());

        assertEquals("040c04080c", HexUtil.bytesToHex(tuple.encode()));

        logger.info(tuple.toString());
    }

    @Test
    void testPrimitive() {
        Primitive primitive = new Primitive(PrimitiveType.BOOLEAN);
        assertEquals(PrimitiveType.BOOLEAN, primitive.getType());

        primitive.setType(PrimitiveType.I8);
        assertEquals(PrimitiveType.I8, primitive.getType());

        assertEquals("0509", HexUtil.bytesToHex(primitive.encode()));

        logger.info(primitive.toString());
    }

    @Test
    void testSequence() {
        Sequence sequence = new Sequence(5);
        assertEquals(5, sequence.getType());

        sequence.setType(20);
        assertEquals(20, sequence.getType());

        assertEquals("0250", HexUtil.bytesToHex(sequence.encode()));

        logger.info(sequence.toString());
    }

    @Test
    void testCompact() {
        Compact compact = new Compact(5);
        assertEquals(5, compact.getType());

        compact.setType(20);
        assertEquals(20, compact.getType());

        assertEquals("0650", HexUtil.bytesToHex(compact.encode()));

        logger.info(compact.toString());
    }

    @Test
    void testComposite() {
        Composite composite = new Composite(new ArrayList<>());
        assertEquals(0, composite.getFields().size());

        composite.setFields(new ArrayList<>(List.of(new MetadataTypeField())));
        assertEquals(1, composite.getFields().size());

        assertEquals("000400000000", HexUtil.bytesToHex(composite.encode()));

        logger.info(composite.toString());
    }
}
