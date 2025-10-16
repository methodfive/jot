package com.method5.jot.examples.extrinsic;

import com.method5.jot.events.EventRecord;
import com.method5.jot.examples.Config;
import com.method5.jot.extrinsic.ExtrinsicResult;
import com.method5.jot.extrinsic.call.Call;
import com.method5.jot.query.model.AccountId;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class BalancesTransferAllowDeathSignAndWaitForResultsExample {
  private static final Logger logger = LoggerFactory.getLogger(BalancesTransferAllowDeathSignAndWaitForResultsExample.class);

  public static void main(String[] args) throws Exception {
    Wallet wallet = Wallet.fromMnemonic(Config.MNEMONIC_PHRASE);

    try (PolkadotWs api = new PolkadotWs(Config.FREQUENCY_WSS_SERVER, 10000)) {
      execute(api, wallet.getSigner());
    }
  }

  public static void execute(PolkadotWs api, SigningProvider signingProvider) throws Exception {
    logger.info("Balances Transfer Allow Death (Using signAndWaitForResults) Example");
    logger.info("------------------------");

    // Destination address
    AccountId destination = AccountId.fromSS58("13NHcoGFJsHJoCYVsJrrv2ygLtz2XJSR17KrnA9QTNYz3Zkz");
    // Amount
    BigDecimal amount = new BigDecimal("0.001");

    Call call = api.tx().balances().transferAllowDeath(destination, amount);

    ExtrinsicResult result = call.signAndWaitForResults(signingProvider);

    List<EventRecord> eventRecordList = result.getEvents();
    for (EventRecord eventRecord : eventRecordList) {
      logger.info("Event: " + eventRecord.method());
    }

    String hash = result.getHash();


    logger.info("Extrinsic hash: {}", hash);
  }

}
