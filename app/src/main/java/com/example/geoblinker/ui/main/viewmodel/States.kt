package com.example.geoblinker.ui.main.viewmodel

import androidx.annotation.StringRes

sealed class DefaultStates {
    data object Input: DefaultStates()
    data object Loading: DefaultStates()
    data object Success: DefaultStates()
    data class Error(@StringRes val message: Int): DefaultStates()
}