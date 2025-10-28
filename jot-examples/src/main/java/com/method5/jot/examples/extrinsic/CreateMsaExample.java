package com.method5.jot.examples.extrinsic;

import com.method5.jot.events.EventRecord;
import com.method5.jot.examples.Config;
import com.method5.jot.extrinsic.ExtrinsicResult;
import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class CreateMsaExample {
  private static final Logger logger = LoggerFactory.getLogger(CreateMsaExample.class);

  public static void main(String[] args) throws Exception {
    Wallet alice = Wallet.fromSr25519Seed(HexUtil.hexToBytes("0xe5be9a5092b81bca64be81d212e7f2f9eba183bb7a90954f7b76361f6edb5c0a"));

    try (PolkadotWs api = new PolkadotWs(Config.FREQUENCY_WSS_SERVER, 10000)) {
      execute(api, alice.getSigner());
    }
  }

  public static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
    logger.info("Create MSA Example");
    logger.info("------------------------");

    Call call = api.tx().msa().createMsa();

    ExtrinsicResult result = call.signAndWaitForResults(signingProvider);

    List<EventRecord> eventRecordList = result.getEvents();
    for (EventRecord eventRecord : eventRecordList) {
      logger.info("Event: " + eventRecord.method());
    }

    String hash = result.getHash();

    logger.info("Extrinsic hash: {}", hash);

    //Now try and get an error by sending the createMsa transaction again

    ExtrinsicResult failure = call.signAndWaitForResults(signingProvider);

    logger.info("Extrinsic dispatch error: " + failure.getError());
  }
}
