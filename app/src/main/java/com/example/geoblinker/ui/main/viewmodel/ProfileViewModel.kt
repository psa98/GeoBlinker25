package com.example.geoblinker.ui.main.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

class ProfileViewModel(
    private val application: Application
): ViewModel() {
    private val _prefs = application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    private val _subscription = MutableStateFlow<Long>(0)
    private val _name = MutableStateFlow("Константин Гусевский")
    private val _phone = MutableStateFlow("")
    private val _login = MutableStateFlow(false)
    val subscription: StateFlow<Long> = _subscription.asStateFlow()
    val name: StateFlow<String> = _name.asStateFlow()
    val phone: StateFlow<String> = _phone.asStateFlow()
    val login: StateFlow<Boolean> = _login.asStateFlow()

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
            _login.value = _prefs.getBoolean("login", false)
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

    fun setLogin(it: Boolean) {
        viewModelScope.launch {
            _prefs.edit().putBoolean("login", it).apply()

            withContext(Dispatchers.Main) {
                _login.value = it
            }
        }
    }
}