package com.example.geoblinker.data

import kotlinx.coroutines.flow.Flow

class Repository(
    private val deviceDao: DeviceDao,
    private val typeSignalDao: TypeSignalDao,
    private val signalDao: SignalDao,
    private val newsDao: NewsDao
) {
    suspend fun insertDevice(device: Device) {
        deviceDao.insert(device)
    }

    suspend fun clearDevice() = deviceDao.clear()

    fun getDevice(imei: String): Flow<Device> {
        return deviceDao.getDevice(imei)
    }

    fun getDevices(): Flow<List<Device>> = deviceDao.getAllDevices()

    suspend fun updateDevice(device: Device) {
        deviceDao.updateDevice(device)
    }

    suspend fun insertAllTypeSignal(imei: String) {
        val listTypeSignal = listOf(
            TypeSignal(deviceId = imei, type = SignalType.MovementStarted),
            TypeSignal(deviceId = imei, type = SignalType.Stop),
            TypeSignal(deviceId = imei, type = SignalType.LowCharge),
            TypeSignal(deviceId = imei, type = SignalType.DoorOpen),
            TypeSignal(deviceId = imei, type = SignalType.ReachedLocation),
        )
        typeSignalDao.insertAll(listTypeSignal)
    }

    fun getTypeSignal(imei: String): Flow<List<TypeSignal>> = typeSignalDao.getTypesSignalsDevice(imei)

    suspend fun updateTypeSignal(typeSignal: TypeSignal) {
        typeSignalDao.update(typeSignal)
    }

    suspend fun insertSignal(signal: Signal) {
        signalDao.insert(signal)
    }

    fun getAllSignals(): Flow<List<Signal>> = signalDao.getAll()

    fun getAllDeviceSignals(imei: String): Flow<List<Signal>> = signalDao.getAllDevice(imei)

    suspend fun updateSignal(signal: Signal) {
        signalDao.update(signal)
    }

    suspend fun insertNews(news: News) {
        newsDao.insert(news)
    }

    fun getAllNews(): Flow<List<News>> = newsDao.getAll()

    suspend fun clearNews() {
        newsDao.clear()
    }

    suspend fun updateNews(news: News) {
        newsDao.update(news)
    }
}