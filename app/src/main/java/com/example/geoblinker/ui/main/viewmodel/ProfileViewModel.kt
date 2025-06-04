package com.example.geoblinker.ui.main.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.ui.WayConfirmationCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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
    private val _subscription = MutableStateFlow<Long>(0)
    private val _name = MutableStateFlow("Константин Гусевский")
    private val _phone = MutableStateFlow("")
    private val _isLogin = MutableStateFlow(false)
    private val _email = MutableStateFlow("")
    private val _orderWays = MutableStateFlow("")
    private val _waysConfirmationCode = MutableStateFlow(
        listOf(
            WayConfirmationCode("Telegram"),
            WayConfirmationCode("WhatsApp"),
            WayConfirmationCode("SMS"),
            WayConfirmationCode("Email")
        )
    )
    val subscription: StateFlow<Long> = _subscription.asStateFlow()
    val name: StateFlow<String> = _name.asStateFlow()
    val phone: StateFlow<String> = _phone.asStateFlow()
    val isLogin: StateFlow<Boolean> = _isLogin.asStateFlow()
    val email: StateFlow<String> = _email.asStateFlow()
    val waysConfirmationCode = _waysConfirmationCode.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _subscription.value = _prefs.getLong("subscription", 0)

            if (_subscription.value == 0L) {
                val time = Instant
                    .now()
                    .atZone(ZoneId.systemDefault())
                    .plusMinutes(2)
                    .toInstant()
                    .toEpochMilli()

                _prefs.edit().putLong("subscription", time).apply()

                withContext(Dispatchers.Main) {
                    _subscription.value = time
                }
            }

            _name.value = _prefs.getString("name", "") ?: ""
            _phone.value = _prefs.getString("phone", "") ?: ""
            _isLogin.value = _prefs.getBoolean("isLogin", false)
            _email.value = _prefs.getString("email", "") ?: ""
            _orderWays.value = _prefs.getString("orderWays", "0123") ?: "0123"
             _waysConfirmationCode.value = List(INITIAL_WAYS.size) { index ->
                WayConfirmationCode(INITIAL_WAYS[_orderWays.value[index].digitToInt()].text, _prefs.getBoolean(INITIAL_WAYS[_orderWays.value[index].digitToInt()].text, false))
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

    fun setName(name: String) {
        viewModelScope.launch {
            _prefs.edit().putString("name", name).apply()

            withContext(Dispatchers.Main) {
                _name.value = name
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

    fun setIsLogin(it: Boolean) {
        viewModelScope.launch {
            _prefs.edit().putBoolean("isLogin", it).apply()

            withContext(Dispatchers.Main) {
                _isLogin.value = it
            }
        }
    }

    fun setEmail(email: String) {
        viewModelScope.launch {
            _prefs.edit().putString("email", email).apply()

            withContext(Dispatchers.Main) {
                _email.value = email
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

    fun setWaysConfirmationCode(
        waysConfirmationCode: SnapshotStateList<WayConfirmationCode>
    ) {
        viewModelScope.launch {
            var orderWays = ""
            waysConfirmationCode.forEach {
                orderWays += when(it.text) {
                    "Telegram" -> '0'
                    "WhatsApp" -> '1'
                    "SMS" -> '2'
                    else -> '3' // "Email"
                }
            }
            _orderWays.value = orderWays
            _prefs.edit().putString("orderWays", orderWays).apply()
            _waysConfirmationCode.value = waysConfirmationCode
            waysConfirmationCode.forEach {
                _prefs.edit().putBoolean(it.text, it.checked).apply()
            }
        }
    }

    fun logout() {
        _isLogin.value = false
        _prefs.edit().clear().apply()
    }
}