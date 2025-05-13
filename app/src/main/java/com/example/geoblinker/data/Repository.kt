package com.example.geoblinker.data

import kotlinx.coroutines.flow.Flow

class Repository(private val deviceDao: DeviceDao) {
    suspend fun insertDevice(device: Device) {
        deviceDao.insert(device)
    }

    suspend fun clearDevice() = deviceDao.clear()

    fun getDevice(imei: String): Flow<Device> {
        return deviceDao.getDevice(imei)
    }

    fun getDevices(): Flow<List<Device>> = deviceDao.getAllDevices()
}