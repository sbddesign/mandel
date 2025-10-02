# Cashu Wallet Repository

This document describes the Cashu wallet repository implementation in the project.

## Overview

The Cashu wallet repository provides a clean, well-structured API for performing Cashu ecash wallet operations. It
follows clean architecture principles with separation of concerns across data, domain, and presentation layers.

## Architecture

```
data/
├── model/               # Data models
│   └── CashuModels.kt  # WalletState, MintInfo, CashuToken, etc.
└── repository/          # Repository layer
    ├── CashuWalletRepository.kt     # Interface
    └── CashuWalletRepositoryImpl.kt # Implementation

domain/
└── usecase/            # Business logic
    └── WalletUseCases.kt # Use cases for wallet operations
```

## Core Components

### 1. Data Models

#### WalletState

Represents the current state of the wallet:

```kotlin
data class WalletState(
    val balance: Long = 0L,
    val activeMints: List<MintInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
```

#### MintInfo

Information about a Cashu mint:

```kotlin
data class MintInfo(
    val url: String,
    val name: String? = null,
    val description: String? = null,
    val isActive: Boolean = true
)
```

#### CashuToken

Represents an ecash token:

```kotlin
data class CashuToken(
    val token: String,
    val amount: Long,
    val mint: String,
    val memo: String? = null
)
```

#### WalletResult

Result wrapper for operations:

```kotlin
sealed class WalletResult<out T> {
    data class Success<T>(val data: T) : WalletResult<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : WalletResult<Nothing>()
    data object Loading : WalletResult<Nothing>()
}
```

### 2. Repository Interface

The `CashuWalletRepository` interface defines all wallet operations:

#### Core Operations

- `initializeWallet(mintUrl: String)` - Initialize wallet with a mint
- `getBalance()` - Get current balance
- `walletState: Flow<WalletState>` - Observe wallet state

#### Token Operations

- `sendTokens(amount: Long, memo: String?)` - Send tokens to another user
- `receiveTokens(encodedToken: String)` - Receive tokens from encoded string

#### Minting/Melting

- `requestMintQuote(amount: Long)` - Request quote for minting
- `mintTokens(quoteId: String)` - Mint new tokens
- `requestMeltQuote(amount: Long, lightningInvoice: String)` - Request melt quote
- `meltTokens(quoteId: String)` - Melt tokens back to Lightning

#### Mint Management

- `addMint(mintUrl: String)` - Add a new mint
- `removeMint(mintUrl: String)` - Remove a mint
- `getActiveMints()` - Get list of active mints

#### History & Backup

- `getTransactionHistory()` - Get past transactions
- `backupWallet()` - Create wallet backup
- `restoreWallet(backupData: String)` - Restore from backup

### 3. Use Cases

The `WalletUseCases` class provides a clean API for the presentation layer with built-in validation:

```kotlin
class WalletUseCases(repository: CashuWalletRepository) {
    val observeWalletState: ObserveWalletStateUseCase
    val initializeWallet: InitializeWalletUseCase
    val getBalance: GetBalanceUseCase
    val sendTokens: SendTokensUseCase
    val receiveTokens: ReceiveTokensUseCase
    val mintTokens: MintTokensUseCase
    val meltTokens: MeltTokensUseCase
    val manageMints: ManageMintsUseCase
    val getTransactionHistory: GetTransactionHistoryUseCase
}
```

## Usage Examples

### Initialize the Wallet

```kotlin
val repository = CashuWalletRepositoryImpl()
val useCases = WalletUseCases(repository)

// Initialize with a mint
val result = useCases.initializeWallet("https://mint.example.com")
when (result) {
    is WalletResult.Success -> println("Wallet initialized")
    is WalletResult.Error -> println("Error: ${result.message}")
    is WalletResult.Loading -> println("Loading...")
}
```

### Observe Wallet State

```kotlin
// In a ViewModel or composable
useCases.observeWalletState().collect { state ->
    println("Balance: ${state.balance}")
    println("Active mints: ${state.activeMints.size}")
    println("Loading: ${state.isLoading}")
    state.error?.let { println("Error: $it") }
}
```

### Send Tokens

```kotlin
// Send 1000 sats with a memo
val result = useCases.sendTokens(1000L, "Payment for coffee")
when (result) {
    is WalletResult.Success -> {
        val encodedToken = result.data
        println("Token to share: $encodedToken")
        // Share this token with the recipient
    }
    is WalletResult.Error -> println("Error: ${result.message}")
    is WalletResult.Loading -> println("Loading...")
}
```

