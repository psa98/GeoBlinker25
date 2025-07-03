package com.example.geoblinker.ui.auth.registration

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.model.Code
import com.example.geoblinker.network.Api
import com.example.geoblinker.ui.auth.AuthViewModel
import com.example.geoblinker.ui.auth.RegisterUiState
import kotlinx.coroutines.launch

class RegistrationViewModel(application: Application) : AuthViewModel(application) {
    var registerUiState: MutableState<RegisterUiState> = mutableStateOf(RegisterUiState.Input)
        private set

    fun resetRegisterUiState() {
        registerUiState.value = RegisterUiState.Input
    }

    fun register(newPhone: String, newName: String) {
        if (newPhone.length < 10) {
            registerUiState.value = RegisterUiState.Error.ErrorPhone
            return
        }
        if (newName.isEmpty()) {
            registerUiState.value = RegisterUiState.Error.ErrorName
            return
        }
        viewModelScope.launch {
            val response: Code
            try {
                response = Api.retrofitService.register(
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
                    registerUiState.value = RegisterUiState.Success
                } else {
                    //Log.e("Register", response.message ?: "Unknown Error")
                    registerUiState.value = RegisterUiState.Error.ErrorDoublePhone
                }
            } catch (e: Exception) {
                Log.e("Register", e.toString())
                registerUiState.value = RegisterUiState.Error.ErrorRegister
                return@launch
            }
        }
    }
}