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

class LangRepository(context: Context) {
    private val langId = 1 //Русский
    var offerTitle = ""
    var policyTitle = ""
    var offerText = ""
    var policyText = ""
    var aboutTitle = ""
    var aboutText = ""
    private val prefs: SharedPreferences =
        context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    private val gson = Gson().also { Log.e("", "!!: =") }
    private val mapType: Type = object : TypeToken<HashMap<String, String>?>() {}.type
    private val eventNameMap: HashMap<String, String> = gson.fromJson(
        prefs.getString("trackerNamesMap", "[]"), mapType
    )
    private var tracker2EventList: List<String> =
        prefs.getStringSet("tracker2EventList", emptySet<String>())!!.toList()
    private var tracker4EventList: List<String> =
        prefs.getStringSet("tracker4EventList", emptySet<String>())!!.toList()
    private var tracker2EventNames: List<String> = mutableListOf<String>().apply {
        tracker2EventList.forEach { this.add(getNameForEvent(it)) }}
    private var tracker4EventNames: List<String> = mutableListOf<String>().apply {
        tracker4EventList.forEach { this.add(getNameForEvent(it)) }}
    private val faqNamesTagList: List<String> = listOf(
        "e_faq_1",
        "e_faq_2",
        "e_faq_3",
        "e_faq_4",
        "e_faq_5"
    )
    private val faqTextMap: HashMap<String, String> = HashMap()

    fun initConstants() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val trackerResponse = Api.retrofitService.getDeviceSignalsData()
            if (trackerResponse.code == "200") {
                tracker2EventList =
                    trackerResponse.data.data.constants.tracker2.getList().list ?: emptyList()
                tracker4EventList =
                    trackerResponse.data.data.constants.tracker4.getList().list ?: emptyList()
                if (tracker2EventList.isEmpty() || tracker4EventList.isEmpty()) return@launch
                 val langResponse = Api.retrofitService.getLangData(langId.toString())
                if (langResponse.code == "200") {
                    val langValues: Map<String, Map<Int, String>> =
                        langResponse.data.data.langValues
                    tracker4EventList.forEach { eventCode ->
                        val translation = langValues[eventCode]?.get(langId)
                        translation?.let { eventNameMap[eventCode] = translation }
                    }
                    tracker2EventList.forEach { eventCode ->
                        val translation = langValues[eventCode]?.get(langId)
                        translation?.let { eventNameMap[eventCode] = translation }
                    }
                    tracker2EventNames=tracker2EventList.map { getNameForEvent(it) }
                    tracker4EventNames=tracker4EventList.map { getNameForEvent(it) }
                    prefs.edit().putStringSet("tracker2EventList", tracker2EventList.toSet()).apply()
                    prefs.edit().putStringSet("tracker4EventList", tracker4EventList.toSet()).apply()
                    prefs.edit().putString("trackerNamesMap", gson.toJson(eventNameMap)).apply()
                    faqNamesTagList.forEach { tag ->
                        val translation = langValues[tag]?.get(langId)
                        translation?.let { faqTextMap[tag] = translation  }}
                    val offerJson = langValues["e_about_1_oferta"]?.get(langId)?:"[]"
                    // todo - вероятно при неудаче загрузок надо " писать не удалось загрузить с сервера"
                    //и блокировать дальнейшую работу.
                    // todo - добавить сохранение последней оферты и политики в префы
                    val policyJson = langValues["e_about_2_pkd"]?.get(langId)?:"[]"
                    val aboutJson = langValues["e_about_app"]?.get(langId)?:"[]"
                    offerTitle = (gson.fromJson(offerJson,mapType) as Map<String,String>)["title"]
                        ?:""
                    offerText = (gson.fromJson(offerJson,mapType) as Map<String,String>)["body"]
                        ?:""
                    policyTitle = (gson.fromJson(policyJson,mapType) as Map<String,String>)["title"]
                        ?:""
                    policyText = (gson.fromJson(policyJson,mapType) as Map<String,String>)["body"]
                        ?:""

                    aboutTitle = (gson.fromJson(aboutJson,mapType) as Map<String,String>)["title"]
                        ?:""
                    aboutText = (gson.fromJson(aboutJson,mapType) as Map<String,String>)["body"]
                        ?:""
                }
            }

        } catch (e: Exception) {
            Log.e("SubscriptionRepo", "Error creating subscription", e)
        }
    }

    fun getNameForEvent(name: String): String {
        return eventNameMap[name] ?: ""
    }

    fun getNameForFaq(tag: String): String {
        return faqTextMap[tag] ?: ""
    }

}
