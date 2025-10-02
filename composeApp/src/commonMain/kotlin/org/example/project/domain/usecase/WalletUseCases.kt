package org.example.project.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.project.data.model.*
import org.example.project.data.repository.CashuWalletRepository

/**
 * Container for all wallet use cases
 * This provides a clean API for the presentation layer
 */
class WalletUseCases(
    private val repository: CashuWalletRepository
) {
    val observeWalletState: ObserveWalletStateUseCase = ObserveWalletStateUseCase(repository)
    val initializeWallet: InitializeWalletUseCase = InitializeWalletUseCase(repository)
    val getBalance: GetBalanceUseCase = GetBalanceUseCase(repository)
    val sendTokens: SendTokensUseCase = SendTokensUseCase(repository)
    val receiveTokens: ReceiveTokensUseCase = ReceiveTokensUseCase(repository)
    val mintTokens: MintTokensUseCase = MintTokensUseCase(repository)
    val meltTokens: MeltTokensUseCase = MeltTokensUseCase(repository)
    val manageMints: ManageMintsUseCase = ManageMintsUseCase(repository)
    val getTransactionHistory: GetTransactionHistoryUseCase = GetTransactionHistoryUseCase(repository)
}

/**
 * Observes the current wallet state
 */
class ObserveWalletStateUseCase(
    private val repository: CashuWalletRepository
) {
    operator fun invoke(): Flow<WalletState> = repository.walletState
}

/**
 * Initializes the wallet with a mint
 */
class InitializeWalletUseCase(
    private val repository: CashuWalletRepository
) {
    suspend operator fun invoke(mintUrl: String): WalletResult<Unit> {
        if (mintUrl.isBlank()) {
            return WalletResult.Error("Mint URL cannot be empty")
        }
        return repository.initializeWallet(mintUrl)
    }
}

/**
 * Gets the current wallet balance
 */
class GetBalanceUseCase(
    private val repository: CashuWalletRepository
) {
    suspend operator fun invoke(): WalletResult<Long> {
        return repository.getBalance()
    }
}

/**
 * Sends tokens to another user
 */
class SendTokensUseCase(
    private val repository: CashuWalletRepository
) {
    suspend operator fun invoke(amount: Long, memo: String? = null): WalletResult<String> {
        if (amount <= 0) {
            return WalletResult.Error("Amount must be greater than 0")
        }

        // Check if we have sufficient balance
        val balanceResult = repository.getBalance()
        if (balanceResult is WalletResult.Success && balanceResult.data < amount) {
            return WalletResult.Error("Insufficient balance")
        }

        return repository.sendTokens(amount, memo)
    }
}

/**
 * Receives tokens from an encoded token string
 */
class ReceiveTokensUseCase(
    private val repository: CashuWalletRepository
) {
    suspend operator fun invoke(encodedToken: String): WalletResult<Long> {
        if (encodedToken.isBlank()) {
            return WalletResult.Error("Token string cannot be empty")
        }
        return repository.receiveTokens(encodedToken)
    }
}

/**
 * Mints new tokens (creates ecash)
 */
class MintTokensUseCase(
    private val repository: CashuWalletRepository
) {
    suspend operator fun invoke(amount: Long): WalletResult<CashuToken> {
        if (amount <= 0) {
            return WalletResult.Error("Amount must be greater than 0")
        }

        // Request a mint quote first
        val quoteResult = repository.requestMintQuote(amount)
        if (quoteResult !is WalletResult.Success) {
            return WalletResult.Error("Failed to get mint quote")
        }

        // Then mint the tokens
        return repository.mintTokens(quoteResult.data)
    }
}

/**
 * Melts tokens (converts back to Lightning)
 */
class MeltTokensUseCase(
    private val repository: CashuWalletRepository
) {
    suspend operator fun invoke(amount: Long, lightningInvoice: String): WalletResult<Unit> {
        if (amount <= 0) {
            return WalletResult.Error("Amount must be greater than 0")
        }
        if (lightningInvoice.isBlank()) {
            return WalletResult.Error("Lightning invoice cannot be empty")
        }

        // Check balance
        val balanceResult = repository.getBalance()
        if (balanceResult is WalletResult.Success && balanceResult.data < amount) {
            return WalletResult.Error("Insufficient balance")
        }

        // Request a melt quote
        val quoteResult = repository.requestMeltQuote(amount, lightningInvoice)
        if (quoteResult !is WalletResult.Success) {
            return WalletResult.Error("Failed to get melt quote")
        }

        // Melt the tokens
        return repository.meltTokens(quoteResult.data)
    }
}

/**
 * Manages mint operations (add/remove/list)
 */
class ManageMintsUseCase(
    private val repository: CashuWalletRepository
) {
    suspend fun addMint(mintUrl: String): WalletResult<MintInfo> {
        if (mintUrl.isBlank()) {
            return WalletResult.Error("Mint URL cannot be empty")
        }
        return repository.addMint(mintUrl)
    }

    suspend fun removeMint(mintUrl: String): WalletResult<Unit> {
        return repository.removeMint(mintUrl)
    }

    suspend fun getActiveMints(): WalletResult<List<MintInfo>> {
        return repository.getActiveMints()
    }
}

/**
 * Gets transaction history
 */
class GetTransactionHistoryUseCase(
    private val repository: CashuWalletRepository
) {
    suspend operator fun invoke(): WalletResult<List<Transaction>> {
        return repository.getTransactionHistory()
    }
}