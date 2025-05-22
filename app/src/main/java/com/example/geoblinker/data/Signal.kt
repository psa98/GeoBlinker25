package com.example.geoblinker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "signals",
    foreignKeys = [
        ForeignKey(
            entity = Device::class,
            parentColumns = ["imei"],
            childColumns = ["deviceId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Signal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deviceId: String,
    val name: String,
    val dateTime: Long,
    val isSeen: Boolean = false
)
