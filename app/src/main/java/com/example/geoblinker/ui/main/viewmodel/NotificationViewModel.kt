package com.example.geoblinker.ui.main.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoblinker.R
import com.example.geoblinker.data.SignalType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalTime
import java.lang.reflect.Type

private const val DEFAULT_START = 8 * 60
private const val DEFAULT_END = 23 * 60 + 30

class NotificationViewModel(
    private val application: Application
): ViewModel() {
    private val _prefs = application.getSharedPreferences("notification", Context.MODE_PRIVATE)
    private var _prefix = ""

    private val _quietMode = MutableStateFlow(false)
    private val _getAllNotification = MutableStateFlow(true)
    private val _emailNotification = MutableStateFlow(false)
    private val _getNotificationAroundClock = MutableStateFlow(true)
    private val _startTime = MutableStateFlow(DEFAULT_START) // В минутах
    private val _endTime = MutableStateFlow(DEFAULT_END)
    private val _allNotification = MutableStateFlow(true)
    private val _news = MutableStateFlow(false)
    private val _typesSignals = MutableStateFlow<List<TypeSignal>>(emptyList())
    val quietMode = _quietMode.asStateFlow()
    val getAllNotification = _getAllNotification.asStateFlow()
    val emailNotification = _emailNotification.asStateFlow()
    val getNotificationAroundClock = _getNotificationAroundClock.asStateFlow()
    val startTime = _startTime.asStateFlow()
    val endTime = _endTime.asStateFlow()
    val allNotification = _allNotification.asStateFlow()
    val news = _news.asStateFlow()
    val typesSignals = _typesSignals.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _quietMode.value = _prefs.getBoolean("quietMode", false)
            _getAllNotification.value = _prefs.getBoolean("getAllNotification", false)
            _emailNotification.value = _prefs.getBoolean("emailNotification", false)
            _getNotificationAroundClock.value = _prefs.getBoolean("getNotificationAroundClock", false)
            _startTime.value = _prefs.getInt("startTime", DEFAULT_START)
            _endTime.value = _prefs.getInt("endTime", DEFAULT_END)
            _allNotification.value = _prefs.getBoolean("allNotification", true)
            _news.value = _prefs.getBoolean("news", true)
            _typesSignals.value = SignalType.entries.map { TypeSignal(it, _prefs.getBoolean(it.name, true)) }
        }
    }

    fun setQuietMode(it: Boolean) {
        viewModelScope.launch {
            _prefs.edit().putBoolean("quietMode", it).apply()

            withContext(Dispatchers.Main) {
                _quietMode.value = it
            }
        }
    }

    fun setGetAllNotification(it: Boolean) {
        viewModelScope.launch {
            _prefs.edit().putBoolean("getAllNotification", it).apply()

            if (it) {
                setAllNotification(true)
                setNews(true)
                for (i in 0..<SignalType.entries.size)
                    setTypeSignal(i, true)
            }

            withContext(Dispatchers.Main) {
                _getAllNotification.value = it
            }
        }
    }

    fun setEmailNotification(it: Boolean) {
        viewModelScope.launch {
            _prefs.edit().putBoolean("emailNotification", it).apply()

            withContext(Dispatchers.Main) {
                _emailNotification.value = it
            }
        }
    }

    fun setGetNotificationAroundClock(it: Boolean) {
        viewModelScope.launch {
            _prefs.edit().putBoolean("${_prefix}getNotificationAroundClock", it).apply()

            withContext(Dispatchers.Main) {
                _getNotificationAroundClock.value = it
            }
        }
    }

    fun setAllNotification(it: Boolean) {
        viewModelScope.launch {
            _prefs.edit().putBoolean("${_prefix}allNotification", it).apply()

            if (it) {
                for (i in 0..<SignalType.entries.size)
                    setTypeSignal(i, true)
            }

            withContext(Dispatchers.Main) {
                _allNotification.value = it
            }
        }
    }

    fun setNews(it: Boolean) {
        viewModelScope.launch {
            _prefs.edit().putBoolean("${_prefix}news", it).apply()

            withContext(Dispatchers.Main) {
                _news.value = it
            }
        }
    }

    fun setTypeSignal(index: Int, it: Boolean) {
        viewModelScope.launch {
            _prefs.edit().putBoolean("${_prefix}${_typesSignals.value[index].type.name}", it).apply()
            _typesSignals.value[index].checked = it
            var isNotAll = false
            _typesSignals.value.forEach { if (!it.checked) isNotAll = true }
            if (isNotAll) {
                _prefs.edit().putBoolean("${_prefix}allNotification", false).apply()
                _allNotification.value = false
            }
            else {
                _prefs.edit().putBoolean("${_prefix}allNotification", true).apply()
                _allNotification.value = true
            }
        }
    }

    fun toEmail(it: Boolean) {
        if (it) {
            _prefix = "email"
            _getNotificationAroundClock.value = _prefs.getBoolean("emailgetNotificationAroundClock", true)
            _startTime.value = _prefs.getInt("emailstartTime", DEFAULT_START)
            _endTime.value = _prefs.getInt("emailendTime", DEFAULT_END)
            _allNotification.value = _prefs.getBoolean("emailallNotification", true)
            _news.value = _prefs.getBoolean("emailnews", true)
            _typesSignals.value = SignalType.entries.map { TypeSignal(it, _prefs.getBoolean("email${it.name}", true)) }
        }
        else {
            _prefix = ""
            _getNotificationAroundClock.value = _prefs.getBoolean("getNotificationAroundClock", true)
            _startTime.value = _prefs.getInt("startTime", DEFAULT_START)
            _endTime.value = _prefs.getInt("endTime", DEFAULT_END)
            _allNotification.value = _prefs.getBoolean("allNotification", true)
            _news.value = _prefs.getBoolean("news", true)
            _typesSignals.value = SignalType.entries.map { TypeSignal(it, _prefs.getBoolean(it.name, true)) }
        }
    }

    fun logout() {
        _prefs.edit().clear().apply()
        _quietMode.value = false
        _getAllNotification.value = true
        _emailNotification.value = false
        _getNotificationAroundClock.value = true
        _startTime.value = DEFAULT_START // В минутах
        _endTime.value = DEFAULT_END
        _allNotification.value = true
        _news.value = false
        _typesSignals.value = emptyList()
    }
}