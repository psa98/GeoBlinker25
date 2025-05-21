package com.example.geoblinker.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.data.Device
import com.example.geoblinker.data.Repository
import com.example.geoblinker.data.Signal
import com.example.geoblinker.data.TypeSignal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.Instant

class DeviceViewModel(private val repository: Repository): ViewModel() {
    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    private val _device = MutableStateFlow(Device("", "", false, 0))
    private val _typesSignals = MutableStateFlow<List<TypeSignal>>(emptyList())
    private val _typeSignal = MutableStateFlow(TypeSignal(deviceId = "", type = TypeSignal.SignalType.MovementStarted))
    private val _signalsDevice = MutableStateFlow<List<Signal>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()
    val device: StateFlow<Device> = _device.asStateFlow()
    val typesSignals: StateFlow<List<TypeSignal>> = _typesSignals.asStateFlow()
    val typeSignal: StateFlow<TypeSignal> = _typeSignal.asStateFlow()
    val signalsDevice: StateFlow<List<Signal>> = _signalsDevice.asStateFlow()

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
        val nowTime = Instant.now().toEpochMilli()
        val device = Device(
            imei,
            name,
            bindingTime = nowTime
        )
        _device.value = device
        viewModelScope.launch {
            repository.insertDevice(device)
            launch {
                repository.insertAllTypeSignal(imei)
                repository.getTypeSignal(device.imei)
                    .collect {
                        _typesSignals.value = it
                    }
            }
            launch {
                repository.insertSignal(
                    Signal(
                        deviceId = imei,
                        name = "Устройство привязано",
                        dateTime = nowTime
                    )
                )
                repository.getAllDeviceSignals(device.imei)
                    .collect {
                        _signalsDevice.value = it
                    }
            }
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

    fun updateDevice(device: Device) {
        viewModelScope.launch {
            repository.updateDevice(device)
        }
        _device.value = device
    }

    fun checkDevices(): Boolean {
        return _devices.asStateFlow().value.isNotEmpty()
    }

    fun setDevice(device: Device) {
        _device.value = device
        viewModelScope.launch {
            launch {
                repository.getTypeSignal(device.imei)
                    .collect {
                        _typesSignals.value = it
                    }
            }
            launch {
                repository.getAllDeviceSignals(device.imei)
                    .collect {
                        _signalsDevice.value = it
                    }
            }
        }
    }

    fun setTypeSignal(typeSignal: TypeSignal) {
        _typeSignal.value = typeSignal
    }

    fun updateTypeSignal(typeSignal: TypeSignal) {
        viewModelScope.launch {
            repository.updateTypeSignal(typeSignal)
        }
        _typeSignal.value = typeSignal
        _typesSignals.update { typesSignals ->
            typesSignals.map { if (it.type == typeSignal.type) typeSignal else it }
        }
    }
}