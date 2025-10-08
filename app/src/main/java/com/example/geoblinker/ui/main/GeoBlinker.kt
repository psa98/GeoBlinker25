package com.example.geoblinker.ui.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import com.example.geoblinker.data.AppDatabase
import com.example.geoblinker.network.LangRepository
import com.example.geoblinker.worker.NotificationRepository
import com.example.geoblinker.worker.NotificationWorkerFactory
import com.google.gson.Gson

class GeoBlinker : Application(), Configuration.Provider {

    private lateinit var repository: NotificationRepository
    private lateinit var workerFactory: NotificationWorkerFactory

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver)
        val db = AppDatabase.getInstance(this)
        val prefs = getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
        repository = NotificationRepository(db.deviceDao(), db.typeSignalDao(), db.signalDao())
        workerFactory = NotificationWorkerFactory(repository, prefs)
        langData = LangRepository(this)
        langData.initConstants()
        context =this


    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

companion object{
    lateinit var langData: LangRepository
    lateinit var context: Application
    val gson = Gson()

}
}