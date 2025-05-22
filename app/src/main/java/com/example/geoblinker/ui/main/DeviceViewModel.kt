package com.example.geoblinker.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.data.Device
import com.example.geoblinker.data.News
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
    private val _signals = MutableStateFlow<List<Signal>>(emptyList())
    private val _news = MutableStateFlow<List<News>>(emptyList())
    private val _countNotifications = MutableStateFlow(0)
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()
    val device: StateFlow<Device> = _device.asStateFlow()
    val typesSignals: StateFlow<List<TypeSignal>> = _typesSignals.asStateFlow()
    val typeSignal: StateFlow<TypeSignal> = _typeSignal.asStateFlow()
    val signalsDevice: StateFlow<List<Signal>> = _signalsDevice.asStateFlow()
    val signals: StateFlow<List<Signal>> = _signals.asStateFlow()
    val news: StateFlow<List<News>> = _news.asStateFlow()
    val countNotifications: StateFlow<Int> = _countNotifications.asStateFlow()

    init {
        // Запускаем подписку на изменения
        viewModelScope.launch {
            launch {
                repository.getDevices()
                    .collect { devicesList ->
                        _devices.value = devicesList // Обновляем StateFlow
                    }
            }
            launch {
                repository.getAllSignals()
                    .collect {
                        _signals.value = it
                    }
            }
            launch {
                repository.getAllNews()
                    .collect {
                        _news.value = it
                    }
            }
            launch {
                _countNotifications.value = _signals.value.size + _news.value.size - (_signals.value.count { it.isSeen } + _news.value.count { it.isSeen })
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
            /**
             * TODO: Пока это новость-затычка, потом нужно убрать
             */
            repository.clearNews()
            repository.insertNews(
                News(
                    description = "Спешим порадовать вас! В период с 02.02.25 по 10.03.25 будет действовать 10% скидка на годовую подписку! Спешите купить её, пока выгодно!",
                    dateTime = Instant.now().toEpochMilli()
                )
            )
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

    fun updateSignal(signal: Signal) {
        viewModelScope.launch {
            repository.updateSignal(signal)
        }
    }

    fun updateNews(news: News) {
        viewModelScope.launch {
            repository.updateNews(news)
        }
    }
}