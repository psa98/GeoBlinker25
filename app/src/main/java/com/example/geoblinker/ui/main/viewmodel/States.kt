package com.example.geoblinker.ui.main.viewmodel

sealed class DefaultStates {
    data object Input: DefaultStates()
    data object Success: DefaultStates()
    sealed class Error: DefaultStates() {
        data object InputError: Error()
        data object ServerError: Error()
    }
}