package com.example.geoblinker.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.R
import com.example.geoblinker.ui.main.viewmodel.Subscription
import com.example.geoblinker.network.SubscriptionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel(private val application: Application) : AndroidViewModel(application) {
    private val subscriptionRepository = SubscriptionRepository(application)
    private val prefs = application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

    private val _subscriptionOptions = MutableStateFlow<List<Subscription>>(emptyList())
    val subscriptionOptions: StateFlow<List<Subscription>> = _subscriptionOptions.asStateFlow()

    private val _pickSubscription = MutableStateFlow<Subscription>(Subscription(600, 1, "1 месяц", R.drawable.one_month))
    val pickSubscription: StateFlow<Subscription> = _pickSubscription.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _paymentSuccess = MutableStateFlow(false)
    val paymentSuccess: StateFlow<Boolean> = _paymentSuccess.asStateFlow()
    
    private val _subscriptionActive = MutableStateFlow(false)
    val subscriptionActive: StateFlow<Boolean> = _subscriptionActive.asStateFlow()

    private val _paymentUrl = MutableStateFlow<String?>(null)
    val paymentUrl: StateFlow<String?> = _paymentUrl.asStateFlow()

    init {
        // СНАЧАЛА проверяем флаг payment_success
        val paymentSuccessFlag = prefs.getBoolean("payment_success", false)
        val subscriptionActiveFlag = prefs.getBoolean("subscription_active", false)
        
        Log.d("SubscriptionViewModel", "🔍 Init: payment_success=$paymentSuccessFlag, subscription_active=$subscriptionActiveFlag")
        
        if (paymentSuccessFlag || subscriptionActiveFlag) {
            Log.d("SubscriptionViewModel", "✅ SUCCESS FLAGS FOUND - showing success")
            _paymentSuccess.value = true
            _subscriptionActive.value = true
        }
        
        // Проверяем статус подписки через сохраненную дату
        checkSubscriptionStatus()
        
        // Загружаем тарифы из БД
        loadTariffsFromDB()
    }

    fun checkPaymentStatus() {
        val paymentSuccess = prefs.getBoolean("payment_success", false)
        val subscriptionActive = prefs.getBoolean("subscription_active", false)
        
        Log.d("SubscriptionViewModel", "🔍 checkPaymentStatus: payment_success=$paymentSuccess, subscription_active=$subscriptionActive")
        
        if (paymentSuccess || subscriptionActive) {
            Log.d("SubscriptionViewModel", "✅ Setting UI to success state")
            _paymentSuccess.value = true
            _subscriptionActive.value = true
        }
        
        // ТАКЖЕ проверяем статус подписки
        checkSubscriptionStatus()
    }
    
    fun clearPaymentSuccess() {
        _paymentSuccess.value = false
        _subscriptionActive.value = false
        val editor = prefs.edit()
        editor.remove("payment_success")
        editor.remove("success_message")
        editor.apply()
    }

    fun refreshPaymentStatus() {
        // Принудительно обновляем все статусы
        val paymentSuccess = prefs.getBoolean("payment_success", false)
        val subscriptionActive = prefs.getBoolean("subscription_active", false)
        
        Log.d("SubscriptionViewModel", "🔄 refreshPaymentStatus: payment=$paymentSuccess, subscription=$subscriptionActive")
        
        _paymentSuccess.value = paymentSuccess || subscriptionActive
        _subscriptionActive.value = subscriptionActive
        
        checkPaymentStatus()
    }
    
    private fun checkPaymentStatusViaAPI(paymentId: String) {
        viewModelScope.launch {
            try {
                Log.d("SubscriptionViewModel", "Checking payment status via API for ID: $paymentId")
                val result = subscriptionRepository.getPaymentStatus(paymentId)
                
                if (result.isSuccess) {
                    val payment = result.getOrNull()
                    Log.d("SubscriptionViewModel", "Payment status from API: ${payment?.paymentStatus}")
                    
                    when (payment?.paymentStatus) {
                        6 -> { // PAYMENT_SUCCEEDED
                            Log.d("SubscriptionViewModel", "Payment confirmed as successful via API")
                            _paymentSuccess.value = true
                            // Очищаем payment_id так как оплата завершена
                            prefs.edit().remove("current_payment_id").apply()
                        }
                        3 -> { // PAYMENT_CANCELED
                            Log.d("SubscriptionViewModel", "Payment confirmed as canceled via API")
                            _errorMessage.value = "Оплата отменена"
                            prefs.edit().remove("current_payment_id").apply()
                        }
                        else -> {
                            Log.w("SubscriptionViewModel", "Payment status unclear: ${payment?.paymentStatus}")
                        }
                    }
                } else {
                    Log.e("SubscriptionViewModel", "Failed to check payment status: ${result.exceptionOrNull()}")
                }
            } catch (e: Exception) {
                Log.e("SubscriptionViewModel", "Error checking payment status via API", e)
            }
        }
    }
    
    /**
     * Проверяет статус подписки через сохраненную максимальную дату окончания
     */
    private fun checkSubscriptionStatus() {
        val maxEndDate = prefs.getLong("max_subscription_end_date", 0L)
        val currentTime = System.currentTimeMillis() / 1000
        
        val isActive = maxEndDate > currentTime
        Log.d("SubscriptionViewModel", "📅 Subscription check: maxEndDate=$maxEndDate, currentTime=$currentTime, active=$isActive")
        
        // Сохраняем статус в SharedPreferences для быстрого доступа
        prefs.edit().putBoolean("subscription_active", isActive).apply()
        
        // НЕМЕДЛЕННО обновляем UI если подписка активна
        if (isActive) {
            Log.d("SubscriptionViewModel", "🔄 Subscription is active - updating UI")
            _paymentSuccess.value = true
            _subscriptionActive.value = true
        } else {
            Log.d("SubscriptionViewModel", "❌ Subscription is NOT active")
            _paymentSuccess.value = false
            _subscriptionActive.value = false
        }
    }
    
    fun hasActiveSubscription(): Boolean {
        // BIRINCHI payment_success flagini tekshiramiz
        val paymentSuccess = prefs.getBoolean("payment_success", false)
        if (paymentSuccess) {
            Log.d("SubscriptionVM", "✅ hasActiveSubscription: payment_success=true")
            return true
        }
        
        // KEYIN subscription_active flagini tekshiramiz
        val subscriptionActive = prefs.getBoolean("subscription_active", false)
        if (subscriptionActive) {
            Log.d("SubscriptionVM", "✅ hasActiveSubscription: subscription_active=true")
            return true
        }
        
        // NIHOYAT max_subscription_end_date ni tekshiramiz
        val maxEndDate = prefs.getLong("max_subscription_end_date", 0L)
        val currentTime = System.currentTimeMillis() / 1000
        val isActive = maxEndDate > currentTime
        
        Log.d("SubscriptionVM", "🔍 hasActiveSubscription: maxEndDate=$maxEndDate, currentTime=$currentTime, active=$isActive")
        
        return isActive
    }
    
    fun getSubscriptionEndDate(): Long {
        return prefs.getLong("max_subscription_end_date", 0L)
    }
    
    private fun loadTariffsFromDB() {
        viewModelScope.launch {
            try {
                Log.d("SubscriptionViewModel", "Loading tariffs from database...")
                val result = subscriptionRepository.getTariffs()
                
                if (result.isSuccess) {
                    val tariffs = result.getOrNull() ?: emptyMap()
                    Log.d("SubscriptionViewModel", "Loaded ${tariffs.size} tariffs from DB")
                    
                    // Преобразуем тарифы в опции подписки
                    val options = tariffs.map { (id, tariff) ->
                        val drawable = when (id.toString()) {
                            "1" -> R.drawable.one_month    // 10 USD - месяц
                            "2" -> R.drawable.three_months // 4 USD - день  
                            "3" -> R.drawable.six_months   // 1 USD - час
                            "4" -> R.drawable.twelve_months // 5 USD - месяц
                            "5" -> R.drawable.one_month    // 2 USD - день
                            "6" -> R.drawable.three_months // 0.5 USD - час
                            else -> R.drawable.one_month
                        }
                        
                        // Определяем период и название на основе name_ru из БД
                        val periodName = when (id.toString()) {
                            "1" -> "Год на месяц"
                            "2" -> "Год на день" 
                            "3" -> "Год на час"
                            "4" -> "Сильвер на месяц"
                            "5" -> "Сильвер на день"
                            "6" -> "Сильвер на час"
                            else -> "Подписка"
                        }
                        
                        // Определяем период для расчетов
                        val periodDays = when (id.toString()) {
                            "1", "4" -> 30  // месяц
                            "2", "5" -> 1   // день
                            "3", "6" -> 1   // час (считаем как 1 день для простоты)
                            else -> 30
                        }
                        
                        // Конвертируем USD в рубли (примерно 1 USD = 90 RUB)
                        val priceInRub = (tariff.price * 90).toInt()
                        
                        Subscription(
                            price = priceInRub,
                            period = periodDays, // используем дни как период
                            labelPeriod = periodName,
                            draw = drawable
                        )
                    }.sortedBy { it.price }
                    
                    _subscriptionOptions.value = options
                    
                    // Устанавливаем первый тариф как выбранный по умолчанию
                    if (options.isNotEmpty()) {
                        _pickSubscription.value = options.first()
                    }
                    
                } else {
                    Log.e("SubscriptionViewModel", "Failed to load tariffs: ${result.exceptionOrNull()}")
                    _errorMessage.value = "Ошибка загрузки тарифов"
                }
            } catch (e: Exception) {
                Log.e("SubscriptionViewModel", "Error loading tariffs", e)
                _errorMessage.value = "Ошибка загрузки тарифов"
            }
        }
    }

    fun setPickSubscription(index: Int) {
        val options = _subscriptionOptions.value
        if (index < options.size) {
            _pickSubscription.value = options[index]
        }
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
                    // Сохраняем payment ID для проверки статуса
                    paymentData.pId?.let { pId ->
                        prefs.edit().putString("current_payment_id", pId).apply()
                    }
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
        // Возвращаем к рабочему варианту
        return "{\"succeeded\":\"geoblinker://payment/success\",\"canceled\":\"geoblinker://payment/canceled\"}"
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