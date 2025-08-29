# 🧠 Jot – A Modern Java SDK for Polkadot

**Jot** is a clean, modular SDK built from the ground up for developers working with **Polkadot** and **Substrate-based chains**.

> ⚡️ Everything you need to build real Substrate connected apps in Java — no outdated libs, no workarounds.
---

## ✨ Features

✅ Custom SCALE encoding/decoding  
✅ Runtime metadata parsing (v14+)  
✅ Full transaction construction & signing  
✅ Substrate JSON-RPC (HTTP + WSS)  
✅ SS58 address encoding/decoding  
✅ Cryptographic signing via `sr25519` and `ed25519`  
✅ Fee estimation  
✅ Multisig transactions  
✅ Batched extrinsics  
✅ Persistent wallet file abstraction

---

## 📦 Technology Stack

- Java 21+
- Maven (multi-module)

---

## 🔧 Getting Started

Requires **Java 21+** and **Maven** or **Gradle**.

### Import via Maven

```xml
<dependency>
  <groupId>com.method5</groupId>
  <artifactId>jot</artifactId>
  <version>1.0.0</version>
</dependency>
```

**Gradle (Kotlin DSL):**
```kotlin
implementation("com.method5:jot:1.0.0")
```

---

## 🧰 Modules

| Module       | Description                                                   |
|--------------|---------------------------------------------------------------|
| jot          | SCALE codec, metadata, crypto, rpc, signing, extrinsics, etc. |
| jot-examples | 50+ ready-to-run usage examples                               |
| jot-docs     | Jot SDK API docs                                              |

---

## 🚀 Usage Examples

### 🛡️ Create or Load Wallet
```java
// Load wallet from file
Wallet wallet = Wallet.fromFile(new File("wallet.key"));

// or load from mnemonic
wallet = Wallet.fromMnemonic("gesture rather obey video awake marine ...");
System.out.println(wallet.getAddress());

// or create new
wallet = Wallet.generate();
wallet.saveToFile(new File("wallet.key"));
System.out.println(wallet.getAddress());
```

### 💸 Send a Transfer (Balances.transferKeepAlive)
```java
try (PolkadotRpcClient rpc = new PolkadotRpcClient(new String[] { ExampleConstants.RPC_SERVER }, 10000)) {
    String to = "15..."; // destination address
    BigInteger amount = BigInteger.ONE; // 1 DOT
    
    // Build call data for Balances.transferKeepAlive
    byte[] callData = BalancesPallet.transferKeepAlive(
        rpc.getResolver(),
        to,
        amount
    );
    
    // Create and sign extrinsic
    byte[] extrinsic = ExtrinsicSigner.signAndBuild(rpc,
        wallet.getSigner(),
        callData
    );
    
    // Submit extrinsic to RPC
    String hash = AuthorRpc.submitExtrinsic(rpc, extrinsic);
    
    System.out.println("Extrinsic hash: "+ hash);
}

```

### 🛠️ Decode a Signed Extrinsic
```java
String hex = "0x..."; // signed extrinsic hex
DecodedExtrinsic decoded = ExtrinsicDecoder.decode(hex, chain);

System.out.println(decoded);
```

### 📥 Subscribe to finalized blocks
```java
// Subscribe to finalized blocks
Subscription<BlockHeader> subscription = new Subscription<>(
    SubscriptionType.FINALIZED_HEAD,
    rpc,
    header -> {
        System.out.println("New head: " + header);
    }
);

// Unsubscribe
subscription.unsubscribe();
```

### 🔍 Query wallet balance
```java
// Account
AccountId account = AccountId.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB");

// Query balance
AccountInfo accountInfo = StorageQuery.getAccountInfo(rpc, account);

System.out.println("Free balance: " + accountInfo.getFree());
System.out.println("Reserved balance: " + accountInfo.getReserved());
System.out.println("Frozen balance: " + accountInfo.getFrozen());
```

### ⏱️ Submit extrinsic and wait for result
```java
// Build call data for System.remark
byte[] callData = SystemPallet.remark(
    client.getResolver(),
    "test".getBytes(StandardCharsets.UTF_8) // remark
);

// Create and sign extrinsic
byte[] extrinsic = ExtrinsicSigner.signAndBuild(client,
    wallet.getSigner(),
    callData
);

// Submit to RPC and wait for result
ExtrinsicResult result = client.submitAndWaitForExtrinsic(extrinsic, PolkadotWsClient.Confirmation.BEST, 10000);

// Result
System.out.println("Successful: " + result.isSuccess());

// Individual events related to extrinsic
System.out.println("Events: " + Arrays.toString(result.getEvents()));

// Failure reason (if it failed)
if (!result.isSuccess()) {
    System.err.println("Failed because: " + result.getError().toHuman());
}
```

👉 For full setup instructions, see [Quick Start Guide](https://methodfive.github.io/jot/quickstart/).  

👉 API reference: [Javadoc](https://methodfive.github.io/jot/api/index.html).  

👉 See [jot-examples](https://github.com/methodfive/jot/tree/main/jot-examples/src/main/java/com/method5/jot/examples) module for 50+ ready to run examples.

---

## 📄 License

This project is licensed under the Apache License 2.0.

---

Built with ❤️ to bring Polkadot to the Java ecosystem.
