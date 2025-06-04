package com.example.geoblinker.ui.main.viewmodel

import com.example.geoblinker.data.SignalType

data class TypeSignal(
    val type: SignalType,
    var checked: Boolean = true
)
