package com.example.geoblinker.ui.auth

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

class LoginViewModel(
    private val application: Application
): ViewModel() {
    private val _prefs = application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

    var login by mutableStateOf(false)
        private set

    init {
        login = _prefs.getBoolean("login", false)
    }

    fun setInitialSubscription() {
        viewModelScope.launch {
            val time = Instant
                .now()
                .atZone(ZoneId.systemDefault())
                .plusMinutes(2)
                .toInstant()
                .toEpochMilli()

            _prefs.edit().putLong("subscription", time).apply()
        }
    }

    fun setIsLogin(it: Boolean) {
        viewModelScope.launch {
            _prefs.edit().putBoolean("login", it).apply()

            withContext(Dispatchers.Main) {
                login = it
            }
        }
    }
}