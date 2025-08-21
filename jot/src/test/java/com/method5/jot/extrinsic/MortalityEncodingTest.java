package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.scale.ScaleReader;
import com.method5.jot.entity.Mortality;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MortalityEncodingTest extends TestBase {
    @Test
    public void testEncodeDecode() {
        Mortality immortal = Mortality.immortal();
        assertTrue(Mortality.decode(new ScaleReader(immortal.encode())).isImmortal());

        testBlockNumber(123456, 0);
        testBlockNumber(1234560, 0);
        testBlockNumber(1234565, 5);
        testBlockNumber(10, 10);
        testBlockNumber(3, 3);
    }

    private void testBlockNumber(int blockNumber, int expectedPhase) {
        Mortality mortal = Mortality.mortal(blockNumber);
        Mortality decoded = Mortality.decode(new ScaleReader(mortal.encode()));

        assertEquals(64, decoded.getPeriod());
        assertEquals(expectedPhase, decoded.getPhase());
    }
}
