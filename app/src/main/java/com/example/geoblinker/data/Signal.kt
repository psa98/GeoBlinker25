package com.example.geoblinker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signals")
data class Signal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deviceId: String,
    val name: String,
    val dateTime: Long,
    val isSeen: Boolean = false
)
