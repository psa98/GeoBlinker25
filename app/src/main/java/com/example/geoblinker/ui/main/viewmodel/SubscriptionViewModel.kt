package com.example.geoblinker.ui.main.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.R
import com.example.geoblinker.network.SubscriptionRepository
import com.example.geoblinker.ui.main.viewmodel.Subscription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel(application: Application): AndroidViewModel(application) {
    private val subscriptionRepository = SubscriptionRepository(application)
    
    val subscriptionOptions = listOf(
        Subscription(600, 1, "1 месяц", R.drawable.one_month),
        Subscription(1100, 3, "3 месяца", R.drawable.three_months),
        Subscription(1800, 6, "6 месяцев", R.drawable.six_months),
        Subscription(2800, 12, "12 месяцев", R.drawable.twelve_months)
    )

    private val _pickSubscription = MutableStateFlow(subscriptionOptions[0])
    val pickSubscription: StateFlow<Subscription> = _pickSubscription.asStateFlow()

    private val _paymentUrl = MutableStateFlow<String?>(null)
    val paymentUrl: StateFlow<String?> = _paymentUrl.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _paymentSuccess = MutableStateFlow(false)
    val paymentSuccess: StateFlow<Boolean> = _paymentSuccess.asStateFlow()

    fun setPickSubscription(index: Int) {
        _pickSubscription.value = subscriptionOptions[index]
    }

    /**
     * Создает подписку и инициирует оплату через YuKassa
     */
    fun paySubscription() {
        Log.d("SubscriptionViewModel", "paySubscription() called")
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val selectedSubscription = _pickSubscription.value
                Log.d("SubscriptionViewModel", "Selected subscription: $selectedSubscription")
                
                // Получаем тарифы с сервера
                Log.d("SubscriptionViewModel", "Getting tariffs from server...")
                val tariffsResult = subscriptionRepository.getTariffs()
                if (tariffsResult.isFailure) {
                    Log.e("SubscriptionViewModel", "Failed to get tariffs: ${tariffsResult.exceptionOrNull()}")
                    _errorMessage.value = "Ошибка получения тарифов"
                    _isLoading.value = false
                    return@launch
                }
                
                val tariffs = tariffsResult.getOrNull()
                Log.d("SubscriptionViewModel", "Tariffs loaded: ${tariffs?.size} items")
                val tariffId = findTariffIdByPrice(tariffs, selectedSubscription.price)
                Log.d("SubscriptionViewModel", "Found tariff ID: $tariffId for price: ${selectedSubscription.price}")
                
                if (tariffId == null) {
                    Log.e("SubscriptionViewModel", "Tariff not found for price: ${selectedSubscription.price}")
                    _errorMessage.value = "Тариф не найден"
                    _isLoading.value = false
                    return@launch
                }
                
                // Создаем подписку
                Log.d("SubscriptionViewModel", "Creating subscription with tariff ID: $tariffId")
                val subscriptionResult = subscriptionRepository.createSubscription(tariffId)
                if (subscriptionResult.isFailure) {
                    Log.e("SubscriptionViewModel", "Failed to create subscription: ${subscriptionResult.exceptionOrNull()}")
                    _errorMessage.value = "Ошибка создания подписки"
                    _isLoading.value = false
                    return@launch
                }
                
                val subsId = subscriptionResult.getOrNull()
                Log.d("SubscriptionViewModel", "Subscription created with ID: $subsId")
                
                // Создаем платеж
                Log.d("SubscriptionViewModel", "Creating payment for amount: ${selectedSubscription.price}")
                val paymentResult = subscriptionRepository.createPayment(
                    amount = selectedSubscription.price,
                    subsId = subsId,
                    appUrl = createAppUrl()
                )
                
                if (paymentResult.isFailure) {
                    Log.e("SubscriptionViewModel", "Failed to create payment: ${paymentResult.exceptionOrNull()}")
                    _errorMessage.value = "Ошибка создания платежа"
                    _isLoading.value = false
                    return@launch
                }
                
                val paymentData = paymentResult.getOrNull()
                Log.d("SubscriptionViewModel", "Payment created: ${paymentData?.pId}")
                Log.d("SubscriptionViewModel", "Opening payment URL: ${paymentData?.confirmationUrl}")
                
                // Открываем URL для оплаты
                paymentData?.confirmationUrl?.let { url ->
                    openPaymentUrl(url)
                    _paymentSuccess.value = true
                }
                
            } catch (e: Exception) {
                Log.e("SubscriptionViewModel", "Payment error", e)
                _errorMessage.value = "Произошла ошибка: ${e.message}"
                _paymentSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun findTariffIdByPrice(tariffs: Map<String, com.example.geoblinker.model.TariffItem>?, price: Int): String? {
        val selectedSubscription = _pickSubscription.value
        Log.d("SubscriptionViewModel", "Looking for tariff with price: $price, period: ${selectedSubscription.period}")
        tariffs?.forEach { (key, tariff) ->
            Log.d("SubscriptionViewModel", "Tariff $key: name=${tariff.name}, price=${tariff.price}, period=${tariff.period}")
        }
        
        // Since API tariffs all have period=0, use mapping based on subscription period
        val tariffId = when (selectedSubscription.period) {
            1 -> "6"    // 1 month -> cheapest tariff (0.5)
            3 -> "5"    // 3 months -> tariff 5 (2.0)
            6 -> "2"    // 6 months -> tariff 2 (4.0)
            12 -> "1"   // 12 months -> tariff 1 (10.0)
            else -> tariffs?.keys?.firstOrNull()
        }
        
        Log.d("SubscriptionViewModel", "Mapped period ${selectedSubscription.period} to tariff ID: $tariffId")
        return tariffId
    }

    private fun createAppUrl(): String {
        return """{"succeeded":"geoblinker://payment/success","canceled":"geoblinker://payment/canceled"}"""
    }

    private fun openPaymentUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            getApplication<Application>().startActivity(intent)
        } catch (e: Exception) {
            Log.e("SubscriptionViewModel", "Error opening payment URL", e)
            _errorMessage.value = "Не удалось открыть страницу оплаты"
        }
    }

    fun checkPaymentStatus(paymentId: String) {
        viewModelScope.launch {
            try {
                val result = subscriptionRepository.getPaymentStatus(paymentId)
                if (result.isSuccess) {
                    val payment = result.getOrNull()
                    when (payment?.paymentStatus) {
                        SubscriptionRepository.PAYMENT_SUCCEEDED -> {
                            _paymentSuccess.value = true
                        }
                        SubscriptionRepository.PAYMENT_CANCELED -> {
                            _errorMessage.value = "Оплата отменена"
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SubscriptionViewModel", "Error checking payment status", e)
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearPaymentUrl() {
        _paymentUrl.value = null
    }
}