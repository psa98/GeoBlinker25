package com.example.geoblinker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(device: Device)

    @Query("DELETE FROM devices")
    suspend fun clear()

    @Query("SELECT * FROM devices WHERE imei = :imei")
    fun getDevice(imei: String): Flow<Device>

    @Query("SELECT * FROM devices")
    fun getAllDevices(): Flow<List<Device>>

    @Update
    suspend fun updateDevice(device: Device)
}