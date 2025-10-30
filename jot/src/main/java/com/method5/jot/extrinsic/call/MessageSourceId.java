package com.method5.jot.extrinsic.call;

import java.math.BigInteger;
import java.util.Objects;

public class MessageSourceId {
  private final BigInteger value;

  public MessageSourceId(BigInteger value) {
    this.value = value;
  }

  public BigInteger getValue() {
    return value;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    MessageSourceId that = (MessageSourceId) other;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
