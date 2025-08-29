package com.example.geoblinker.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.InvalidationTracker
import com.example.geoblinker.model.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Objects

class SubscriptionRepository(private val context: Context) {
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    suspend fun createSubscription(tariffId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val token = prefs.getString("token", "") ?: ""
            val uHash = prefs.getString("hash", "") ?: ""
            
            if (token.isEmpty() || uHash.isEmpty()) {
                return@withContext Result.failure(Exception("No authentication tokens"))
            }

            val subscriptionData = SubscriptionData(
                tariff = tariffId,
                autoRenew = 1
            )

            val request = mapOf(
                "token" to token,
                "u_hash" to uHash,
                "data" to gson.toJson(subscriptionData)
            )

            val response = Api.retrofitService.createSubscription(request)
            
            if (response.code == "200") {
                Log.d("SubscriptionRepo", "Subscription created: ${response.data.subsId}")
                Result.success(response.data.subsId)
            } else {
                Result.failure(Exception("Failed to create subscription: ${response.code}"))
            }
        } catch (e: Exception) {
            Log.e("SubscriptionRepo", "Error creating subscription", e)
            Result.failure(e)
        }
    }

    suspend fun createPayment(
        amount: Int, 
        subsId: String? = null,
        appUrl: String? = null
    ): Result<PaymentResponseData> = withContext(Dispatchers.IO) {
        try {
            val token = prefs.getString("token", "") ?: ""
            val uHash = prefs.getString("hash", "") ?: ""
            
            if (token.isEmpty() || uHash.isEmpty()) {
                return@withContext Result.failure(Exception("No authentication tokens"))
            }

            val paymentData = PaymentData(
                sum = amount,
                currency = "RUB",
                paymentService = 1,
                subsId = subsId,
                paymentWay = 2
            )

            val requestMap = mutableMapOf(
                "token" to token,
                "u_hash" to uHash,
                "data" to gson.toJson(paymentData)
            )
            
            appUrl?.let { 
                Log.d("SubscriptionRepo", "Adding appUrl to payment request: $it")
                requestMap["appUrl"] = it 
            }

            val response = Api.retrofitService.createPayment(requestMap)
            
            if (response.code == "200") {
                Log.d("SubscriptionRepo", "Payment created: ${response.data.pId}")
                Result.success(response.data)
            } else {
                Result.failure(Exception("Failed to create payment: ${response.code}"))
            }
        } catch (e: Exception) {
            Log.e("SubscriptionRepo", "Error creating payment", e)
            Result.failure(e)
        }
    }

    suspend fun getPaymentStatus(paymentId: String): Result<PaymentInfo> = withContext(Dispatchers.IO) {
        try {
            val token = prefs.getString("token", "") ?: ""
            val uHash = prefs.getString("hash", "") ?: ""
            
            val request = mapOf(
                "token" to token,
                "u_hash" to uHash,
                "p_id" to paymentId
            )

            val response = Api.retrofitService.getPayment(request)
            
            if (response.code == "200" && response.data.payment.isNotEmpty()) {
                Result.success(response.data.payment[0])
            } else {
                Result.failure(Exception("Payment not found"))
            }
        } catch (e: Exception) {
            Log.e("SubscriptionRepo", "Error getting payment status", e)
            Result.failure(e)
        }
    }

    suspend fun getUserSubscriptions(): Result<List<SubscriptionInfo>> = withContext(Dispatchers.IO) {
        try {
            val token = prefs.getString("token", "") ?: ""
            val uHash = prefs.getString("hash", "") ?: ""
            
            val request = mapOf(
                "token" to token,
                "u_hash" to uHash
            )

            val response = Api.retrofitService.getSubscription(request)
            
            if (response.code == "200") {
                Result.success(response.data.subscription)
            } else {
                Result.failure(Exception("Failed to get subscriptions: ${response.code}"))
            }
        } catch (e: Exception) {
            Log.e("SubscriptionRepo", "Error getting subscriptions", e)
            Result.failure(e)
        }
    }

    suspend fun getTariffs(): Result<Map<String, Map<String,Any>>> = withContext(Dispatchers.IO) {
        try {
            Log.d("SubscriptionRepo", "Making getTariffs API call...")
            val response = Api.retrofitService.getTariffs()
            Log.d("SubscriptionRepo", "getTariffs response code: ${response.code}")
            
            if (response.code == "200") {
                val tariffs = response.data.data.tariffs ?: emptyMap()
                Log.d("SubscriptionRepo", "Tariffs received: ${tariffs.size} items")
                Result.success(tariffs)
            } else {
                Log.e("SubscriptionRepo", "Failed to get tariffs: ${response.code}")
                Result.failure(Exception("Failed to get tariffs: ${response.code}"))
            }
        } catch (e: Exception) {
            Log.e("SubscriptionRepo", "Error getting tariffs", e)
            Result.failure(e)
        }
    }

    // Payment status constants
    companion object {
        const val PAYMENT_CREATED = 1
        const val PAYMENT_CANCELED = 3
        const val PAYMENT_SUCCEEDED = 6
    }
}
