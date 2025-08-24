package com.example.geoblinker.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SubscriptionCheckWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d("SubscriptionCheckWorker", "🔄 Daily subscription check started")
            
            val prefs = applicationContext.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
            val maxEndDate = prefs.getLong("max_subscription_end_date", 0L)
            val currentTime = System.currentTimeMillis() / 1000
            
            val isActive = maxEndDate > currentTime
            
            Log.d("SubscriptionCheckWorker", "📅 Max end date: $maxEndDate, current: $currentTime, active: $isActive")
            
            // Сохраняем статус подписки
            prefs.edit().putBoolean("subscription_active", isActive).apply()
            
            if (!isActive && maxEndDate > 0) {
                Log.d("SubscriptionCheckWorker", "❌ Subscription expired - clearing data")
                // Подписка истекла, очищаем данные
                prefs.edit()
                    .remove("max_subscription_end_date")
                    .putBoolean("subscription_expired", true)
                    .apply()
            }
            
            Result.success()
        } catch (e: Exception) {
            Log.e("SubscriptionCheckWorker", "Error in daily subscription check", e)
            Result.retry()
        }
    }
}
