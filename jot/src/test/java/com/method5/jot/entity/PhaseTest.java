package com.method5.jot.entity;

import com.method5.jot.scale.ScaleReader;
import com.method5.jot.scale.ScaleWriter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhaseTest {
    @Test
    void testPhaseCase0() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte(0);
        writer.writeInt(10);

        Phase phase = Phase.decode(new ScaleReader(writer.toByteArray()));

        assertEquals(10, phase.extrinsicIndex());
        assertTrue(phase.isApplyExtrinsic(10));
        assertFalse(phase.isApplyExtrinsic(5));
    }

    @Test
    void testPhaseCase1() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte(1);

        Phase phase = Phase.decode(new ScaleReader(writer.toByteArray()));

        assertEquals(Phase.Type.FINALIZATION, phase.type());
    }

    @Test
    void testPhaseCase2() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte(2);

        Phase phase = Phase.decode(new ScaleReader(writer.toByteArray()));

        assertEquals(Phase.Type.INITIALIZATION, phase.type());
    }

    @Test
    void testPhaseUnknown() {
        ScaleWriter writer = new ScaleWriter();
        writer.writeByte(99);

        Phase phase = Phase.decode(new ScaleReader(writer.toByteArray()));

        assertEquals(Phase.Type.UNKNOWN, phase.type());
    }
}
