package com.example.geoblinker.data

import kotlinx.coroutines.flow.Flow

class Repository(
    private val deviceDao: DeviceDao,
    private val typeSignalDao: TypeSignalDao,
    private val signalDao: SignalDao,
    private val newsDao: NewsDao
) {
    suspend fun insertAllDevices(devices: List<Device>) {
        deviceDao.insert(*devices.toTypedArray())
        devices.forEach { device ->
            insertAllTypeSignal(device.id)
        }
    }

    suspend fun insertDevice(device: Device) {
        deviceDao.insert(device)
        insertAllTypeSignal(device.id)
    }

    suspend fun updateDevice(device: Device) {
        deviceDao.update(device)
    }

    suspend fun updateAllDevices(devices: List<Device>) {
        deviceDao.updateALl(devices)
    }

    suspend fun clearAllDevices() {
        deviceDao.deleteAll()
    }

    fun getAllDevices(): Flow<List<Device>> = deviceDao.getAll()

    private suspend fun insertAllTypeSignal(id: String) {
        val listTypeSignal = listOf(
            TypeSignal(deviceId = id, type = SignalType.MovementStarted),
            TypeSignal(deviceId = id, type = SignalType.Stop),
            TypeSignal(deviceId = id, type = SignalType.LowCharge),
            TypeSignal(deviceId = id, type = SignalType.DoorOpen),
            TypeSignal(deviceId = id, type = SignalType.VibrationAlarm),
        )
        typeSignalDao.insertAll(*listTypeSignal.toTypedArray())
    }

    suspend fun getTypeSignal(imei: String): List<TypeSignal> = typeSignalDao.getTypesSignalsDevice(imei)

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