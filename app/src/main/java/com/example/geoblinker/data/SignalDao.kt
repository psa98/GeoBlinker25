package com.example.geoblinker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SignalDao {
    @Insert
    suspend fun insert(signal: Signal)

    @Query("SELECT * FROM signals")
    fun getAll(): Flow<List<Signal>>

    @Query("SELECT * FROM signals WHERE deviceId = :imei")
    fun getAllDevice(imei: String): Flow<List<Signal>>
}