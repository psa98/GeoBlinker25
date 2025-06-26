package com.example.geoblinker.ui.auth

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.R
import com.example.geoblinker.model.Authorization
import com.example.geoblinker.network.Api
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

    var phone by mutableStateOf("")
        private set
    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var codeUiState: CodeUiState by mutableStateOf(CodeUiState.Input)
        private set

    fun resetCodeUiState() {
        codeUiState = CodeUiState.Input
    }

    fun updatePhone(newPhone: String) {
        phone = newPhone
    }

    fun updateName(newName: String) {
        name = newName
    }

    fun setWays(ways: List<String>) {
        waysGetCode = ways
        nowWay = 0
    }

    fun sendCode() {
        viewModelScope.launch {
            when (waysGetCode[nowWay]) {
                "WhatsApp" -> Log.d("SendCode", "WhatsApp: " + Api.retrofitService.auth(
                    mapOf(
                        "login" to "7$phone", // 7 999 999 99 99
                        "type" to "whatsapp"
                    )
                ).code)
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
                            "login" to "7$phone", // 7 999 999 99 99
                            "password" to code,
                            "type" to "whatsapp"
                        )
                    )

                    else -> {
                        Authorization(
                            code = "404"
                        )
                    }
                }
                codeUiState = if (res.code == "200" && res.user != null) {
                    name = res.user.name
                    email = res.user.email ?: ""
                    Log.d("Photo", res.user.photo)
                    _avatarPrefs.edit().putString("avatar_uri", res.user.photo).apply()
                    val timeHash = res.hash!!
                    val data = Api.retrofitService.getToken(timeHash).data
                    token = data.token
                    hash = data.hash
                    CodeUiState.Success
                } else
                    CodeUiState.Error
            } catch (e: Exception) {
                Log.e("Code", e.toString())
                codeUiState = CodeUiState.Error
            }
        }
    }

    fun saveData() {
        viewModelScope.launch {
            _prefs
                .edit()
                .putString("name", name)
                .putString("email", email)
                .putString("phone", phone)
                .putString("token", token)
                .putString("hash", hash)
                .apply()
        }
    }
}