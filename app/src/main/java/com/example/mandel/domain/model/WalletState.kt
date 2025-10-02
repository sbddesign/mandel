package com.example.mandel.domain.model

/**
 * Represents the current state of the wallet
 */
data class WalletState(
    val balance: ULong = 0UL,
    val isInitialized: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val mnemonic: String? = null
)

/**
 * Represents the state of a mint operation
 */
data class MintState(
    val quoteId: String? = null,
    val amount: ULong = 0UL,
    val paymentRequest: String? = null,
    val isProcessing: Boolean = false,
    val isCompleted: Boolean = false,
    val error: String? = null
)

/**
 * Represents the state of a melt (send) operation
 */
data class MeltState(
    val quoteId: String? = null,
    val invoice: String? = null,
    val amount: ULong = 0UL,
    val fee: ULong = 0UL,
    val isProcessing: Boolean = false,
    val isCompleted: Boolean = false,
    val isPaid: Boolean = false,
    val error: String? = null
)

/**
 * Represents the state of sending/receiving tokens
 */
data class TokenTransferState(
    val token: String? = null,
    val amount: ULong = 0UL,
    val isProcessing: Boolean = false,
    val isCompleted: Boolean = false,
    val error: String? = null
)