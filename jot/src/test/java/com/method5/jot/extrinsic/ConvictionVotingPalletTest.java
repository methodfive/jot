package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.entity.MultiAddress;
import com.method5.jot.extrinsic.model.AccountVote;
import com.method5.jot.extrinsic.model.Conviction;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConvictionVotingPalletTest extends TestBase {
    @Test
    public void testBuildDelegate() {
        byte[] callData = api.tx().convictionVoting().delegate(
                1,
                MultiAddress.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"),
                Conviction.LOCKED_4X, BigDecimal.ONE).callData();
        assertNotNull(callData);
        assertEquals("14010100006d6f646c70792f747273727900000000000000000000000000000000000000000400e40b54020000000000000000000000", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBuildRemoveVote() {
        byte[] callData = api.tx().convictionVoting().removeVote(
                5,
                12353).callData();
        assertNotNull(callData);
        assertEquals("140401050041300000", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBuildRemoveVoteWithoutClass() {
        byte[] callData = api.tx().convictionVoting().removeVote(
                null,
                12353).callData();
        assertNotNull(callData);
        assertEquals("14040041300000", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBuildStandardVote() {
        byte[] callData = api.tx().convictionVoting().vote(
                4342,
                AccountVote.standard(true, Conviction.NONE, BigDecimal.valueOf(0.24))).callData();
        assertNotNull(callData);
        assertEquals("1400d943008000180d8f000000000000000000000000", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBuildSplitVote() {
        byte[] callData = api.tx().convictionVoting().vote(
                4342,
                AccountVote.split(BigDecimal.valueOf(10), BigDecimal.valueOf(5))).callData();
        assertNotNull(callData);
        assertEquals("1400d9430100e8764817000000000000000000000000743ba40b0000000000000000000000", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBuildSplitAbstainVote() {
        byte[] callData = api.tx().convictionVoting().vote(
                4342,
                AccountVote.splitAbstain(BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(3))).callData();
        assertNotNull(callData);
        assertEquals("1400d9430200e8764817000000000000000000000000743ba40b000000000000000000000000ac23fc060000000000000000000000", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBuildUnlock() {
        byte[] callData = api.tx().convictionVoting().unlock(
                3424,
                MultiAddress.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB")).callData();
        assertNotNull(callData);
        assertEquals("1403600d006d6f646c70792f74727372790000000000000000000000000000000000000000", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBuildUndelegate() {
        byte[] callData = api.tx().convictionVoting().undelegate(
                2).callData();
        assertNotNull(callData);
        assertEquals("14020200", HexUtil.bytesToHex(callData));
    }

    @Test
    public void testBuildRemoveOther() {
        byte[] callData = api.tx().convictionVoting().removeOtherVote(
                MultiAddress.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB"),
                2,
                1231).callData();
        assertNotNull(callData);
        assertEquals("1405006d6f646c70792f747273727900000000000000000000000000000000000000000200cf040000", HexUtil.bytesToHex(callData));
    }
}