### Receive Tokens

```kotlin
// Receive tokens from an encoded string
val encodedToken = "cashuA..." // Token received from sender
val result = useCases.receiveTokens(encodedToken)
when (result) {
    is WalletResult.Success -> {
        val amount = result.data
        println("Received: $amount sats")
    }
    is WalletResult.Error -> println("Error: ${result.message}")
    is WalletResult.Loading -> println("Loading...")
}
```

### Mint New Tokens

```kotlin
// Mint 5000 sats worth of tokens
val result = useCases.mintTokens(5000L)
when (result) {
    is WalletResult.Success -> {
        val token = result.data
        println("Minted token: ${token.token}")
    }
    is WalletResult.Error -> println("Error: ${result.message}")
    is WalletResult.Loading -> println("Loading...")
}
```

### Melt Tokens (Pay Lightning Invoice)

```kotlin
val invoice = "lnbc..." // Lightning invoice to pay
val result = useCases.meltTokens(1000L, invoice)
when (result) {
    is WalletResult.Success -> println("Payment successful")
    is WalletResult.Error -> println("Error: ${result.message}")
    is WalletResult.Loading -> println("Loading...")
}
```

### Manage Mints

```kotlin
// Add a new mint
val addResult = useCases.manageMints.addMint("https://newmint.example.com")

// Get active mints
val mintsResult = useCases.manageMints.getActiveMints()
when (mintsResult) {
    is WalletResult.Success -> {
        mintsResult.data.forEach { mint ->
            println("Mint: ${mint.name ?: mint.url}")
        }
    }
    is WalletResult.Error -> println("Error: ${mintsResult.message}")
    is WalletResult.Loading -> println("Loading...")
}

// Remove a mint
val removeResult = useCases.manageMints.removeMint("https://oldmint.example.com")
```

### Get Transaction History

```kotlin
val result = useCases.getTransactionHistory()
when (result) {
    is WalletResult.Success -> {
        result.data.forEach { tx ->
            println("${tx.type}: ${tx.amount} sats - ${tx.status}")
        }
    }
    is WalletResult.Error -> println("Error: ${result.message}")
    is WalletResult.Loading -> println("Loading...")
}
```

## Integration with cdk-kotlin

The repository implementation has placeholder TODOs for integrating with the official Cashu Development Kit (
cdk-kotlin). Here's how to integrate:

```kotlin
// In CashuWalletRepositoryImpl.kt

import org.cashudevkit.cdk.*

class CashuWalletRepositoryImpl : CashuWalletRepository {
    
    private var wallet: Wallet? = null
    
    override suspend fun initializeWallet(mintUrl: String): WalletResult<Unit> {
        return try {
            // Initialize the CDK wallet
            wallet = Wallet.builder()
                .addMint(mintUrl)
                .build()
                
            // Rest of implementation...
        } catch (e: Exception) {
            WalletResult.Error("Failed to initialize: ${e.message}", e)
        }
    }
    
    // Implement other methods using the wallet instance...
}
```

## Testing

Example test structure:

```kotlin
class CashuWalletRepositoryTest {
    
    @Test
    fun `initialize wallet returns success`() = runTest {
        val repository = CashuWalletRepositoryImpl()
        val result = repository.initializeWallet("https://test.mint")
        
        assertTrue(result is WalletResult.Success)
    }
    
    @Test
    fun `send tokens with zero amount returns error`() = runTest {
        val repository = CashuWalletRepositoryImpl()
        val useCases = WalletUseCases(repository)
        
        val result = useCases.sendTokens(0L)
        
        assertTrue(result is WalletResult.Error)
        assertEquals("Amount must be greater than 0", (result as WalletResult.Error).message)
    }
}
```

## Next Steps

1. **Integrate cdk-kotlin**: Replace TODO comments with actual CDK calls
2. **Add persistence**: Implement local storage for wallet data
3. **Add error handling**: Enhance error messages and recovery
4. **Implement backup/restore**: Add full backup and restore functionality
5. **Add tests**: Write comprehensive unit and integration tests
6. **Add logging**: Implement logging for debugging and monitoring

## References

- [Cashu Protocol Specification](https://github.com/cashubtc/nuts)
- [cdk-kotlin on Maven Central](https://central.sonatype.com/artifact/org.cashudevkit/cdk-kotlin)
- [Cashu Website](https://cashu.space/)