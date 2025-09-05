package com.example.geoblinker.ui.auth

import android.app.Activity.MODE_PRIVATE
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.R
import com.example.geoblinker.model.Authorization
import com.example.geoblinker.network.Api
import com.example.geoblinker.ui.main.GeoBlinker.Companion.gson
import kotlinx.coroutines.launch

abstract class AuthViewModel(
    private val application: Application
) : ViewModel() {
    private val _prefs = application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    private val _avatarPrefs = application.getSharedPreferences("avatar_prefs", Context.MODE_PRIVATE)
    private val wayCodes = mapOf("Telegram" to "1234", "WhatsApp" to "6940", "SMS" to "2233", "Email" to "1111")
    private val wayTitles = mapOf(
        "Telegram" to R.string.telegram_way_title,
        "WhatsApp" to R.string.whatsapp_way_title,
        "SMS" to R.string.sms_way_title,
        "Email" to R.string.email_way_title
    )
    private var waysGetCode by mutableStateOf<List<String>>(emptyList())
    private var nowWay by mutableIntStateOf(0)
    private var token by mutableStateOf("")
    private var hash by mutableStateOf("")

    var phone = mutableStateOf("")
        private set
    var name = mutableStateOf("")
        private set
    var email = mutableStateOf(_prefs.getString("email", "") ?: ""
    )
    var tgId = mutableStateOf(_prefs.getString("tgId", "") ?: ""
    )
        private set
    var codeUiState: MutableState<CodeUiState> = mutableStateOf(CodeUiState.Input)
        private set

    fun resetCodeUiState() {
        codeUiState.value = CodeUiState.Input
    }

    fun updatePhone(newPhone: String) {
        phone.value = newPhone
    }

    fun updateName(newName: String) {
        name.value = newName
    }

    fun setWays(ways: List<String>) {
        waysGetCode = ways
        nowWay = 0
    }

    fun sendCode() {
        viewModelScope.launch {
            when (waysGetCode[nowWay]) {
                "WhatsApp" -> Api.retrofitService.auth(
                    mapOf(
                        "login" to "7${phone.value}", // 7 999 999 99 99
                        "type" to "whatsapp",
                        "debug" to ""
                    )
                ).code
                "SMS" -> Api.retrofitService.auth(
                    mapOf(
                        "login" to "7${phone.value}", // 7 999 999 99 99
                        "type" to "phone_code"
                    )
                ).code
                "Email" -> Api.retrofitService.auth(
                    mapOf(
                        "login" to email.value, // 7 999 999 99 99
                        "type" to "e-mail_code"
                    )
                ).code
                "Telegram" -> Api.retrofitService.auth(
                    mapOf(
                        "login" to "7${phone.value}", // 7 999 999 99 99
                        "type" to "telegram_id"
                    )
                ).code
                else -> {}
            }
        }
    }

    fun getNextWay(): Int? {
        if (waysGetCode.isEmpty())
            return null
        nowWay = (nowWay + 1) % waysGetCode.size
        sendCode()
        return wayTitles[waysGetCode[nowWay]]!!
    }

    fun getNowWay(): Int? {
        if (waysGetCode.isEmpty())
            return null
        sendCode()
        return wayTitles[waysGetCode[nowWay]]!!
    }



    fun checkWay(code: String) {
        viewModelScope.launch {
            try {
                val res = when (waysGetCode[nowWay]) {
                    "WhatsApp" -> Api.retrofitService.auth(
                        mapOf(
                            "login" to "7${phone.value}", // 7 999 999 99 99
                            "password" to code,
                            "type" to "whatsapp",
                            "debug" to ""
                        )
                    )
                    "SMS" -> Api.retrofitService.auth(
                        mapOf(
                            "login" to "7${phone.value}", // 7 999 999 99 99
                            "password" to code,
                            "type" to "phone_code"
                        )
                    )
                    "Email" -> Api.retrofitService.auth(
                        mapOf(
                            "login" to email.value, // 7 999 999 99 99
                            "password" to code,
                            "type" to "e-mail_code"
                        )
                    )
                    "Telegram" -> Api.retrofitService.auth(
                        mapOf(
                            "login" to "7${phone.value}", // 7 999 999 99 99
                            "password" to code,
                            "type" to "telegram_id"
                        )
                    )
                    else -> {
                        Authorization(
                            code = "404"
                        )
                    }
                }
                codeUiState.value = if (res.code == "200" && res.user != null) {
                    name.value = res.user.name
                    email.value = res.user.email ?: ""
                    Log.d("Photo", res.user.photo)
                    _avatarPrefs.edit().putString("avatar_uri", res.user.photo).apply()
                    val timeHash = res.hash!!
                    val data = Api.retrofitService.getToken(timeHash).data
                    token = data.token
                    hash = data.hash
                    _prefs.edit().putString("token",token).apply()
                    _prefs.edit().putString("hash",hash).apply()

                    try {
                        val repository = com.example.geoblinker.network.SubscriptionRepository(application)
                        val subscriptionsResult = repository.getUserSubscriptions()
                        val tariffsResult = repository.getTariffs()

                        val tariffNamesMap = HashMap<String,String>()
                        tariffsResult.getOrNull()?.forEach{ entry: Map.Entry<String, Map<String, Any>> ->
                            val num = entry.key
                            val name = entry.value["ru"]
                            tariffNamesMap[num] = name.toString()
                        }
                        _prefs.edit().putString("tariff_names_map",gson.toJson(tariffNamesMap)).apply()
                        if (subscriptionsResult.isSuccess && tariffsResult.isSuccess) {
                            val subscriptions = subscriptionsResult.getOrNull() ?: emptyList()
                            val tariffs = tariffsResult.getOrNull() ?: emptyMap()

                            var maxEndDate = 0L

                            // Находим максимальную дату окончания всех активных подписок
                            subscriptions.forEach { subscription ->
                                if (subscription.subsStatus == "1" && subscription.paid) {
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

                                        val endDate = subscription.startDate + durationInSeconds

                                        if (endDate > maxEndDate) {
                                            maxEndDate = endDate
                                        }
                                    }
                                }
                            }

                            // Сохраняем максимальную дату окончания

                            _prefs.edit().putLong("max_subscription_end_date", maxEndDate).apply()

                            // НЕМЕДЛЕННО устанавливаем статус активной подписки
                            val currentTime = System.currentTimeMillis() / 1000
                            val isActive = maxEndDate > currentTime
                            _prefs.edit().putBoolean("subscription_active", isActive).apply()
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error calculating max subscription date", e)
                    }

                    CodeUiState.Success
                } else
                    CodeUiState.Error
            } catch (e: Exception) {
                Log.e("Code", e.toString())
                codeUiState.value = CodeUiState.Error
            }
        }
    }


    fun saveData() {
        viewModelScope.launch {
            _prefs
                .edit()
                .putString("name", name.value)
                .putString("email", email.value)
                .putString("phone", phone.value)
                .putString("token", token)
                .putString("hash", hash)
                .apply()
        }
    }
}