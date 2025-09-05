package com.example.geoblinker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.geoblinker.ui.theme.GeoBlinkerTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        // Включение Crashlytics (можно отключить для debug)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true

        // Временное отключение всех флагов
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)

        TimeUtils.init(this)
        
        // Handle payment deep links
        handlePaymentDeepLink(intent)
        
        enableEdgeToEdge()
        setContent {
            GeoBlinkerTheme {
                GeoBlinkerScreen()
            }
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("MainActivity", "🆕 onNewIntent called with: ${intent.dataString}")
        setIntent(intent)
        // Сразу обрабатываем новый intent
        handlePaymentDeepLink(intent)
    }
    
    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
        
        // Проверяем deep link
        handlePaymentDeepLink(intent)
        
        // DARHOL ProfileViewModel ni yangilaymiz
        val prefs = getSharedPreferences("profile_prefs", MODE_PRIVATE)
        val maxEndDate = prefs.getLong("max_subscription_end_date", 0)
        if (maxEndDate > 0) {
            Log.d("MainActivity", "🔄 Found active subscription, updating ProfileViewModel")
            // ProfileViewModel ni yangilash kerak, lekin u yerda access yo'q
            // Shuning uchun subscription qiymatini to'g'ridan-to'g'ri yangilaymiz
            val subscriptionEndDateMs = maxEndDate * 1000
            prefs.edit().putLong("subscription", subscriptionEndDateMs).apply()
            Log.d("MainActivity", "✅ Updated subscription in prefs: $subscriptionEndDateMs")
        }
        
        // МГНОВЕННАЯ проверка оплаты при входе в приложение
        val paymentId = prefs.getString("current_payment_id", null)
        if (paymentId != null) {
            Log.d("MainActivity", "⚡ INSTANT payment check for ID: $paymentId")
            checkPaymentInstantly(paymentId)
        }
    }
    
    private fun checkPaymentInstantly(paymentId: String) {
        lifecycleScope.launch {
            try {
                val repository = com.example.geoblinker.network.SubscriptionRepository(application)
                val result = repository.getPaymentStatus(paymentId)
                
                val prefs = getSharedPreferences("profile_prefs", MODE_PRIVATE)
                
                if (result.isSuccess) {
                    val payment = result.getOrNull()
                    when (payment?.paymentStatus) {
                        6 -> {
                            Log.d("MainActivity", "✅ PAYMENT SUCCESS - Creating subscription")
                            
                            // СНАЧАЛА создаем подписку в базе данных
                            lifecycleScope.launch {
                                try {
                                       val selectedTariffId = prefs.getInt("selected_tariff_id", 1)
                                    
                                    Log.d("MainActivity", "🏗️ Creating subscription with tariff ID: $selectedTariffId")
                                    val subscriptionResult = repository.createSubscription(selectedTariffId.toString())
                                    
                                    if (subscriptionResult.isSuccess) {
                                        val subscriptionId = subscriptionResult.getOrNull()
                                        Log.d("MainActivity", "✅ Subscription created in DB with ID: $subscriptionId")
                                        
                                        // ТЕПЕРЬ устанавливаем все флаги
                                        val editor = prefs.edit()
                                        editor.putBoolean("payment_success", true)
                                        editor.putString("success_message", "Успешно оплачено! Подписка активирована")
                                        editor.putBoolean("subscription_active", true)
                                        
                                        // Tanlangan tarifga qarab podpiska davomini hisoblaymiz
                                        val currentTime = System.currentTimeMillis() / 1000
                                        val selectedTariffId = prefs.getInt("selected_tariff_id", 1)
                                        
                                        Log.d("MainActivity", "🔍 BEFORE CALCULATION: selectedTariffId = $selectedTariffId")
                                        // todo - тут захардкожены тарифы, их надо брать с сервера и там брать период
                                        // Tarif ID ga qarab davomni belgilaymiz (ANIQ VAQTLAR)
                                        val durationInSeconds = when (selectedTariffId) {
                                            1 -> {
                                                Log.d("MainActivity", "⏰ Tariff 1: 30 days")
                                                30 * 24 * 60 * 60L    // "Год на месяц" - 30 kun
                                            }
                                            2 -> {
                                                Log.d("MainActivity", "⏰ Tariff 2: 1 day")
                                                24 * 60 * 60L         // "Год на день" - 24 soat (1 kun)
                                            }
                                            3 -> {
                                                Log.d("MainActivity", "⏰ Tariff 3: 1 HOUR")
                                                60 * 60L              // "Год на час" - 60 minut (1 soat)
                                            }
                                            4 -> {
                                                Log.d("MainActivity", "⏰ Tariff 4: 30 days")
                                                30 * 24 * 60 * 60L    // "Сильвер на месяц" - 30 kun
                                            }
                                            5 -> {
                                                Log.d("MainActivity", "⏰ Tariff 5: 1 day")
                                                24 * 60 * 60L         // "Сильвер на день" - 24 soat (1 kun)
                                            }
                                            6 -> {
                                                Log.d("MainActivity", "⏰ Tariff 6: 1 HOUR")
                                                60 * 60L              // "Сильвер на час" - 60 minut (1 soat)
                                            }
                                            else -> {
                                                Log.d("MainActivity", "⏰ Default: 30 days")
                                                30 * 24 * 60 * 60L // Default: 30 kun
                                            }
                                        }
                                        
                                        val endDate = currentTime + durationInSeconds
                                        Log.d("MainActivity", "📅 FINAL: Tariff ID: $selectedTariffId, Duration: ${durationInSeconds}s (${durationInSeconds/3600} hours), End date: $endDate")
                                        editor.putLong("max_subscription_end_date", endDate)
                                        
                                        // MUHIM: ProfileViewModel uchun ham subscription ni yangilaymiz
                                        val subscriptionEndDateMs = endDate * 1000
                                        editor.putLong("subscription", subscriptionEndDateMs)
                                        
                                        editor.apply()
                                        
                                        Log.d("MainActivity", "🎯 SUBSCRIPTION FULLY ACTIVATED: ID=$subscriptionId, end_date=$endDate")
                                        
                                        // Также вычисляем реальную дату из API
                                        calculateAndSaveMaxSubscriptionDate()
                                        
                                        // YANGI: ProfileViewModel ni ham yangilaymiz
                                        // Bu popup ni yashirish uchun kerak
                                    } else {
                                        Log.e("MainActivity", "❌ Failed to create subscription in DB")
                                        prefs.edit().putString("error_message", "Ошибка создания подписки").apply()
                                    }
                                } catch (e: Exception) {
                                    Log.e("MainActivity", "💥 Error creating subscription", e)
                                    prefs.edit().putString("error_message", "Ошибка активации подписки").apply()
                                }
                            }
                        }
                        3 -> {
                            Log.d("MainActivity", "❌ PAYMENT CANCELED")
                            prefs.edit().putBoolean("payment_success", false).apply()
                            prefs.edit().putString("error_message", "Оплата отменена. Попробуйте еще раз").apply()
                        }
                        else -> {
                            Log.d("MainActivity", "⏳ PAYMENT PENDING")
                            return@launch // Не очищаем payment_id, проверим позже
                        }
                    }
                } else {
                    Log.d("MainActivity", "❌ PAYMENT CHECK FAILED")
                    prefs.edit().putString("error_message", "Ошибка проверки оплаты. Попробуйте еще раз").apply()
                }
                
                // Очищаем payment_id только после обработки
                prefs.edit().remove("current_payment_id").apply()
                
            } catch (e: Exception) {
                Log.e("MainActivity", "Error checking payment instantly", e)
            }
        }
    }
    
    private suspend fun calculateAndSaveMaxSubscriptionDate() {
        try {
            val repository = com.example.geoblinker.network.SubscriptionRepository(application)
            val subscriptionsResult = repository.getUserSubscriptions()
            val tariffsResult = repository.getTariffs()
            
            if (subscriptionsResult.isSuccess && tariffsResult.isSuccess) {
                val subscriptions = subscriptionsResult.getOrNull() ?: emptyList()
                val tariffs = tariffsResult.getOrNull() ?: emptyMap()
                
                var maxEndDate = 0L
                
                // Находим максимальную дату окончания всех активных подписок
                subscriptions.forEach { subscription ->
                    if (subscription.subsStatus == "1" && subscription.paid == true) {
                        val tariff = tariffs[subscription.tariff]
                        if (tariff != null) {

                            val durationClass = tariff["duration_class"].toString()

                            val durationInSeconds = when (durationClass) {
                                "1"-> 30L * 24 * 3600    // 30 дней
                                "2"-> 1L * 24 * 3600     // 1 день
                                "3"-> 3600L     // 1 час
                                "4"-> 90L * 24 * 3600    // 90 дней
                                "5"-> 180L * 24 * 3600   // 180 дней
                                "6"-> 365L* 24 * 3600   // 365 дней
                                else -> 0
                            }
                            //val durationInSeconds = tariff["period"].toString().toInt()  * 30 * 24 * 60 * 60L
                            val endDate = subscription.startDate + durationInSeconds
                            
                            if (endDate > maxEndDate) {
                                maxEndDate = endDate
                            }
                        }
                    }
                }
                
                // Сохраняем максимальную дату окончания
                val prefs = getSharedPreferences("profile_prefs", MODE_PRIVATE)
                prefs.edit().putLong("max_subscription_end_date", maxEndDate).apply()
                
                // НЕМЕДЛЕННО устанавливаем статус активной подписки
                val currentTime = System.currentTimeMillis() / 1000
                val isActive = maxEndDate > currentTime
                prefs.edit().putBoolean("subscription_active", isActive).apply()
                
                Log.d("MainActivity", "📅 Max subscription end date saved: $maxEndDate, Active: $isActive")
                
                // Запускаем ежедневную проверку
                startDailySubscriptionCheck()
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error calculating max subscription date", e)
        }
    }
    
    private fun startDailySubscriptionCheck() {
        // Используем WorkManager для ежедневной проверки
        val workRequest = androidx.work.PeriodicWorkRequestBuilder<com.example.geoblinker.workers.SubscriptionCheckWorker>(
            24, java.util.concurrent.TimeUnit.HOURS
        ).build()
        
        androidx.work.WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_subscription_check",
            androidx.work.ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
        
        Log.d("MainActivity", "🔄 Daily subscription check started")
    }
    
    private fun handlePaymentDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data
        Log.d("MainActivity", "🔍 Checking intent data: ${intent?.dataString}")
        
        if (data != null) {
            Log.d("MainActivity", "📱 Intent scheme: ${data.scheme}, host: ${data.host}, path: ${data.path}")
            
            if (data.scheme == "geoblinker" && data.host == "payment") {
                Log.d("PaymentCallback", "🎯 PAYMENT DEEP LINK DETECTED: ${data}")
                val prefs = getSharedPreferences("profile_prefs", MODE_PRIVATE)
                
                when (data.path) {
                    "/success" -> {
                        Log.d("PaymentCallback", "✅ SUCCESS CALLBACK - Setting all flags")
                        
                        // СРАЗУ устанавливаем все флаги одним блоком
                        val editor = prefs.edit()
                        editor.putBoolean("payment_success", true)
                        editor.putBoolean("subscription_active", true)
                        editor.putString("success_message", "Успешно оплачено! Подписка активирована")
                        
                        // Tanlangan tarifga qarab podpiska davomini hisoblaymiz
                        val currentTime = System.currentTimeMillis() / 1000
                        val selectedTariffId = prefs.getInt("selected_tariff_id", 1)
                        
                        // Tarif ID ga qarab davomni belgilaymiz (ANIQ VAQTLAR)
                        val durationInSeconds = when (selectedTariffId) {
                            1 -> 30 * 24 * 60 * 60L    // "Год на месяц" - 30 kun
                            2 -> 24 * 60 * 60L         // "Год на день" - 24 soat (1 kun)  
                            3 -> 60 * 60L              // "Год на час" - 60 minut (1 soat)
                            4 -> 30 * 24 * 60 * 60L    // "Сильвер на месяц" - 30 kun
                            5 -> 24 * 60 * 60L         // "Сильвер на день" - 24 soat (1 kun)
                            6 -> 60 * 60L              // "Сильвер на час" - 60 minut (1 soat)
                            else -> 30 * 24 * 60 * 60L // Default: 30 kun
                        }
                        
                        val endDate = currentTime + durationInSeconds
                        Log.d("PaymentCallback", "📅 Deep link - Tariff ID: $selectedTariffId, Duration: ${durationInSeconds}s")
                        editor.putLong("max_subscription_end_date", endDate)
                        
                        // MUHIM: ProfileViewModel uchun ham subscription ni yangilaymiz
                        val subscriptionEndDateMs = endDate * 1000
                        editor.putLong("subscription", subscriptionEndDateMs)
                        
                        editor.remove("current_payment_id")
                        editor.apply()
                        
                        Log.d("PaymentCallback", "🎯 SUCCESS FLAGS SET: payment_success=true, subscription_active=true, end_date=$endDate")
                        
                        // Вычисляем реальную дату из API
                        lifecycleScope.launch {
                            calculateAndSaveMaxSubscriptionDate()
                        }
                        
                        // Очищаем intent
                        setIntent(Intent())
                    }
                    "/canceled" -> {
                        Log.d("PaymentCallback", "❌ CANCELED CALLBACK")
                        prefs.edit().putBoolean("payment_success", false).apply()
                        prefs.edit().remove("current_payment_id").apply()
                        
                        // Очищаем intent
                        setIntent(Intent())
                    }
                }
            } else {
                Log.d("MainActivity", "❌ Not a payment deep link")
            }
        } else {
            Log.d("MainActivity", "❌ No intent data found")
        }
    }
    
    private fun checkPaymentStatusViaAPI(paymentId: String) {
        lifecycleScope.launch {
            try {
                Log.d("MainActivity", "Checking payment status for ID: $paymentId")
                val repository = com.example.geoblinker.network.SubscriptionRepository(application)
                val result = repository.getPaymentStatus(paymentId)
                
                val prefs = getSharedPreferences("profile_prefs", MODE_PRIVATE)
                
                if (result.isSuccess) {
                    val payment = result.getOrNull()
                    Log.d("MainActivity", "Payment status from API: ${payment?.paymentStatus}")
                    
                    when (payment?.paymentStatus) {
                        6 -> { // PAYMENT_SUCCEEDED
                            Log.d("MainActivity", "✅ Payment SUCCESS confirmed!")
                            prefs.edit().putBoolean("payment_success", true).apply()
                            prefs.edit().remove("current_payment_id").apply()
                        }
                        3 -> { // PAYMENT_CANCELED  
                            Log.d("MainActivity", "❌ Payment CANCELED")
                            prefs.edit().remove("current_payment_id").apply()
                        }
                        else -> {
                            Log.d("MainActivity", "⏳ Payment status: ${payment?.paymentStatus}")
                        }
                    }
                } else {
                    Log.e("MainActivity", "Failed to get payment status: ${result.exceptionOrNull()}")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error checking payment status", e)
            }
        }
    }
}