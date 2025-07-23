package com.example.geoblinker.worker

import android.util.Log
import com.example.geoblinker.data.Device
import com.example.geoblinker.data.DeviceDao
import com.example.geoblinker.data.Signal
import com.example.geoblinker.data.SignalDao
import com.example.geoblinker.data.TypeSignal
import com.example.geoblinker.data.TypeSignalDao
import com.example.geoblinker.model.imei.NotificationList
import com.example.geoblinker.model.imei.NotificationParams
import com.example.geoblinker.model.imei.RequestImei
import com.example.geoblinker.network.ApiImei
import com.example.geoblinker.network.ApiServiceImei

class NotificationRepository(
    private val deviceDao: DeviceDao,
    private val typeSignalDao: TypeSignalDao,
    private val signalDao: SignalDao,
    private val api: ApiServiceImei = ApiImei.retrofitService
) {
    suspend fun getListNotifications(sid: String, sidFamily: String): NotificationList {
        val simei = deviceDao.getAllConnectedSimei()
        Log.d("simei", simei.toString())

        val res = api.getSignalList(
            sid,
            RequestImei(
                module = "alarm",
                func = "Get",
                params = NotificationParams(
                    familyId = sidFamily,
                    simei = simei,
                    type = emptyList()
                )
            )
        )

        return res
    }

    suspend fun getDevice(imei: String): Device = deviceDao.getDevice(imei)

    suspend fun getTypesSignal(id: String, type: String): TypeSignal = typeSignalDao.getTypesSignal(id, type)

    suspend fun insertSignal(signal: Signal) = signalDao.insert(signal)
}