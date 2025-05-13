package com.example.geoblinker.ui.device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.data.Device
import com.example.geoblinker.data.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.Instant

class DeviceViewModel(private val repository: Repository): ViewModel() {
    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    private val _device = MutableStateFlow(Device("", "", false, 0))
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()
    val device: StateFlow<Device> = _device.asStateFlow()

    init {
        // Запускаем подписку на изменения
        viewModelScope.launch {
            repository.getDevices()
                .collect { devicesList ->
                    _devices.value = devicesList // Обновляем StateFlow
                }
        }
    }

    fun insertDevice(imei: String, name: String) {
        viewModelScope.launch {
            val device = Device(
                imei,
                name,
                bindingTime = Instant.now().toEpochMilli()
            )
            repository.insertDevice(device)
            _device.value = device
        }
    }

    fun clearDevice() {
        viewModelScope.launch {
            repository.clearDevice()
        }
    }

    fun getDevice(imei: String) {
        viewModelScope.launch {
            repository.getDevice(imei).collect {
                _device.value = it
            }
        }
    }

    fun checkDevices(): Boolean {
        return _devices.asStateFlow().value.isNotEmpty()
    }
}