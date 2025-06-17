package com.example.geoblinker.ui.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.R
import com.example.geoblinker.model.Authorization
import com.example.geoblinker.network.RegistrationApi
import kotlinx.coroutines.launch

abstract class AuthViewModel : ViewModel() {
    private val wayCodes = mapOf("Telegram" to "1234", "WhatsApp" to "6940", "SMS" to "2233", "Email" to "1111")
    private val wayTitles = mapOf(
        "Telegram" to R.string.telegram_way_title,
        "WhatsApp" to R.string.whatsapp_way_title,
        "SMS" to R.string.sms_way_title,
        "Email" to R.string.email_way_title
    )
    private var waysGetCode by mutableStateOf<List<String>>(emptyList())
    private var nowWay by mutableIntStateOf(0)

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
                "WhatsApp" -> Log.d("SendCode", "WhatsApp: " + RegistrationApi.retrofitService.auth(
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
                    "WhatsApp" -> RegistrationApi.retrofitService.auth(
                        mapOf(
                            "login" to "7$phone", // 7 999 999 99 99
                            "password" to code,
                            "type" to "whatsapp"
                        )
                    )

                    else -> {
                        Authorization(
                            code = wayCodes[waysGetCode[nowWay]]!!
                        )
                    }
                }
                codeUiState = if (res.code == "200" && res.user != null) {
                    name = res.user.name
                    email = res.user.email ?: ""
                    CodeUiState.Success
                } else
                    CodeUiState.Error
            } catch (e: Exception) {
                Log.e("Code", e.toString())
                codeUiState = CodeUiState.Error
            }
        }
    }
}