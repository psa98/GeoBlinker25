package com.example.geoblinker.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.geoblinker.R
import com.example.geoblinker.data.Signal
import com.example.geoblinker.data.SignalType
import java.util.concurrent.TimeUnit

class NotificationPollWorker(
    ctx: Context,
    params: WorkerParameters,
    private val repository: NotificationRepository,
    private val prefs: SharedPreferences
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        var lastTime  = prefs.getLong("lastTime", 0)
        val sid       = prefs.getString("sid", "") ?: ""
        val sidFamily = prefs.getString("sidFamily", "") ?: ""

        Log.d("doWork", "Start, lastTime: $lastTime, sid: $sid, sidFamily: $sidFamily")

        if (sid.isBlank() || sidFamily.isBlank()) {
            // нет авторизации – нечего делать
            return Result.failure()
        }

        return try {
            val list = repository.getListNotifications(sid, sidFamily)
            val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

            val channelId = "geoblinker_channel"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "GeoBlinker alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Push-уведомления об изменениях"
                    enableVibration(true)
                }
                nm.createNotificationChannel(channel)
            }

            Log.d("notifications", list.toString())

            // Показываем по одному уведомлению на каждый элемент
            list.items.sortedBy { it.time }.forEach { notification ->
                if (notification.time > lastTime) {
                    val device = repository.getDevice(notification.imei.toString())
                    val typeN = SignalType.fromRaw(notification.type)
                    typeN?.let { type ->
                        val typeR = repository.getTypesSignal(device.id, type.name)

                        if (typeR.checked) {
                            repository.insertSignal(
                                Signal(
                                    deviceId = device.id,
                                    name = type.description,
                                    dateTime = notification.time * 1000
                                )
                            )

                            if (typeR.checkedPush) {
                                val builder = NotificationCompat.Builder(applicationContext, channelId)
                                    .setSmallIcon(R.drawable.notifications)  // ваш значок
                                    .setContentTitle(device.name.ifEmpty { device.imei })
                                    .setContentText(type.description)
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                val id = notification.time.hashCode()
                                nm.notify(id, builder.build())
                            }
                            if (typeR.checkedEmail) {
                                // TODO: В разработке
                            }
                            if (typeR.checkedAlarm) {
                                // TODO: В разработке
                            }
                        }
                    }
                    lastTime = notification.time
                    prefs.edit().putLong("lastTime", lastTime).apply()
                }
            }
            val next = OneTimeWorkRequestBuilder<NotificationPollWorker>()
                .setInitialDelay(5, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(applicationContext)
                .enqueue(next)

            Result.success()
        } catch (e: Exception) {
            Log.e("worker", e.toString())
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "NotificationPollWorker"

        fun schedule(context: Context) {
            val first = OneTimeWorkRequestBuilder<NotificationPollWorker>()
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    WORK_NAME,
                    ExistingWorkPolicy.KEEP,
                    first
                )
        }
    }
}