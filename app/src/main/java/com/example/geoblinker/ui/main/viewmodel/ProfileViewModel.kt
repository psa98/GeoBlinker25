package com.example.geoblinker.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.R
import com.example.geoblinker.model.Code
import com.example.geoblinker.model.Profile
import com.example.geoblinker.network.Api
import com.example.geoblinker.ui.WayConfirmationCode
import com.example.geoblinker.ui.main.GeoBlinker.Companion.gson
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

private val INITIAL_WAYS = listOf(
    WayConfirmationCode("Telegram"),
    WayConfirmationCode("WhatsApp"),
    WayConfirmationCode("SMS"),
    WayConfirmationCode("Email")
)

class ProfileViewModel(
    private val application: Application
): ViewModel() {
    private val _prefs = application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    private var _token by mutableStateOf("")
    private var _hash by mutableStateOf("")
    private val _isLogin = MutableStateFlow(false)
    private val _subscription = MutableStateFlow<Long>(0)
    private val _phone = MutableStateFlow("")
    private val _email = MutableStateFlow(_prefs.getString("email","")!!)
    private val _orderWays = MutableStateFlow("")
    private val _waysConfirmationCode = MutableStateFlow(
        if (_email.value.isNotEmpty()) listOf(
            WayConfirmationCode("Telegram"),
            WayConfirmationCode("WhatsApp"),
            WayConfirmationCode("SMS"),
            WayConfirmationCode("Email")
        ) else
            listOf(WayConfirmationCode("Telegram"),
        WayConfirmationCode("WhatsApp"),
        WayConfirmationCode("SMS"),
    )

    )
    val subscription = _subscription.asStateFlow()
    var name = mutableStateOf("Константин Гусевский")
        private set
    val phone = _phone.asStateFlow()
    val isLogin = _isLogin.asStateFlow()
    val email = _email.asStateFlow()
    val waysConfirmationCode = _waysConfirmationCode.asStateFlow()
    var uiState: MutableState<DefaultStates> = mutableStateOf(DefaultStates.Input)
        private set

    init {
        viewModelScope.launch {
            _token = _prefs.getString("token", null) ?: ""
            _hash = _prefs.getString("hash", null) ?: ""
            
            // YANGI: max_subscription_end_date dan olamiz
            val maxEndDate = _prefs.getLong("max_subscription_end_date", 0)
            val oldSubscription = _prefs.getLong("subscription", 0)
            
            // Eng katta qiymatni tanlaymiz (millisekundlarda)
            val subscriptionEndDate = if (maxEndDate > 0) {
                maxEndDate * 1000 // sekunddan millisekundga
            } else {
                oldSubscription
            }
            
            _subscription.value = subscriptionEndDate
            Log.d("ProfileViewModel", "Subscription end date set to: $subscriptionEndDate (max_end_date: $maxEndDate)")
            
            name.value = _prefs.getString("name", "") ?: ""
            _phone.value = _prefs.getString("phone", "") ?: ""
            _isLogin.value = _prefs.getBoolean("login", false)
            _email.value = _prefs.getString("email", "") ?: ""
            _orderWays.value = _prefs.getString("orderWays", "0123") ?: "0123"
            
            // XAVFSIZ: orderWays bo'sh bo'lsa, default qiymat ishlatamiz
            if (_orderWays.value.length < INITIAL_WAYS.size) {
                _orderWays.value = "0123"
            }
            
            _waysConfirmationCode.value = List(INITIAL_WAYS.size) { index ->
                val orderIndex = try {
                    _orderWays.value[index].digitToInt()
                } catch (e: Exception) {
                    index // default index
                }
                WayConfirmationCode(INITIAL_WAYS[orderIndex].text, _prefs.getBoolean(INITIAL_WAYS[orderIndex].text, false))
            }
        }
    }

    fun addMonthsSubscription(months: Long) {
        viewModelScope.launch {
            val newTime = Instant
                .ofEpochMilli(_subscription.value)
                .atZone(ZoneId.systemDefault())
                .plusMonths(months)
                .toInstant()
                .toEpochMilli()

            _prefs.edit().putLong("subscription", newTime).apply()

            withContext(Dispatchers.Main) {
                _subscription.value = newTime
            }
        }
    }
    
    fun refreshSubscriptionFromPrefs() {
        viewModelScope.launch {
            val maxEndDate = _prefs.getLong("max_subscription_end_date", 0)
            val oldSubscription = _prefs.getLong("subscription", 0)
            
            val subscriptionEndDate = if (maxEndDate > 0) {
                maxEndDate * 1000 // sekunddan millisekundga
            } else {
                oldSubscription
            }
            
            _subscription.value = subscriptionEndDate
            Log.d("ProfileViewModel", "🔄 Subscription refreshed: $subscriptionEndDate")
        }
    }

    fun resetUiState() {
        uiState.value = DefaultStates.Input
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            if (newName.isEmpty()) {
                uiState.value = DefaultStates.Error(R.string.name_cannot_empty)
                return@launch
            }
            val res: Code
            try {
                res = Api.retrofitService.editUser(
                    mapOf(
                        "token" to _token,
                        "u_hash" to _hash,
                        "data" to gson.toJson(Profile(
                            name = newName
                        ))
                    )
                )
                Log.d("ChangeName", "Code: ${res.code}, message: ${res.message ?: "Unknown"}")
            } catch(e: Exception) {
                Log.e("ChangeName", e.toString())
                uiState.value = DefaultStates.Error(R.string.server_error)
                return@launch
            }
            if (res.code == "200") {
                _prefs.edit().putString("name", newName).apply()
                name.value = newName
                uiState.value = DefaultStates.Success
            } else {
                uiState.value = DefaultStates.Error(R.string.server_error)
            }
        }
    }

    fun updateEmail(newMail: String) {
        viewModelScope.launch {
            if (newMail.isEmpty()) {
                uiState.value = DefaultStates.Error(R.string.name_cannot_empty)
                return@launch
            }
            val res: Code
            try {
                res = Api.retrofitService.editUser(
                    mapOf(
                        "token" to _token,
                        "u_hash" to _hash,
                        "data" to gson.toJson(Profile(
                            email = newMail
                        ))
                    )
                )
                Log.d("ChangeName", "Code: ${res.code}, message: ${res.message ?: "Unknown"}")
            } catch(e: Exception) {
                Log.e("ChangeName", e.toString())
                uiState.value = DefaultStates.Error(R.string.server_error)
                return@launch
            }
            if (res.code == "200") {
                _prefs.edit().putString("name", newMail).apply()
                _email.value = newMail
                uiState.value = DefaultStates.Success
            } else {
                uiState.value = DefaultStates.Error(R.string.server_error)
            }
        }
    }

    fun checkPhone(code: String, phone: String): Boolean {
        /**
         * TODO: Необходимо добавить проверку телефона
         */
        return code == "1234"
    }

    fun setPhone(phone: String) {
        viewModelScope.launch {
            _prefs.edit().putString("phone", phone).apply()

            withContext(Dispatchers.Main) {
                _phone.value = phone
            }
        }
    }

    fun changeConfirmationCode(indexA: Int, indexB: Int) {
        val now = _waysConfirmationCode.value[indexA].copy()
        _waysConfirmationCode.value[indexA].text = _waysConfirmationCode.value[indexB].text
        _waysConfirmationCode.value[indexA].checked = _waysConfirmationCode.value[indexB].checked
        _waysConfirmationCode.value[indexB].text = now.text
        _waysConfirmationCode.value[indexB].checked = now.checked

        viewModelScope.launch {
            val orderWays = _orderWays.value.toCharArray()
            orderWays[indexA] = _orderWays.value[indexB]
            orderWays[indexB] = _orderWays.value[indexA]
            _orderWays.value = String(orderWays)
            _prefs.edit().putString("orderWays", _orderWays.value).apply()
        }
    }

    fun setCheckedWayConfirmationCode(index: Int, it: Boolean) {
        _waysConfirmationCode.value[index].checked = it

        viewModelScope.launch {
            _prefs.edit().putBoolean(_waysConfirmationCode.value[index].text, it).apply()
        }
    }

    fun logout() {
        val editor = _prefs.edit()

        //_prefs.edit().clear().apply()
        editor.remove("selected_tariff_id").apply()
        editor.remove("max_subscription_end_date").apply()
        editor.remove("current_payment_id").apply()
        editor.remove("trackerNamesMap").apply()
        editor.remove("tracker2EventList").apply()
        editor.remove("tracker4EventList").apply()
        editor.remove("token").apply()
        editor.remove("hash").apply()
        //editor.remove("email")
        editor.remove("login").apply()
        editor.remove("success_message").apply()
        editor.remove("error_message").apply()
        editor.remove("unitsDistance").apply()
        editor.remove("updateMap").apply()
        editor.remove("name").apply()
        editor.remove("phone").apply()
        editor.remove("subscription").apply()
        editor.remove("lastTime").apply()
        editor.remove("sid").apply()
        editor.remove("sidFamily").apply()
        _isLogin.value = false
        _subscription.value = 0
        name.value = ""
        _phone.value = ""
        _email.value = ""
        _orderWays.value = ""
        _waysConfirmationCode.value = listOf(
            WayConfirmationCode("Telegram"),
            WayConfirmationCode("WhatsApp"),
            WayConfirmationCode("SMS"),
            WayConfirmationCode("Email")
        )
    }
}