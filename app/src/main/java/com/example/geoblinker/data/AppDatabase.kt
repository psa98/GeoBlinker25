package com.example.geoblinker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.geoblinker.data.techsupport.ChatTechSupport
import com.example.geoblinker.data.techsupport.ChatTechSupportDao
import com.example.geoblinker.data.techsupport.MessageTechSupport
import com.example.geoblinker.data.techsupport.MessageTechSupportDao

@Database(
    entities = [Device::class, TypeSignal::class, Signal::class, News::class, ChatTechSupport::class, MessageTechSupport::class],
    version = 11
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun typeSignalDao(): TypeSignalDao
    abstract fun signalDao(): SignalDao
    abstract fun newsDao(): NewsDao
    abstract fun ChatTechSupportDao(): ChatTechSupportDao
    abstract fun MessageTechSupportDao(): MessageTechSupportDao

    companion object {
        private val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        ALTER TABLE 'devices' ADD COLUMN 'id' TEXT NOT NULL DEFAULT ''
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        ALTER TABLE 'message_tech_support' ADD COLUMN 'typeMessage' TEXT NOT NULL DEFAULT 'Text'
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        ALTER TABLE 'message_tech_support' ADD COLUMN 'photoUri' TEXT DEFAULT NULL
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        CREATE TABLE 'chat_tech_support' (
                            'id' INTEGER NOT NULL PRIMARY KEY,
                            'title' TEXT NOT NULL,
                            'lastMessageTime' INTEGER NOT NULL,
                            'lastChecked' INTEGER NOT NULL,
                            'decided' INTEGER NOT NULL
                        )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        CREATE TABLE 'message_tech_support' (
                            'id' INTEGER NOT NULL PRIMARY KEY,
                            'chatId' INTEGER NOT NULL,
                            'content' TEXT NOT NULL,
                            'timeStamp' INTEGER NOT NULL,
                            'isMy' INTEGER NOT NULL,
                            FOREIGN KEY ('chatId') REFERENCES 'chat_tech_support' ('id') ON DELETE CASCADE
                        )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        ALTER TABLE 'devices' ADD COLUMN 'breakdownForecast' TEXT
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        ALTER TABLE 'devices' ADD COLUMN 'maintenanceRecommendations' TEXT
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        ALTER TABLE 'devices' ADD COLUMN 'typeStatus' TEXT NOT NULL DEFAULT 'Available'
                    """.trimIndent()
                )
            }
        }

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
                    .addMigrations(MIGRATION_6_7)
                    .addMigrations(MIGRATION_7_8)
                    .addMigrations(MIGRATION_8_9)
                    .addMigrations(MIGRATION_9_10)
                    .addMigrations(MIGRATION_10_11)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}