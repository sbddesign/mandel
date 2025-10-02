package com.example.mandel.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mandel.data.repository.CashuWalletRepository
import com.example.mandel.domain.model.MeltState
import com.example.mandel.domain.model.MintState
import com.example.mandel.domain.model.TokenTransferState
import com.example.mandel.domain.model.WalletState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.cashudevkit.Token

/**
 * ViewModel for managing wallet state and operations
 * Coordinates between UI and repository layer
 */
class WalletViewModel(
  private val repository: CashuWalletRepository = CashuWalletRepository()
) : ViewModel() {

  private val _walletState = MutableStateFlow(WalletState())
  val walletState: StateFlow<WalletState> = _walletState.asStateFlow()

  private val _mintState = MutableStateFlow(MintState())
  val mintState: StateFlow<MintState> = _mintState.asStateFlow()

  private val _meltState = MutableStateFlow(MeltState())
  val meltState: StateFlow<MeltState> = _meltState.asStateFlow()

  private val _sendState = MutableStateFlow(TokenTransferState())
  val sendState: StateFlow<TokenTransferState> = _sendState.asStateFlow()

  private val _receiveState = MutableStateFlow(TokenTransferState())
  val receiveState: StateFlow<TokenTransferState> = _receiveState.asStateFlow()

  init {
    // Observe repository state changes
    viewModelScope.launch {
      repository.balance.collect { balance ->
        _walletState.update { it.copy(balance = balance) }
      }
    }

    viewModelScope.launch {
      repository.isInitialized.collect { initialized ->
        _walletState.update { it.copy(isInitialized = initialized) }
      }
    }
  }

  /**
   * Initialize the wallet
   * @param mnemonic Optional mnemonic for wallet recovery
   */
  fun initializeWallet(mnemonic: String? = null) {
    viewModelScope.launch {
      _walletState.update { it.copy(isLoading = true, error = null) }

      repository.initializeWallet(mnemonic = mnemonic)
        .onSuccess { generatedMnemonic ->
          _walletState.update {
            it.copy(
              isLoading = false,
              isInitialized = true,
              mnemonic = generatedMnemonic
            )
          }
        }
        .onFailure { error ->
          _walletState.update {
            it.copy(
              isLoading = false,
              error = error.message ?: "Failed to initialize wallet"
            )
          }
        }
    }
  }

  /**
   * Create a mint quote to receive funds
   * @param amount Amount in sats
   */
  fun createMintQuote(amount: ULong, description: String = "") {
    viewModelScope.launch {
      _mintState.update { it.copy(isProcessing = true, error = null, amount = amount) }

      repository.createMintQuote(amount, description)
        .onSuccess { quote ->
          _mintState.update {
            it.copy(
              isProcessing = false,
              quoteId = quote.id,
              paymentRequest = quote.request
            )
          }
        }
        .onFailure { error ->
          _mintState.update {
            it.copy(
              isProcessing = false,
              error = error.message ?: "Failed to create mint quote"
            )
          }
        }
    }
  }

  /**
   * Mint tokens after payment is confirmed
   */
  fun mintTokens() {
    val quoteId = _mintState.value.quoteId ?: return

    viewModelScope.launch {
      _mintState.update { it.copy(isProcessing = true, error = null) }

      repository.mintTokens(quoteId)
        .onSuccess { amount ->
          _mintState.update {
            it.copy(
              isProcessing = false,
              isCompleted = true
            )
          }
          refreshBalance()
        }
        .onFailure { error ->
          _mintState.update {
            it.copy(
              isProcessing = false,
              error = error.message ?: "Failed to mint tokens"
            )
          }
        }
    }
  }

  /**
   * Create a melt quote to send payment via Lightning
   * @param invoice Lightning invoice
   */
  fun createMeltQuote(invoice: String) {
    viewModelScope.launch {
      _meltState.update { it.copy(isProcessing = true, error = null, invoice = invoice) }

      repository.createMeltQuote(invoice)
        .onSuccess { quote ->
          _meltState.update {
            it.copy(
              isProcessing = false,
              quoteId = quote.id,
              amount = quote.amount.value,
              fee = quote.feeReserve.value
            )
          }
        }
        .onFailure { error ->
          _meltState.update {
            it.copy(
              isProcessing = false,
              error = error.message ?: "Failed to create melt quote"
            )
          }
        }
    }
  }

  /**
   * Execute melt to pay Lightning invoice
   */
  fun meltTokens() {
    val quoteId = _meltState.value.quoteId ?: return

    viewModelScope.launch {
      _meltState.update { it.copy(isProcessing = true, error = null) }

      repository.meltTokens(quoteId)
        .onSuccess { response ->
          _meltState.update {
            it.copy(
              isProcessing = false,
              isCompleted = true,
              isPaid = it.isPaid
            )
          }
          refreshBalance()
        }
        .onFailure { error ->
          _meltState.update {
            it.copy(
              isProcessing = false,
              error = error.message ?: "Failed to melt tokens"
            )
          }
        }
    }
  }

  /**
   * Send tokens to another Cashu wallet
   * @param amount Amount in sats
   */
  fun sendTokens(amount: ULong) {
    viewModelScope.launch {
      _sendState.update { it.copy(isProcessing = true, error = null, amount = amount) }

      repository.sendTokens(amount)
        .onSuccess { token ->
          _sendState.update {
            it.copy(
              isProcessing = false,
              isCompleted = true,
              token = token
            )
          }
          refreshBalance()
        }
        .onFailure { error ->
          _sendState.update {
            it.copy(
              isProcessing = false,
              error = error.message ?: "Failed to send tokens"
            )
          }
        }
    }
  }

  /**
   * Receive tokens from another Cashu wallet
   * @param token Encoded token string
   */
  fun receiveTokens(token: String) {
    viewModelScope.launch {
      _receiveState.update { it.copy(isProcessing = true, error = null, token = token) }

      repository.receiveTokens(TODO())
        .onSuccess { amount ->
          _receiveState.update {
            it.copy(
              isProcessing = false,
              isCompleted = true,
              amount = amount.value
            )
          }
          refreshBalance()
        }
        .onFailure { error ->
          _receiveState.update {
            it.copy(
              isProcessing = false,
              error = error.message ?: "Failed to receive tokens"
            )
          }
        }
    }
  }

  /**
   * Refresh wallet balance
   */
  fun refreshBalance() {
    viewModelScope.launch {
      repository.refreshBalance()
    }
  }

  /**
   * Reset mint state
   */
  fun resetMintState() {
    _mintState.value = MintState()
  }

  /**
   * Reset melt state
   */
  fun resetMeltState() {
    _meltState.value = MeltState()
  }

  /**
   * Reset send state
   */
  fun resetSendState() {
    _sendState.value = TokenTransferState()
  }

  /**
   * Reset receive state
   */
  fun resetReceiveState() {
    _receiveState.value = TokenTransferState()
  }

  override fun onCleared() {
    super.onCleared()
    viewModelScope.launch {
      repository.closeWallet()
    }
  }
}