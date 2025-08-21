package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.entity.MultiAddress;
import com.method5.jot.extrinsic.call.ConvictionVotingPallet;
import com.method5.jot.extrinsic.model.AccountVote;
import com.method5.jot.extrinsic.model.Conviction;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConvictionVotingPalletTest extends TestBase {
    @Test
    public void testConvictionVotingPallet() {
        byte[] callData = ConvictionVotingPallet.delegate(resolver,
                1,
                MultiAddress.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"),
                Conviction.LOCKED_4X, BigDecimal.ONE);
        assertNotNull(callData);
        assertEquals("14010100006d6f646c70792f747273727900000000000000000000000000000000000000000400e40b54020000000000000000000000", HexUtil.bytesToHex(callData));

        callData = ConvictionVotingPallet.removeVote(resolver,
                5,
                12353);
        assertNotNull(callData);
        assertEquals("140401050041300000", HexUtil.bytesToHex(callData));

        callData = ConvictionVotingPallet.vote(resolver,
                4342,
                AccountVote.standard(true, Conviction.NONE, BigDecimal.valueOf(0.24)));
        assertNotNull(callData);
        assertEquals("1400d943008000180d8f000000000000000000000000", HexUtil.bytesToHex(callData));

        callData = ConvictionVotingPallet.unlock(resolver,
                3424,
                MultiAddress.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"));
        assertNotNull(callData);
        assertEquals("1403600d006d6f646c70792f74727372790000000000000000000000000000000000000000", HexUtil.bytesToHex(callData));

        callData = ConvictionVotingPallet.undelegate(resolver,
                2);
        assertNotNull(callData);
        assertEquals("14020200", HexUtil.bytesToHex(callData));


        callData = ConvictionVotingPallet.removeOtherVote(resolver,
                MultiAddress.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"),
                2,
                1231);
        assertNotNull(callData);
        assertEquals("1405006d6f646c70792f747273727900000000000000000000000000000000000000000200cf040000", HexUtil.bytesToHex(callData));
    }
}
