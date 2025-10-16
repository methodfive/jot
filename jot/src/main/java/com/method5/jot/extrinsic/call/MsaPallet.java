package com.method5.jot.extrinsic.call;

import com.method5.jot.rpc.Api;
import com.method5.jot.rpc.CallOrQuery;
import com.method5.jot.scale.ScaleWriter;

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

}
