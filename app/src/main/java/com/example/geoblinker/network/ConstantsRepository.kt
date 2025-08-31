package com.example.geoblinker.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class ConstantsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
            .also { Log.e("", "!!: =") }
    private val gson = Gson().also { Log.e("", "!!: =") }
    private var mapType: Type = object : TypeToken<HashMap<String, String>?>() {}.type
    private val eventNameMap: HashMap<String, String> = gson.fromJson(
        prefs.getString("trackerNamesMap", "[]"), mapType
    )
    var tracker2EventList: List<String> =
        prefs.getStringSet("tracker2EventList", emptySet<String>())!!.toList()
    var tracker4EventList: List<String> =
        prefs.getStringSet("tracker4EventList", emptySet<String>())!!.toList()
    var tracker2EventNames: List<String> = mutableListOf<String>().apply {
        tracker2EventList.forEach { this.add(getNameForEvent(it)) }}
    var tracker4EventNames: List<String> = mutableListOf<String>().apply {
        tracker4EventList.forEach { this.add(getNameForEvent(it)) }}

    fun initConstants() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val trackerResponse = Api.retrofitService.getDeviceSignalsData()
            if (trackerResponse.code == "200") {
                tracker2EventList =
                    trackerResponse.data.data.constants.tracker2.getList().list ?: emptyList()
                tracker4EventList =
                    trackerResponse.data.data.constants.tracker4.getList().list ?: emptyList()
                if (tracker2EventList.isEmpty() || tracker4EventList.isEmpty()) return@launch
                 val langResponse = Api.retrofitService.getLangData("1")
                if (langResponse.code == "200") {
                    val langValues: Map<String, Map<Int, String>> =
                        langResponse.data.data.langValues
                    tracker4EventList.forEach { eventCode ->
                        val translation = langValues[eventCode]?.get(1)
                        translation?.let { eventNameMap[eventCode] = translation }
                    }
                    tracker2EventList.forEach { eventCode ->
                        val translation = langValues[eventCode]?.get(1)
                        translation?.let { eventNameMap[eventCode] = translation }
                    }
                    tracker2EventNames=tracker2EventList.map { getNameForEvent(it) }
                    tracker4EventNames=tracker4EventList.map { getNameForEvent(it) }
                    prefs.edit().putStringSet("tracker2EventList", tracker2EventList.toSet()).apply()
                    prefs.edit().putStringSet("tracker4EventList", tracker4EventList.toSet()).apply()
                    prefs.edit().putString("trackerNamesMap", gson.toJson(eventNameMap)).apply()

                }
            }

        } catch (e: Exception) {
            Log.e("SubscriptionRepo", "Error creating subscription", e)
        }
    }

    fun getNameForEvent(name: String): String {
        return eventNameMap[name] ?: ""
    }

}
