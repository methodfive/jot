package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.extrinsic.call.SystemPallet;
import com.method5.jot.extrinsic.call.Transaction;
import com.method5.jot.query.AuthorRpc;
import com.method5.jot.query.ChainRpc;
import com.method5.jot.query.Query;
import com.method5.jot.query.SystemRpc;
import com.method5.jot.query.model.SignedBlock;
import com.method5.jot.rpc.OfflineApi;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.wallet.Wallet;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CallTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(CallTest.class);

    @Test
    public void testSign() throws Exception {
        PolkadotWs api = mock(PolkadotWs.class);
        Transaction tx = mock(Transaction.class);
        SystemPallet systemPallet = mock(SystemPallet.class);
        SystemRpc systemRpc = mock(SystemRpc.class);
        Query query = mock(Query.class);
        ChainRpc chainRpc = mock(ChainRpc.class);

        when(tx.system()).thenReturn(systemPallet);
        when(systemRpc.accountNextIndex(anyString())).thenReturn(BigInteger.TEN);
        when(chainRpc.finalizedHead()).thenReturn(new byte[32]);
        when(chainRpc.block(anyString())).thenReturn(new SignedBlock());
        when(chainRpc.blockHash(anyLong())).thenReturn("00");
        when(query.chain()).thenReturn(chainRpc);
        when(query.system()).thenReturn(systemRpc);
        when(systemPallet.remark("test".getBytes(StandardCharsets.UTF_8))).thenReturn(new Call(api, new byte[0]));
        when(api.tx()).thenReturn(tx);
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(query);

        Wallet wallet = Wallet.generate();
        String signedExtrinsic = api.tx().system().remark("test".getBytes(StandardCharsets.UTF_8)).sign(wallet.getSigner());

        assertNotNull(signedExtrinsic);

        logger.info(signedExtrinsic);
    }

    @Test
    public void testSignAndSend() throws Exception {
        PolkadotWs api = mock(PolkadotWs.class);
        Transaction tx = mock(Transaction.class);
        SystemPallet systemPallet = mock(SystemPallet.class);
        SystemRpc systemRpc = mock(SystemRpc.class);
        AuthorRpc authorRpc = mock(AuthorRpc.class);
        Query query = mock(Query.class);
        ChainRpc chainRpc = mock(ChainRpc.class);

        when(tx.system()).thenReturn(systemPallet);
        when(authorRpc.submitExtrinsic(any())).thenReturn("00112233");
        when(systemRpc.accountNextIndex(anyString())).thenReturn(BigInteger.TEN);
        when(chainRpc.finalizedHead()).thenReturn(new byte[32]);
        when(chainRpc.block(anyString())).thenReturn(new SignedBlock());
        when(chainRpc.blockHash(anyLong())).thenReturn("00");
        when(query.chain()).thenReturn(chainRpc);
        when(query.author()).thenReturn(authorRpc);
        when(query.system()).thenReturn(systemRpc);
        when(systemPallet.remark("test".getBytes(StandardCharsets.UTF_8))).thenReturn(new Call(api, new byte[0]));
        when(api.tx()).thenReturn(tx);
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(query);

        Wallet wallet = Wallet.generate();
        String signedExtrinsic = api.tx().system().remark("test".getBytes(StandardCharsets.UTF_8)).signAndSend(wallet.getSigner());

        assertNotNull(signedExtrinsic);
        assertEquals("00112233", signedExtrinsic);
    }

    @Test
    public void testSignAndWaitForResults() throws Exception {
        PolkadotWs api = mock(PolkadotWs.class);
        Transaction tx = mock(Transaction.class);
        SystemPallet systemPallet = mock(SystemPallet.class);
        SystemRpc systemRpc = mock(SystemRpc.class);
        AuthorRpc authorRpc = mock(AuthorRpc.class);
        Query query = mock(Query.class);
        ChainRpc chainRpc = mock(ChainRpc.class);

        when(tx.system()).thenReturn(systemPallet);
        when(authorRpc.submitExtrinsic(any())).thenReturn("00112233");
        when(systemRpc.accountNextIndex(anyString())).thenReturn(BigInteger.TEN);
        when(chainRpc.finalizedHead()).thenReturn(new byte[32]);
        when(chainRpc.block(anyString())).thenReturn(new SignedBlock());
        when(chainRpc.blockHash(anyLong())).thenReturn("00");
        when(query.chain()).thenReturn(chainRpc);
        when(query.author()).thenReturn(authorRpc);
        when(query.system()).thenReturn(systemRpc);
        when(systemPallet.remark("test".getBytes(StandardCharsets.UTF_8))).thenReturn(new Call(api, new byte[0]));
        when(api.tx()).thenReturn(tx);
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(query);
        when(api.submitAndWaitForExtrinsic(any(), any(), anyLong())).thenReturn(new ExtrinsicResult("00", true, null, null));

        Wallet wallet = Wallet.generate();
        ExtrinsicResult result = api.tx().system().remark("test".getBytes(StandardCharsets.UTF_8)).signAndWaitForResults(wallet.getSigner());

        assertNotNull(result);
    }

    @Test
    public void testWaitForResultsBadType() throws Exception {
        OfflineApi api = mock(OfflineApi.class);
        Transaction tx = mock(Transaction.class);
        SystemPallet systemPallet = mock(SystemPallet.class);
        SystemRpc systemRpc = mock(SystemRpc.class);
        AuthorRpc authorRpc = mock(AuthorRpc.class);
        Query query = mock(Query.class);
        ChainRpc chainRpc = mock(ChainRpc.class);

        when(tx.system()).thenReturn(systemPallet);
        when(authorRpc.submitExtrinsic(any())).thenReturn("00112233");
        when(systemRpc.accountNextIndex(anyString())).thenReturn(BigInteger.TEN);
        when(chainRpc.finalizedHead()).thenReturn(new byte[32]);
        when(chainRpc.block(anyString())).thenReturn(new SignedBlock());
        when(chainRpc.blockHash(anyLong())).thenReturn("00");
        when(query.chain()).thenReturn(chainRpc);
        when(query.author()).thenReturn(authorRpc);
        when(query.system()).thenReturn(systemRpc);
        when(systemPallet.remark("test".getBytes(StandardCharsets.UTF_8))).thenReturn(new Call(api, new byte[0]));
        when(api.tx()).thenReturn(tx);
        when(api.getChainSpec()).thenReturn(chainSpec);
        when(api.query()).thenReturn(query);

        Wallet wallet = Wallet.generate();
        assertThrows(UnsupportedOperationException.class, () -> api.tx().system().remark("test".getBytes(StandardCharsets.UTF_8)).signAndWaitForResults(wallet.getSigner(), PolkadotWs.Confirmation.BEST, 10000));
    }
}
