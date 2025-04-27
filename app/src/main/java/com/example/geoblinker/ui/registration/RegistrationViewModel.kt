package com.example.geoblinker.ui.registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {
    private var _phone = MutableStateFlow<String>("")
    private var _name = MutableStateFlow<String>("")

    val name: StateFlow<String> = _name.asStateFlow()

    fun updateState(phone: String, name: String) {
        _phone.value = phone
        _name.value = name
    }
}