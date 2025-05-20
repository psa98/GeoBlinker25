package com.example.geoblinker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeSignalDao {
    @Insert
    suspend fun insertAll(typesSignals: List<TypeSignal>)

    @Query("SELECT * FROM type_signals WHERE deviceId = :imei")
    fun getTypesSignalsDevice(imei: String): Flow<List<TypeSignal>>

    @Update
    suspend fun update(typeSignal: TypeSignal)
}