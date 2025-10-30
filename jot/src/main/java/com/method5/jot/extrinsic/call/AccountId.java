package com.method5.jot.extrinsic.call;

import java.util.Arrays;

public class AccountId {
  private final byte[] publicKeyBytes;

  public AccountId(byte[] publicKeyBytes) {
    this.publicKeyBytes = publicKeyBytes;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof AccountId)) return false;
    AccountId that = (AccountId) other;
    return Arrays.equals(this.publicKeyBytes, that.publicKeyBytes);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(publicKeyBytes);
  }

  public static AccountId fromBytes(byte[] data) {
    return new AccountId(data);
  }
}
