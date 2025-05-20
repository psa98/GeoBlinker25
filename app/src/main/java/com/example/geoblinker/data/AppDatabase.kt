package com.example.geoblinker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Device::class, TypeSignal::class], // Ваши Entity-классы
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao // Метод для доступа к DAO
    abstract fun typeSignalDao(): TypeSignalDao

    companion object {
        private val MIRGATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        CREATE TABLE 'type_signals' (
                            'id' INTEGER NOT NULL PRIMARY KEY,
                            'deviceId' TEXT NOT NULL,
                            'type' TEXT NOT NULL,
                            'checked' INTEGER NOT NULL,
                            'checkedPush' INTEGER NOT NULL,
                            'checkedEmail' INTEGER NOT NULL,
                            'checkedAlarm' INTEGER NOT NULL,
                            'soundUri' TEXT,
                            FOREIGN KEY ('deviceId') REFERENCES 'devices' ('imei') ON DELETE CASCADE
                        )
                    """.trimIndent()
                )
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addMigrations(MIRGATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}