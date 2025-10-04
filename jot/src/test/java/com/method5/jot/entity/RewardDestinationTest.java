package com.method5.jot.entity;

import com.method5.jot.scale.ScaleReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RewardDestinationTest {
    @Test
    void testFromTag() {
        assertEquals(RewardDestination.Type.STAKED, RewardDestination.Type.fromTag(0));
    }

    @Test
    void testUnknownTag() {
        assertThrows(Exception.class, () -> RewardDestination.Type.fromTag(99));
    }

    @Test
    void testEncodeDecodeStash() {
        RewardDestination rewardDestination = RewardDestination.stash();
        RewardDestination decoded = RewardDestination.decode(new ScaleReader(rewardDestination.encode()));

        assertEquals(rewardDestination.getType(), decoded.getType());
    }

    @Test
    void testEncodeDecodeController() {
        RewardDestination rewardDestination = RewardDestination.controller();
        RewardDestination decoded = RewardDestination.decode(new ScaleReader(rewardDestination.encode()));

        assertEquals(rewardDestination.getType(), decoded.getType());
    }

    @Test
    void testEncodeDecodeAccount() {
        RewardDestination rewardDestination = RewardDestination.account(new byte[32]);
        RewardDestination decoded = RewardDestination.decode(new ScaleReader(rewardDestination.encode()));

        assertEquals(rewardDestination.getType(), decoded.getType());
    }
}
