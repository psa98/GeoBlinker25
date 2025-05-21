package com.example.geoblinker

import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

object TimeUtils {
    // Инициализируйте ThreeTenABP в классе Application
    fun init(context: Context) {
        AndroidThreeTen.init(context)
    }

    /**
     * Форматирует UTC-время в строку с учётом текущего часового пояса.
     * @param utcMillis Время в миллисекундах (UTC).
     * @return Строка в формате "dd.MM.yyyy HH:mm GMT±X".
     */
    fun formatToLocalTime(utcMillis: Long): String {
        val instant = Instant.ofEpochMilli(utcMillis)
        val zoneId = ZoneId.systemDefault() // Часовой пояс устройства
        val zonedDateTime = instant.atZone(zoneId)

        val formatter = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm 'GMT'xxx")
            .withZone(zoneId)

        return formatter.format(zonedDateTime)
    }

    /**
     * Форматирует UTC-время в строку с учётом текущего часового пояса.
     * @param utcMillis Время в миллисекундах (UTC).
     * @return Строка в формате "dd.MM.yyyy".
     */
    fun formatToLocalTimeDate(utcMillis: Long): String {
        val instant = Instant.ofEpochMilli(utcMillis)
        val zoneId = ZoneId.systemDefault() // Часовой пояс устройства
        val zonedDateTime = instant.atZone(zoneId)

        val formatter = DateTimeFormatter
            .ofPattern("dd.MM.yyyy")
            .withZone(zoneId)

        return formatter.format(zonedDateTime)
    }

    /**
     * Форматирует UTC-время в строку с учётом текущего часового пояса.
     * @param utcMillis Время в миллисекундах (UTC).
     * @return Строка в формате "HH:mm".
     */
    fun formatToLocalTimeTime(utcMillis: Long): String {
        val instant = Instant.ofEpochMilli(utcMillis)
        val zoneId = ZoneId.systemDefault() // Часовой пояс устройства
        val zonedDateTime = instant.atZone(zoneId)

        val formatter = DateTimeFormatter
            .ofPattern("HH:mm")
            .withZone(zoneId)

        return formatter.format(zonedDateTime)
    }
}