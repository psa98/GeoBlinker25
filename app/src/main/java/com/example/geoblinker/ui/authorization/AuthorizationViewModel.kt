package com.example.geoblinker.ui.authorization

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.geoblinker.R
import kotlinx.coroutines.flow.MutableStateFlow

class AuthorizationViewModel(application: Application) : AndroidViewModel(application) {
    private val _wayCodes = mapOf("Telegram" to "1234", "WhatsApp" to "6940", "SMS" to "2233", "Email" to "1111")
    private val _wayTitles = mapOf(
        "Telegram" to R.string.telegram_way_title,
        "WhatsApp" to R.string.whatsapp_way_title,
        "SMS" to R.string.sms_way_title,
        "Email" to R.string.email_way_title
        )
    private val _waysGetCode = MutableStateFlow<List<String>>(emptyList())
    private val _nowWay = MutableStateFlow(0)

    fun setWays(ways: List<String>) {
        _waysGetCode.value = ways
        _nowWay.value = 0
    }

    fun getNextWay(): Int? {
        if (_waysGetCode.value.isEmpty())
            return null
        _nowWay.value = (_nowWay.value + 1) % _waysGetCode.value.size
        return _wayTitles[_waysGetCode.value[_nowWay.value]]!!
    }

    fun getNowWay(): Int? {
        if (_waysGetCode.value.isEmpty())
            return null
        return _wayTitles[_waysGetCode.value[_nowWay.value]]!!
    }

    fun checkWay(code: String): Boolean {
        return code == _wayCodes[_waysGetCode.value[_nowWay.value]]
    }
}