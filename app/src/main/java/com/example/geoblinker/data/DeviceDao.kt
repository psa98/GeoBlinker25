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
    suspend fun insert(vararg device: Device)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(device: Device)

    @Update
    suspend fun updateALl(devices: List<Device>)

    @Query("DELETE FROM devices")
    suspend fun deleteAll()

    @Query("SELECT * FROM devices")
    fun getAll(): Flow<List<Device>>

    @Query("SELECT simei FROM devices WHERE isConnected = 1 AND simei != ''")
    suspend fun getAllConnectedSimei(): List<String>

    @Query("SELECT * FROM devices WHERE imei=:imei LIMIT 1")
    suspend fun getDevice(imei: String): Device
}