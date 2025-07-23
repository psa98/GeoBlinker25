package com.example.geoblinker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TypeSignalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg typesSignals: TypeSignal)

    @Query("SELECT * FROM type_signals WHERE deviceId = :id")
    suspend fun getTypesSignalsDevice(id: String): List<TypeSignal>

    @Update
    suspend fun update(typeSignal: TypeSignal)

    @Query("SELECT * FROM type_signals")
    suspend fun getAll(): List<TypeSignal>

    @Query("SELECT * FROM type_signals WHERE deviceId = :id AND type = :type LIMIT 1")
    suspend fun getTypesSignal(id: String, type: String): TypeSignal
}