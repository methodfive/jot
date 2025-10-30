package com.method5.jot.extrinsic.call;

import com.method5.jot.metadata.RuntimeTypeDecoder;
import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.scale.ScaleWriter;

import java.math.BigInteger;
import java.util.*;

public class MsaPallet extends CallOrQuery {
  public MsaPallet(Api api) {
    super(api);
  }

  public Call createMsa() {
    return new Call(api, createMsaWriter(
       getResolver().resolveCallIndex("Msa", "create")
    ));
  }

  private byte[] createMsaWriter(byte[] callIndex) {
    ScaleWriter writer = new ScaleWriter();
    writer.writeBytes(callIndex);
    return writer.toByteArray();
  }

  public static class MsaCreated implements EventClass<MsaCreated>{
    private final MessageSourceId msaId;
    private final AccountId accountId;

    public MsaCreated(MessageSourceId msaId, AccountId accountId) {
      this.msaId = msaId;
      this.accountId = accountId;
    }

    public MessageSourceId getMsaId() {
      return msaId;
    }

    public AccountId getAccountId() {
      return accountId;
    }

    public static MsaCreated create(Map<String, RuntimeTypeDecoder.TypeAndValue> attributes) {
      BigInteger msaIdValue = new BigInteger(attributes.get("MessageSourceId").getValue().toString());
      MessageSourceId msaId = new MessageSourceId(msaIdValue);
      ArrayList<Byte> byteList = (ArrayList<Byte>) ((HashMap) attributes.get("T::AccountId").getValue()).get("field1");
      byte[] accountIdValue = new byte[byteList.size()];
      for (int i = 0; i < byteList.size(); i++) {
        accountIdValue[i] = byteList.get(i);
      }
      AccountId accountId = new AccountId(accountIdValue);

      return new MsaCreated(msaId, accountId);
    }
  }

}

