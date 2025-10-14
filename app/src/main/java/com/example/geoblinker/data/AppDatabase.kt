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
    version = 17
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun typeSignalDao(): TypeSignalDao
    abstract fun signalDao(): SignalDao
    abstract fun newsDao(): NewsDao
    abstract fun ChatTechSupportDao(): ChatTechSupportDao
    abstract fun MessageTechSupportDao(): MessageTechSupportDao

    companion object {

        private val MIGRATION_16_17 = object : Migration(16, 17) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""ALTER TABLE 'devices' ADD COLUMN 'markerId' INTEGER NOT NULL DEFAULT 0
                """)
            }
        }
        private val MIGRATION_15_16 = object : Migration(15, 16) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""ALTER TABLE 'devices' ADD COLUMN 'deviceType' TEXT NOT NULL DEFAULT 'tracker_model2'
                """)
            }
        }
        private val MIGRATION_14_15 = object : Migration(14, 15) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE type_signals")

                db.execSQL(
                    """
                        CREATE TABLE type_signals (
                            id INTEGER PRIMARY KEY NOT NULL,
                            deviceId TEXT NOT NULL,
                            list TEXT NOT NULL,
                            checked INTEGER NOT NULL,
                            checkedPush INTEGER NOT NULL,
                            checkedEmail INTEGER NOT NULL,
                            checkedAlarm INTEGER NOT NULL,
                            soundUri TEXT
                        )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                        CREATE UNIQUE INDEX index_type_signals_deviceId_type
                        ON type_signals(deviceId, list)
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_13_14 = object : Migration(13, 14) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        CREATE TABLE 'devices' (
                            'imei' TEXT NOT NULL,
                            'id' TEXT NOT NULL PRIMARY KEY,
                            'name' TEXT NOT NULL,
                            'isConnected' INTEGER NOT NULL DEFAULT 1,
                            'bindingTime' INTEGER NOT NULL,
                            'simei' TEXT NOT NULL DEFAULT '',
                            'registrationPlate' TEXT NOT NULL,
                            'modelName' TEXT NOT NULL DEFAULT '',
                            'powerRate' INTEGER NOT NULL DEFAULT 0,
                            'signalRate' INTEGER NOT NULL DEFAULT 0,
                            'speed' REAL NOT NULL DEFAULT 0,
                            'lat' REAL NOT NULL,
                            'lng' REAL NOT NULL,
                            'breakdownForecast' TEXT,
                            'maintenanceRecommendations' TEXT,
                            'typeStatus' TEXT NOT NULL DEFAULT 'Available'
                        )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE signals")
                db.execSQL("DROP TABLE type_signals")
                db.execSQL("DROP TABLE devices")

                db.execSQL(
                    """
                        CREATE TABLE 'signals' (
                            'id' INTEGER NOT NULL PRIMARY KEY,
                            'deviceId' TEXT NOT NULL,
                            'name' TEXT NOT NULL,
                            'dateTime' INTEGER NOT NULL,
                            'isSeen' INTEGER NOT NULL DEFAULT 0
                        )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                        CREATE TABLE 'type_signals' (
                            'id' INTEGER NOT NULL PRIMARY KEY,
                            'deviceId' TEXT NOT NULL,
                            'list' TEXT NOT NULL,
                            'checked' INTEGER NOT NULL,
                            'checkedPush' INTEGER NOT NULL,
                            'checkedEmail' INTEGER NOT NULL,
                            'checkedAlarm' INTEGER NOT NULL,
                            'soundUri' TEXT,
                            FOREIGN KEY (deviceId) REFERENCES devices(id) ON DELETE CASCADE
                        )
                    """.trimIndent()
                )
            }
        }
        private val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        ALTER TABLE 'devices' ADD COLUMN 'simei' TEXT NOT NULL DEFAULT ''
                    """.trimIndent()
                )
            }
        }

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
                            'list' TEXT NOT NULL,
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
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7,
                        MIGRATION_7_8,
                        MIGRATION_8_9,
                        MIGRATION_9_10,
                        MIGRATION_10_11,
                        MIGRATION_11_12,
                        MIGRATION_12_13,
                        MIGRATION_13_14,
                        MIGRATION_14_15,
                        MIGRATION_15_16,
                        MIGRATION_16_17,
                        )
                    /*
                    .setQueryCallback(
                        { sqlQuery, bindArgs ->
                            Log.d("RoomSQL", "SQL: $sqlQuery | Args: $bindArgs")
                        },
                        Executors.newSingleThreadExecutor() // или Dispatchers.IO.asExecutor()
                    )
                     */
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}