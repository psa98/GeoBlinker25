package com.example.geoblinker.data

import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.geoblinker.R

@Entity(
    tableName = "type_signals",
    foreignKeys = [
        ForeignKey(
            entity = Device::class,
            parentColumns = ["imei"],
            childColumns = ["deviceId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
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
