package com.method5.jot.extrinsic;

import com.method5.jot.entity.DispatchError;
import com.method5.jot.events.EventRecord;
import com.method5.jot.extrinsic.call.*;
import com.method5.jot.rpc.PolkadotWs;
import com.method5.jot.signing.SigningProvider;
import com.method5.jot.util.HexUtil;
import com.method5.jot.wallet.Wallet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Testcontainers
public class E2ETest {
  public final static String FREQUENCY_VERSION = "v1.17.5";

  @Container
  public static GenericContainer<?> frequencyTestContainer = new GenericContainer<>(DockerImageName.parse(String.format("frequencychain/standalone-node:%s", FREQUENCY_VERSION)))
      .withExposedPorts(30333, 9944, 9933)
      .waitingFor(org.testcontainers.containers.wait.strategy.Wait.forLogMessage(".*Running JSON-RPC server.*", 1));

  @BeforeAll
  public static void setup() {
    frequencyTestContainer.start();
  }

  public static String getWsAddress() {
    return String.format("ws://%s:%s", frequencyTestContainer.getHost(), frequencyTestContainer.getMappedPort(9944));
  }

  public Class<? extends EventClass<?>> mapEventNameToEventClass(String methodName){
    switch (methodName) {
      case "MsaCreated":
        return MsaPallet.MsaCreated.class;
      default:
        return null;
    }
  }

  public Class<? extends ExtrinsicError> mapErrorNameToErrorClass(String errorName, int moduleIndex){
    switch (errorName) {
      case "KeyAlreadyRegistered":
        return KeyAlreadyRegisteredError.class;
      default:
        return null;
    }
  }

  @Test
  public void msaTest() {
    try(PolkadotWs api = new PolkadotWs(getWsAddress())) {
      String chain = api.query().system().chain();
      Assertions.assertEquals("Frequency Development (No Relay)", chain);

      Wallet alice = Wallet.fromSr25519Seed(HexUtil.hexToBytes("0xe5be9a5092b81bca64be81d212e7f2f9eba183bb7a90954f7b76361f6edb5c0a"));
      Assertions.assertEquals("5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY", alice.getAddress(42));

      SigningProvider aliceSigningProvider = alice.getSigner();

      Call call = api.tx().msa().createMsa();

      ExtrinsicResult result = call.signAndWaitForResults(aliceSigningProvider);

      List<EventRecord> eventRecordList = result.getEvents();
      for (EventRecord eventRecord : eventRecordList) {
        System.out.println("Event: " + eventRecord.method());
      }

      EventRecord msaCreated = eventRecordList.stream().filter(r -> Objects.equals(r.method(), "MsaCreated")).toList().getFirst();
      Assertions.assertNotNull(msaCreated);

      Class<? extends EventClass<?>> eventClass = mapEventNameToEventClass(msaCreated.method());

      Object msaCreatedObject = eventClass.getMethod("create", Map.class).invoke(null, msaCreated.attributes());

      Assertions.assertInstanceOf(MsaPallet.MsaCreated.class, msaCreatedObject);
      Assertions.assertEquals(1, ((MsaPallet.MsaCreated) msaCreatedObject).getMsaId().getValue().intValue());

      String hash = result.getHash();

      System.out.println("Extrinsic hash: " + hash);

      ExtrinsicResult failure = call.signAndWaitForResults(aliceSigningProvider);

      //Step 1 dereference Error name to Error object type (moduleIndex, name) should be unique enough
      //Step 2 There are no error attributes so you can just use an enum from step 1


      System.out.println(failure.getError().toHuman());
      Assertions.assertEquals("Module[60] Error[0]: KeyAlreadyRegistered", failure.getError().toHuman());

      Object error = mapErrorNameToErrorClass(failure.getError().name, failure.getError().moduleIndex);
      Assertions.assertInstanceOf(KeyAlreadyRegisteredError.class, error);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @AfterAll
  public static void teardown() {
    frequencyTestContainer.stop();
  }
}
