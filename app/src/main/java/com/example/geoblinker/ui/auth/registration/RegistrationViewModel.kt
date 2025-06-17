package com.example.geoblinker.ui.auth.registration

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.model.Code
import com.example.geoblinker.network.RegistrationApi
import com.example.geoblinker.ui.auth.AuthViewModel
import com.example.geoblinker.ui.auth.RegisterUiState
import kotlinx.coroutines.launch

class RegistrationViewModel : AuthViewModel() {
    var registerUiState: RegisterUiState by mutableStateOf(RegisterUiState.Input)
        private set

    fun resetRegisterUiState() {
        registerUiState = RegisterUiState.Input
    }

    fun register(newPhone: String, newName: String) {
        if (newPhone.length < 10) {
            registerUiState = RegisterUiState.Error.ErrorPhone
            return
        }
        if (newName.isEmpty()) {
            registerUiState = RegisterUiState.Error.ErrorName
            return
        }
        viewModelScope.launch {
            val response: Code
            try {
                response = RegistrationApi.retrofitService.register(
                    mapOf(
                        "u_phone" to "7$newPhone", // 7 999 999 99 99
                        "u_name" to newName,
                        "u_role" to "2"
                    )
                )
                if (response.code == "200") {
                    Log.d("Register", "Register Success")
                    updatePhone(newPhone)
                    updateName(newName)
                    registerUiState = RegisterUiState.Success
                } else {
                    //Log.e("Register", response.message ?: "Unknown Error")
                    registerUiState = RegisterUiState.Error.ErrorDoublePhone
                }
            } catch (e: Exception) {
                Log.e("Register", e.toString())
                registerUiState = RegisterUiState.Error.ErrorRegister
                return@launch
            }
        }
    }
}