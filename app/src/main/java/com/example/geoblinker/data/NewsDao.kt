package com.example.geoblinker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert
    suspend fun insert(news: News)

    @Query("SELECT * FROM news")
    fun getAll(): Flow<List<News>>

    @Query("DELETE FROM news")
    suspend fun clear()

    @Update
    suspend fun update(news: News)
}