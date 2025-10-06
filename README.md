# 🧠 Jot – A Modern Java SDK for Polkadot

[![Maven Central](https://img.shields.io/maven-central/v/com.method5/jot.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/com.method5/jot)
[![Javadoc](https://img.shields.io/badge/docs-Javadoc-informational.svg)](https://methodfive.github.io/jot/api/index.html)

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

(If you want to build Jot from source, see [Building Jot from Source](#%EF%B8%8F-building-jot-from-source).)

### Import via Maven

```xml
<dependency>
  <groupId>com.method5</groupId>
  <artifactId>jot</artifactId>
  <version>1.0.2</version>
</dependency>
```

**Gradle (Kotlin DSL):**
```kotlin
implementation("com.method5:jot:1.0.2")
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
try (PolkadotRpc api = new PolkadotRpc(new String[] { Config.WSS_SERVER }, 10000)) {
    String to = "15..."; // destination address
    BigInteger amount = BigInteger.valueOf(1); // 1 DOT
    
    Call call = api.tx().balances().transferKeepAlive(to, amount);
    
    String hash = call.signAndSend(wallet.getSigner());
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
Subscription<BlockHeader> subscription = api.subscribe().finalizedHeads(
    header -> {
        System.out.println("New finalized head: " + header);
    }
);

// Unsubscribe
subscription.unsubscribe();
```

> ⚠️ Note: api must be of type PolkadotWs for subscriptions!


### 🔍 Query wallet balance
```java
// Account
AccountId account = AccountId.fromSS58("13UVJyLnbVp9RBZYFwFGyDvVd1y27Tt8tkntv6Q7JVPhFsTB");

// Query balance
AccountInfo accountInfo = api.query().storage().accountInfo(account);

System.out.println("Free balance: " + accountInfo.getFree());
System.out.println("Reserved balance: " + accountInfo.getReserved());
System.out.println("Frozen balance: " + accountInfo.getFrozen());
```

### ⏱️ Submit extrinsic and wait for result
```java
Call call = api.tx().system().remark("test");
        
ExtrinsicResult result = call.signAndWaitForResults(wallet.getSigner());

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

## 🛠️ Building Jot from Source

If you want to build Jot from source, you’ll need:

- Java 21+
- Maven 3.9+
- Rust 1.81.0+

> ⚠️ After installing Rust, make sure your environment is configured by sourcing `~/.cargo/env`:
> ```bash
> source ~/.cargo/env
> ```

Building Jot:

```bash
# (Optional) make sure the build script is executable
chmod 755 ./build.sh

./build.sh
```

---

## 📄 License

This project is licensed under the Apache License 2.0.

---

Built with ❤️ to bring Polkadot to the Java ecosystem.
