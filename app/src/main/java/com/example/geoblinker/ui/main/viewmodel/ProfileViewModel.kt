package com.example.geoblinker.ui.main.viewmodel

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

class ProfileViewModel(context: Context): ViewModel() {
    private val _prefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    private val _subscription = MutableStateFlow<Long>(0)
    val subscription: StateFlow<Long> = _subscription.asStateFlow()

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
}