package com.example.geoblinker.ui.auth

sealed class RegisterUiState {
    data object Input : RegisterUiState()
    data object Success : RegisterUiState()
    sealed class Error : RegisterUiState() {
        data object ErrorPhone : Error()
        data object ErrorName : Error()
        data object ErrorDoublePhone : Error()
        data object ErrorRegister : Error()
    }
}

sealed class CodeUiState {
    data object Input : CodeUiState()
    data object Success : CodeUiState()
    data object Error : CodeUiState()
}