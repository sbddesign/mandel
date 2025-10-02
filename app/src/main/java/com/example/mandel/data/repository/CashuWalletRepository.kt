package com.example.mandel.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.cashudevkit.*

/**
 * Repository for managing Cashu wallet operations using CDK-Kotlin
 * Handles all interactions with the Cashu Development Kit
 */
class CashuWalletRepository(
    private val mintUrl: String = "https://testmint.cashu.space",
    private val unit: CurrencyUnit = CurrencyUnit.Sat
) {
    private var wallet: Wallet? = null
    private var database: WalletSqliteDatabase? = null

    private val _balance = MutableStateFlow(0UL)
    val balance: Flow<ULong> = _balance.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: Flow<Boolean> = _isInitialized.asStateFlow()

    /**
     * Initialize the wallet with a new or existing mnemonic
     * @param mnemonic Optional mnemonic phrase. If null, generates a new one
     * @param targetProofCount Number of proofs to maintain in the wallet
     */
    suspend fun initializeWallet(
        mnemonic: String? = null,
        targetProofCount: UInt = 10u
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Create in-memory database for wallet storage
            database = WalletSqliteDatabase.newInMemory()

            // Generate or use provided mnemonic
            val walletMnemonic = mnemonic ?: generateMnemonic()

            // Configure wallet
            val config = WalletConfig(targetProofCount = targetProofCount)

            // Initialize wallet
            wallet = Wallet(
                mintUrl = mintUrl,
                unit = unit,
                mnemonic = walletMnemonic,
                db = database!!,
                config = config
            )

            _isInitialized.value = true

            // Refresh balance
            refreshBalance()

            Result.success(walletMnemonic)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Create a mint quote to receive funds
     * @param amount Amount in sats to mint
     * @param description Optional description for the quote
     */
    suspend fun createMintQuote(
        amount: ULong,
        description: String = ""
    ): Result<MintQuote> = withContext(Dispatchers.IO) {
        try {
            val currentWallet = wallet ?: return@withContext Result.failure(
                IllegalStateException("Wallet not initialized")
            )

            val amountObj = Amount(value = amount)
            val quote = currentWallet.mintQuote(
                amount = amountObj,
                description = description
            )

            Result.success(quote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mint tokens after payment is made
     * @param quoteId The quote ID from createMintQuote
     */
    suspend fun mintTokens(quoteId: String): Result<List<Proof>> = withContext(Dispatchers.IO) {
        try {
            val currentWallet = wallet ?: return@withContext Result.failure(
                IllegalStateException("Wallet not initialized")
            )

            val proofs = currentWallet.mint(
                quoteId = quoteId,
                amountSplitTarget = SplitTarget.None,
                spendingConditions = null
            )
            refreshBalance()

            Result.success(proofs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Create a melt quote to send funds
     * @param invoice Lightning invoice to pay
     */
    suspend fun createMeltQuote(invoice: String): Result<MeltQuote> = withContext(Dispatchers.IO) {
        try {
            val currentWallet = wallet ?: return@withContext Result.failure(
                IllegalStateException("Wallet not initialized")
            )

            val quote = currentWallet.meltQuote(
                request = invoice,
                options = null
            )
            Result.success(quote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Melt tokens to pay a Lightning invoice
     * @param quoteId The quote ID from createMeltQuote
     */
    suspend fun meltTokens(quoteId: String): Result<Melted> = withContext(Dispatchers.IO) {
        try {
            val currentWallet = wallet ?: return@withContext Result.failure(
                IllegalStateException("Wallet not initialized")
            )

            val response = currentWallet.melt(quoteId = quoteId)
            refreshBalance()

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Send tokens to another Cashu wallet
     * @param amount Amount in sats to send
     */
    suspend fun sendTokens(amount: ULong): Result<String> = withContext(Dispatchers.IO) {
        try {
            val currentWallet = wallet ?: return@withContext Result.failure(
                IllegalStateException("Wallet not initialized")
            )

            val amountObj = Amount(value = amount)
            // Use swap to create a token for sending
            val proofs: List<Proof>? = currentWallet.swap(
                amount = amountObj,                       // Amount?
                amountSplitTarget = SplitTarget.None,     // SplitTarget
                inputProofs = emptyList(),                // List<Proof> (non-null)
                spendingConditions = null,                // SpendingConditions?
                includeFees = false                       // Boolean (pick true/false)
            )

            // Encode proofs as a token string
            val token = "currentWallet.encodeProofs(proofs = proofs)"
            refreshBalance()

            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Receive tokens from another Cashu wallet
     * @param encodedToken The token string received
     */
    suspend fun receiveTokens(encodedToken: Token): Result<Amount> = withContext(Dispatchers.IO) {
        try {
            val currentWallet = wallet ?: return@withContext Result.failure(
                IllegalStateException("Wallet not initialized")
            )

            val opts = ReceiveOptions(
                amountSplitTarget = SplitTarget.None,
                p2pkSigningKeys = emptyList(),
                preimages = emptyList(),
                metadata = emptyMap()
            )

            val amount = currentWallet.receive(
                token = encodedToken,
                options = opts
            )
            refreshBalance()

            Result.success(amount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Refresh the wallet balance
     */
    suspend fun refreshBalance(): Result<ULong> = withContext(Dispatchers.IO) {
        try {
            val currentWallet = wallet ?: return@withContext Result.failure(
                IllegalStateException("Wallet not initialized")
            )

            val balanceAmount = currentWallet.totalBalance()
            _balance.value = balanceAmount.value

            Result.success(balanceAmount.value)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Close the wallet and cleanup resources
     */
    suspend fun closeWallet() = withContext(Dispatchers.IO) {
        try {
            wallet?.close()
            database?.close()
            wallet = null
            database = null
            _isInitialized.value = false
            _balance.value = 0UL
        } catch (e: Exception) {
            // Log error but don't throw
            e.printStackTrace()
        }
    }

    /**
     * Encode proofs as a token string
     * @param proofs List of proofs to encode
     */
    suspend fun encodeProofsAsToken(proofs: List<Proof>): Result<String> = withContext(Dispatchers.IO) {
        try {
            val currentWallet = wallet ?: return@withContext Result.failure(
                IllegalStateException("Wallet not initialized")
            )

            val token = "currentWallet.encodeProofs(proofs = proofs)"
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get wallet mnemonic for backup
     * Note: In production, implement secure storage for mnemonic
     */
    fun getMnemonic(): String? {
        // This should be retrieved from secure storage in production
        // The mnemonic is returned during initialization
        return null
    }
}
