package org.example.project.data.model

/**
 * Represents the current state of a Cashu wallet
 */
data class WalletState(
    val balance: Long = 0L,
    val activeMints: List<MintInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Information about a Cashu mint
 */
data class MintInfo(
    val url: String,
    val name: String? = null,
    val description: String? = null,
    val isActive: Boolean = true
)

/**
 * Represents a token (ecash) in the wallet
 */
data class CashuToken(
    val token: String,
    val amount: Long,
    val mint: String,
    val memo: String? = null
)

/**
 * Result type for wallet operations
 */
sealed class WalletResult<out T> {
    data class Success<T>(val data: T) : WalletResult<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : WalletResult<Nothing>()
    data object Loading : WalletResult<Nothing>()
}

/**
 * Transaction history item
 */
data class Transaction(
    val id: String,
    val type: TransactionType,
    val amount: Long,
    val timestamp: Long,
    val status: TransactionStatus,
    val memo: String? = null
)

enum class TransactionType {
    SEND,
    RECEIVE,
    MINT,
    MELT
}

enum class TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED
}