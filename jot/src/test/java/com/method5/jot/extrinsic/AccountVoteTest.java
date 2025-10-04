package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.extrinsic.model.AccountVoteType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountVoteTest extends TestBase {
    @Test
    public void testGetAccountVoteByType() {
        assertEquals(AccountVoteType.STANDARD, AccountVoteType.fromValue(0));
        assertEquals(AccountVoteType.SPLIT, AccountVoteType.fromValue(1));
        assertEquals(AccountVoteType.SPLIT_ABSTAIN, AccountVoteType.fromValue(2));

        assertNull(AccountVoteType.fromValue(99));
    }
}
