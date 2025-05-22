package com.example.geoblinker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Device::class, TypeSignal::class, Signal::class, News::class], // Ваши Entity-классы
    version = 6
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao // Метод для доступа к DAO
    abstract fun typeSignalDao(): TypeSignalDao
    abstract fun signalDao(): SignalDao
    abstract fun newsDao(): NewsDao

    companion object {
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        ALTER TABLE 'devices' ADD COLUMN 'lat' REAL NOT NULL DEFAULT 0
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        ALTER TABLE 'devices' ADD COLUMN 'lng' REAL NOT NULL DEFAULT 0
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        ALTER TABLE 'news' ADD COLUMN 'isSeen' INTEGER NOT NULL DEFAULT 0
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        ALTER TABLE 'signals' ADD COLUMN 'isSeen' INTEGER NOT NULL DEFAULT 0
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        CREATE TABLE 'news' (
                            'id' INTEGER NOT NULL PRIMARY KEY,
                            'description' TEXT NOT NULL,
                            'dateTime' INTEGER NOT NULL
                        )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        CREATE TABLE 'signals' (
                            'id' INTEGER NOT NULL PRIMARY KEY,
                            'deviceId' TEXT NOT NULL,
                            'name' TEXT NOT NULL,
                            'dateTime' INTEGER NOT NULL,
                             FOREIGN KEY ('deviceId') REFERENCES 'devices' ('imei') ON DELETE CASCADE
                        )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
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
                )
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .addMigrations(MIGRATION_3_4)
                    .addMigrations(MIGRATION_4_5)
                    .addMigrations(MIGRATION_5_6)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}