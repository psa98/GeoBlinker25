package com.example.geoblinker.worker

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class NotificationWorkerFactory(
    private val repository: NotificationRepository,
    private val prefs: SharedPreferences
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ) = when (workerClassName) {
        NotificationPollWorker::class.java.name ->
            NotificationPollWorker(appContext, workerParameters, repository, prefs)
        else -> null
    }
}