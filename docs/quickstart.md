# 🚀 Quick Start with Jot

This guide walks you through setting up a Java project with **Jot**, and interacting with the Polkadot blockchain.

---

## 📋 Prerequisites
- Java **21+**
- Maven **3.9+** or Gradle **7+**
- A Polkadot/Substrate node RPC endpoint (e.g., `wss://rpc.polkadot.io`)

---

## ⚙️ 1) Create a new project

### Maven (quickstart archetype)
```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=hello-jot \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
cd hello-jot
```

Add Jot to your `pom.xml`:
```xml
<dependencies>
  <dependency>
    <groupId>com.method5</groupId>
    <artifactId>jot</artifactId>
    <version>1.0.2</version>
  </dependency>
</dependencies>
```

(Optional) Add the Maven Exec plugin so you can run `main` easily:
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>exec-maven-plugin</artifactId>
      <version>3.5.0</version>
      <configuration>
        <mainClass>com.example.HelloPolkadot</mainClass>
      </configuration>
    </plugin>
  </plugins>
</build>
```

### Gradle (application template)
```bash
gradle init --type java-application
cd hello-jot
```

Add Jot to `build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.method5:jot:1.0.2")
}
```

Ensure the application plugin has your main class:
```kotlin
plugins {
    application
}

application {
    mainClass.set("com.example.HelloPolkadot")
}
```

---

## 🔌 2) Connect to a node (“Hello Polkadot”)

Create `src/main/java/com/example/HelloPolkadot.java`:
```java
package com.example;

import com.method5.jot.rpc.PolkadotWs;

public class HelloPolkadot {
    public static void main(String[] args) throws Exception {
        try (PolkadotWs api = new PolkadotWs("wss://polkadot.api.onfinality.io/public-ws")) {
            System.out.println("Connected to chain: " + api.query().system().chain());
            System.out.println("Genesis hash: " + api.query().chain().genesisBlockHash());
        }
    }
}
```

Run it:

**Maven**
```bash
mvn -q compile exec:java
```

**Gradle**
```bash
./gradlew run
```

---

## 🧪 3) Where to go next

- Review the **[README](https://github.com/methodfive/jot)** for most common usage examples.
- Explore the **[jot-examples](https://github.com/methodfive/jot/tree/main/jot-examples)** module for an additional 50+ runnable samples.
- Browse the **[API Javadoc](https://methodfive.github.io/jot/api/index.html)** for JOT API information.
- Build real apps on Polkadot in Java 🚀

---

## 🛠️ Troubleshooting (quick)

- **Java version**: Ensure `java -version` reports **21+**.
- **WebSocket blocked**: Some corporate networks/proxies block WSS. Try another network or a self-hosted node.
- **Native libs**: If you see `UnsatisfiedLinkError`, ensure your runtime can load Jot’s native libs for your OS/arch. Check release notes for classifiers and verify `java.library.path` if needed.
