package org.example.project.data.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.data.model.*

/**
 * Repository interface for Cashu wallet operations
 * This provides a clean API for all wallet-related functionality
 */
interface CashuWalletRepository {

    /**
     * Observes the current wallet state including balance and active mints
     */
    val walletState: Flow<WalletState>

    /**
     * Initializes the wallet with a mint URL
     * @param mintUrl The URL of the Cashu mint to connect to
     * @return Result indicating success or failure
     */
    suspend fun initializeWallet(mintUrl: String): WalletResult<Unit>

    /**
     * Gets the current balance of the wallet
     * @return The balance in satoshis
     */
    suspend fun getBalance(): WalletResult<Long>

    /**
     * Requests a mint quote for creating new ecash tokens
     * @param amount Amount in satoshis to mint
     * @return Quote information including payment request
     */
    suspend fun requestMintQuote(amount: Long): WalletResult<String>

    /**
     * Mints new tokens after payment has been made
     * @param quoteId The quote ID from requestMintQuote
     * @return Result with minted tokens
     */
    suspend fun mintTokens(quoteId: String): WalletResult<CashuToken>

    /**
     * Sends tokens to another user
     * @param amount Amount in satoshis to send
     * @param memo Optional memo for the transaction
     * @return Encoded token string to share with recipient
     */
    suspend fun sendTokens(amount: Long, memo: String? = null): WalletResult<String>

    /**
     * Receives tokens from an encoded token string
     * @param encodedToken The token string to receive
     * @return Amount received in satoshis
     */
    suspend fun receiveTokens(encodedToken: String): WalletResult<Long>

    /**
     * Requests a melt quote for converting tokens back to Lightning
     * @param amount Amount in satoshis to melt
     * @param lightningInvoice Lightning invoice to pay
     * @return Quote information
     */
    suspend fun requestMeltQuote(
        amount: Long,
        lightningInvoice: String
    ): WalletResult<String>

    /**
     * Melts tokens by paying a Lightning invoice
     * @param quoteId The quote ID from requestMeltQuote
     * @return Result indicating success or failure
     */
    suspend fun meltTokens(quoteId: String): WalletResult<Unit>

    /**
     * Gets transaction history
     * @return List of past transactions
     */
    suspend fun getTransactionHistory(): WalletResult<List<Transaction>>

    /**
     * Adds a new mint to the wallet
     * @param mintUrl URL of the mint to add
     * @return Result with mint information
     */
    suspend fun addMint(mintUrl: String): WalletResult<MintInfo>

    /**
     * Removes a mint from the wallet
     * @param mintUrl URL of the mint to remove
     * @return Result indicating success or failure
     */
    suspend fun removeMint(mintUrl: String): WalletResult<Unit>

    /**
     * Gets information about active mints
     * @return List of active mints
     */
    suspend fun getActiveMints(): WalletResult<List<MintInfo>>

    /**
     * Backs up wallet data
     * @return Backup data as a string
     */
    suspend fun backupWallet(): WalletResult<String>

    /**
     * Restores wallet from backup data
     * @param backupData The backup data string
     * @return Result indicating success or failure
     */
    suspend fun restoreWallet(backupData: String): WalletResult<Unit>

    /**
     * Checks the status of pending proofs
     * @return Result indicating any state changes
     */
    suspend fun checkPendingProofs(): WalletResult<Unit>
}