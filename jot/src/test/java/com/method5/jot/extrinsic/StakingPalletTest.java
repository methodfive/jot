package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.entity.MultiAddress;
import com.method5.jot.entity.RewardDestination;
import com.method5.jot.extrinsic.call.StakingPallet;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StakingPalletTest extends TestBase {
    @Test
    public void testBond() {
        byte[] callData = StakingPallet.bond(resolver,
                BigDecimal.TWO,
                RewardDestination.staked());
        assertNotNull(callData);
        assertEquals("07000700c817a80400", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBondExtra() {
        byte[] callData = StakingPallet.bondExtra(resolver,
                new BigDecimal(1));
        assertNotNull(callData);
        assertEquals("07010700e40b5402", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testChill() {
        byte[] callData = StakingPallet.chill(resolver);
        assertNotNull(callData);
        assertEquals("0706", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testRebond() {
        byte[] callData = StakingPallet.rebond(resolver,
                new BigDecimal(1));
        assertNotNull(callData);
        assertEquals("07130700e40b5402", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testUnbond() {
        byte[]  callData = StakingPallet.unbond(resolver,
                new BigDecimal(1));
        assertNotNull(callData);
        assertEquals("07020700e40b5402", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testNominate() {
        byte[] callData = StakingPallet.nominate(resolver,
                new ArrayList<>(List.of(
                        MultiAddress.fromSS58("114SUbKCXjmb9czpWTtS3JANSmNRwVa4mmsMrWYpRG1kDH5"),
                        MultiAddress.fromSS58("11uMPbeaEDJhUxzU4ZfWW9VQEsryP9XqFcNRfPdYda6aFWJ"),
                        MultiAddress.fromSS58("1737bipUqNUHYjUB5HCezyYqto5ZjFiMSXNAX8fWktnD5AS")
                )));
        assertNotNull(callData);
        assertEquals("07050c00000b93d72dcc12bd5577438c92a19c4778e12cfb8ada871a17694e5a2f86c3740000b03b23766d70d0445943b290606521acaefee7660d521950faf2801c79d42800049a9687c22bf19c419cfcc79a77b60b07faa3df2034d7cfa4635350571cbd32", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testStakingPallet() {
        byte[] callData = StakingPallet.payoutStakers(resolver,
                AccountId.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"),
                1872  );
        assertNotNull(callData);
        assertEquals("07126d6f646c70792f7472737279000000000000000000000000000000000000000050070000", HexUtil.bytesToHex(callData));
    }
}
