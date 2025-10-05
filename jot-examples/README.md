# 🧩 Jot Examples

Runnable examples that demonstrate how to use the **Jot (Polkadot Java SDK)** to connect to a node, query on-chain data, and submit transactions.  
These examples are configured to run against the **Westend** test network and are intended to **succeed when run** using a funded test account.

---

## ⚙️ Configure Endpoint & Wallet

All configuration lives in:

```
com.method5.jot.examples.Config
```

Edit this file to set:

- **WSS endpoint(s) for relay chain** (default recommended: `wss://westend.api.onfinality.io/public-ws`)
- **WSS endpoint(s) for asset hub** (default recommended: `wss://asset-hub-westend-rpc.n.dwellir.com`)
- **Wallet mnemonic phrase** (12 or 24 words) for a **funded Westend** account. See ["Out of Funds" - How to Fix](#-out-of-funds--how-to-fix)

---

## ▶️ How to Run

From the `jot-examples` module:

```bash
# Build everything
mvn -q -DskipTests package

# Run any example (Replace <ExampleClass> with one below)
mvn exec:java -Dexec.mainClass="<ExampleClass>"

# For example:
# mvn exec:java -Dexec.mainClass="com.method5.jot.examples.query.QueryAccountAndBalanceExample"
```

---

## 📚 Examples

### 🔍 Queries (`com.method5.jot.examples.query`)

| Example | Description |
|---|---|
| **ManualQueryExample** | Perform a manual RPC query with raw parameters and inspect the JSON response. |
| **QueryAccountAndBalanceExample** | Retrieve `system.account` data and balances for a given address. |
| **QueryAssetBalanceExample** | Query balances for assets on Asset Hub. |
| **QueryBlockExample** | Fetch a block and print its contents. |
| **QueryBlockHashExample** | Resolve block numbers to block hashes. |
| **QueryBlockHeaderExample** | Query and decode block headers. |
| **QueryBlockEventsExample** | Read and decode system events for a specific block. |
| **QueryFeeInfoExample** | Retrieve fee and weight information for a call. |
| **QueryFinalizedHeadExample** | Get the hash of the latest finalized block. |
| **QueryMetadataExample** | Retrieve and decode runtime metadata. |
| **QueryNonceExample** | Query an account’s current transaction nonce. |
| **QueryRuntimeVersionExample** | Get the runtime version and spec information. |
| **QuerySystemDetailsExample** | Fetch chain name, properties, and node system info. |

---

### 🔢 SCALE Encoding (`com.method5.jot.examples.scale`)

| Example | Description |
|---|---|
| **ScaleEncodingExample** | Demonstrate how to encode and decode values using the SCALE codec. |

---

### 🏷️ SS58 (`com.method5.jot.examples.ss58`)

| Example | Description |
|---|---|
| **AddressConversionExample** | Convert between public keys and SS58 addresses across networks. |

---

### 🧠 Subscriptions (`com.method5.jot.examples.subscription`)

| Example | Description |
|---|---|
| **SubscribeAllHeadsExample** | Subscribe to *all* new block headers. |
| **SubscribeBestHeadsExample** | Subscribe to *best* (non-finalized) block headers. |
| **SubscribeFinalizedHeadsExample** | Subscribe to finalized block headers. |
| **SubscribeLatestHeadsExample** | Stream the latest block headers in real time. |
| **SubscribeRuntimeVersionExample** | Subscribe to runtime version updates. |

---

### 💸 Extrinsics (`com.method5.jot.examples.extrinsic`)

| Example | Description |
|---|---|
| **BalancesTransferAllExample** | Submit a `balances.transferAll` transaction. |
| **BalancesTransferAllowDeathExample** | Submit a `balances.transferAllowDeath` transaction. |
| **BalancesTransferKeepAliveExample** | Submit a `balances.transferKeepAlive` transaction. |
| **ConvictionVotingVoteExample** | Cast a governance vote using conviction voting. |
| **ConvictionVotingRemoveVoteExample** | Remove a previously cast governance vote. |
| **MultisigAsMultiExample** | Create and send a multisig transaction with multiple signers. |
| **MultisigApproveAsMultiExample** | Approve and execute an existing multisig transaction. |
| **ReceiveExtrinsicResultExample** | Submit an extrinsic and wait for inclusion/finalization events. |
| **StakingBondExample** | Bond funds to become a nominator or validator. |
| **StakingBondExtraExample** | Add additional bonded funds. |
| **StakingChillExample** | Stop nominating (chill). |
| **StakingNominateExample** | Nominate validators. |
| **StakingPayoutStakersExample** | Payout staking rewards for a given era. |
| **StakingRebondExample** | Rebond previously unbonded funds. |
| **StakingUnbondExample** | Unbond part of your staking balance. |
| **SystemRemarkExample** | Submit an on-chain remark message. |
| **UtilityBatchExample** | Send multiple calls in a single batched transaction. |
| **UtilityBatchAllExample** | Send multiple calls in an atomic batch (`batch_all`). |
| **UtilityForceBatchExample** | Force batch multiple calls, continuing on errors. |

---

### 🔑 Keys (`com.method5.jot.examples.keys`)

| Example | Description |
|---|---|
| **Ed25519WalletExample** | Generate, sign, and verify using Ed25519 keys. |
| **GenericWalletExample** | Demonstrate wallet creation, SS58 address derivation, and signing. |

---

### 🧩 Metadata (`com.method5.jot.examples.metadata`)

| Example | Description |
|---|---|
| **ParsingMetadataExample** | Parse and inspect runtime metadata (modules, calls, events, constants). |

---

## 💰 “Out of Funds” — How to Fix

Transaction examples need WND to cover fees. If you see errors like **InsufficientBalance**:

1. Open the **Polkadot Faucet**: https://faucet.polkadot.io/
2. Select **Westend**.
3. Paste your **Westend address** (from `Config.java`).
4. Complete the CAPTCHA and request funds.

> Most examples work best with **≥ 1 WND** (Westend uses 12 decimals).

---

## 📝 Notes

- Target network: **Westend** (testnet).
- Query-only examples (Wallet utility, RPC, subscriptions) run without funds.
- Transaction examples require a funded mnemonic; they may **gracefully skip** if no balance is detected (depending on the example).

---

## 🔗 Useful Links

- Jot (Polkadot Java SDK): https://github.com/Method5/jot
- Westend Faucet: https://faucet.polkadot.io/
- Westend Explorer: https://westend.subscan.io
