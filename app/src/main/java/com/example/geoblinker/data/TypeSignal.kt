package com.example.geoblinker.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "type_signals",
    indices = [Index(value = ["deviceId", "type"], unique = true)]
)
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
