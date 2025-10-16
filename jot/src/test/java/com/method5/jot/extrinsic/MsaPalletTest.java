package com.method5.jot.extrinsic;

import com.method5.jot.TestBase;
import com.method5.jot.util.HexUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MsaPalletTest extends TestBase {

  @Disabled("Metadata has to be updated to use a frequency one, but that'll screw up all the other tests")
  @Test
  public void testCreateMsa() {
    byte[] callData = api.tx().msa().createMsa().callData();
    assertNotNull(callData);
    assertEquals("3c00", HexUtil.bytesToHex(callData));
  }
}
