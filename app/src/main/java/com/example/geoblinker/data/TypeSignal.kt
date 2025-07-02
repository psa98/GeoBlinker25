package com.example.geoblinker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type_signals")
data class TypeSignal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deviceId: String,
    val type: SignalType,
    val checked: Boolean = false,
    val checkedPush: Boolean = false,
    val checkedEmail: Boolean = false,
    val checkedAlarm: Boolean = false,
    val soundUri: String? = null // Для типа ALARM
)
