package com.example.geoblinker.ui.main.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoblinker.R
import com.example.geoblinker.data.Device
import com.example.geoblinker.data.News
import com.example.geoblinker.data.Repository
import com.example.geoblinker.data.Signal
import com.example.geoblinker.data.SignalType
import com.example.geoblinker.data.TypeSignal
import com.example.geoblinker.model.Car
import com.example.geoblinker.model.Cars
import com.example.geoblinker.model.Details
import com.example.geoblinker.model.imei.AddParamsImei
import com.example.geoblinker.model.imei.GetDetailImei
import com.example.geoblinker.model.imei.GetDetailParamsImei
import com.example.geoblinker.model.imei.GetDeviceListImei
import com.example.geoblinker.model.imei.GetDeviceListParamsImei
import com.example.geoblinker.model.imei.LoginImei
import com.example.geoblinker.model.imei.LoginParamsImei
import com.example.geoblinker.model.imei.ParamsTrajectoryImei
import com.example.geoblinker.model.imei.PosData
import com.example.geoblinker.model.imei.RequestImei
import com.example.geoblinker.model.imei.TrajectoryImeiItem
import com.example.geoblinker.network.Api
import com.example.geoblinker.network.ApiImei
import com.example.geoblinker.worker.NotificationPollWorker
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import java.security.SecureRandom

class DeviceViewModel(
    private val repository: Repository,
    private val application: Application
) : ViewModel() {
    private var selectedString = "Тип 2"
    private var selectedType = getType(selectedString)
    private val _profilePrefs =
        application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    private var _token by mutableStateOf("")
    private var _hash by mutableStateOf("")
    private var _sid by mutableStateOf("")
    private var _sidFamily by mutableStateOf("")
    private var _sgid by mutableStateOf("")
    private val _prefs = application.getSharedPreferences("device", Context.MODE_PRIVATE)
    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    private val _device = MutableStateFlow(Device("", "", "", false, 0, "", ""))
    private val _typesSignals = MutableStateFlow<List<TypeSignal>>(emptyList())
    private val _typeSignal =
        MutableStateFlow(TypeSignal(deviceId = "", type = SignalType.ShakeAlarm))
    private val _signalsDevice = MutableStateFlow<List<Signal>>(emptyList())
    private val _signals = MutableStateFlow<List<Signal>>(emptyList())
    private val _news = MutableStateFlow<List<News>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()
    val device: StateFlow<Device> = _device.asStateFlow()
    val typesSignals: StateFlow<List<TypeSignal>> = _typesSignals.asStateFlow()
    val typeSignal: StateFlow<TypeSignal> = _typeSignal.asStateFlow()
    val signalsDevice: StateFlow<List<Signal>> = _signalsDevice.asStateFlow()
    val signals: StateFlow<List<Signal>> = _signals.asStateFlow()
    val news: StateFlow<List<News>> = _news.asStateFlow()
    var unitsDistance = mutableStateOf(true) // true - км / м, false - мили / футы
        private set
    var uiState: MutableState<DefaultStates> = mutableStateOf(DefaultStates.Input)
        private set
    var updateMap = mutableStateOf(true)
        private set
    var selectedMarker = mutableStateOf<Device?>(null)
        private set
    var trajectory = mutableStateOf<List<TrajectoryImeiItem>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            repository.clearAllDevices()
            _token = _profilePrefs.getString("token", null) ?: ""
            _hash = _profilePrefs.getString("hash", null) ?: ""
            Log.i("tokenAndHash", "token: $_token, hash: $_hash")
            unitsDistance.value = _prefs.getBoolean("unitsDistance", true)
            updateMap.value = _prefs.getBoolean("updateMap", true)
            var res: Cars
            var next = false
            while (!next && isActive) {
                try {

                    val params = HashMap<String,String>()
                    params["token"] = _token
                    params["u_hash"] = _hash
                    res = Api.retrofitService.getAllCar( params)


                    if (res.code != "200")
                        throw Exception("Code: ${res.code}")
                } catch (e: Exception) {
                    Log.e("getAllCar", e.toString())
                    continue
                }
                val newDevices = mutableListOf<Device>()
                res.data.cars.forEach { entry ->
                    val device = entry.value
                    val newDevice = Device(
                        imei = device.details.imei ?: device.registrationPlate,
                        id = device.id,
                        name = device.details.name,
                        isConnected = device.details.isConnected,
                        bindingTime = device.details.bindingTime,
                        registrationPlate = device.registrationPlate,
                        deviceType = device.details.typeName?:"tracker_model2"
                    )
                    newDevices.add(newDevice)
                    Log.d("devices", "name: ${newDevice.name}, id: ${newDevice.id}, IMEI: ${newDevice.imei}, registrationPlate: ${newDevice.registrationPlate}")
                }
                repository.insertAllDevices(newDevices)
                next = true
            }
            var resImei: LoginImei
            next = false
            while (!next && isActive) {
                try {
                    resImei = ApiImei.retrofitService.login(
                        RequestImei(
                            module = "user",
                            func = "Login",
                            params = LoginParamsImei()
                        )
                    )
                } catch (e: Exception) {
                    Log.e("loginImei", e.toString())
                    continue
                }
                _sid = resImei.sid
                _sidFamily = resImei.family[0]["sid"] as String
                _profilePrefs.edit().putString("sid", _sid).putString("sidFamily", _sidFamily).apply()
                Log.d("LoginImei", "sid: $_sid, sidFamily: $_sidFamily")
                next = true
            }
            var resDeviceListImei: GetDeviceListImei
            next = false
            while (!next && isActive) {
                try {
                    resDeviceListImei = ApiImei.retrofitService.getDeviceList(
                        _sid,
                        RequestImei(
                            module = "family",
                            func = "GetDeviceList",
                            params = GetDeviceListParamsImei(
                                family = _sidFamily
                            )
                        )
                    )
                } catch (e: Exception) {
                    Log.e("getDeviceListImei", e.toString())
                    continue
                }
                _sgid = resDeviceListImei.items[0].sgid
                // собираем Map<imei, simei>
                val imeiToSimei = resDeviceListImei.items.associateBy({ it.imei.toString() }, { it.simei })

                // делаем копию списка с проставленным simei
                val newList = _devices.value.map { device ->
                    val newSimei = imeiToSimei[device.imei] ?: device.simei
                    if (newSimei != device.simei) {
                        Log.i("getDeviceListImei", "name: ${device.name}, simei: $newSimei")
                    }
                    device.copy(simei = newSimei)
                }
                _devices.value = newList
                repository.updateAllDevices(newList)
                NotificationPollWorker.schedule(application)
                next = true
            }
            updateLocationDevices()
        }
        viewModelScope.launch {
            repository.getAllSignals()
                .collect {
                    _signals.value = it
                }
        }
        viewModelScope.launch {
            repository.getAllNews()
                .collect {
                    _news.value = it
                }
        }
        viewModelScope.launch {
            repository.getAllDevices()
                .collect {
                    _devices.value = it
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Firebase.crashlytics.sendUnsentReports()
    }

    fun clearTrajectoryDevice() {
        viewModelScope.launch {
            trajectory.value = emptyList()
        }
    }

    fun getTrajectoryDevice() {
        uiState.value = DefaultStates.Loading
        viewModelScope.launch {
            try {
                val res = ApiImei.retrofitService.getTrajectory(
                    sid = _sid,
                    RequestImei(
                        module = "location",
                        func = "QueryLocation",
                        params = ParamsTrajectoryImei(
                            simei = _device.value.simei
                        )
                    )
                )
                if (res.code != 0)
                    throw Exception(res.toString())
                trajectory.value = res.data
            } catch (e: Exception) {
                Log.e("queryLocation", e.toString())
                uiState.value = DefaultStates.Error(R.string.server_error)
                return@launch
            }
            Log.i("getTrajectory", trajectory.value.toString())
            uiState.value = DefaultStates.Success
        }
    }

    fun setSelectedMarker(device: Device? = null) {
        viewModelScope.launch {
            selectedMarker.value = device
        }
    }

    fun resetUiState() {
        uiState.value = DefaultStates.Input
    }

    fun setUpdateMap(it: Boolean) {
        viewModelScope.launch {
            updateMap.value = it
            _prefs.edit().putBoolean("updateMap", it).apply()
        }
    }

    suspend fun updateLocationDevices() {
        val currentList = _devices.value.map { device ->
            if (device.simei == "" || _sid == "")
                return@map device
            val res: GetDetailImei
            try {
                res = ApiImei.retrofitService.getDetail(
                    sid = _sid,
                    RequestImei(
                        module = "device",
                        func = "GetDetail",
                        params = GetDetailParamsImei(
                            simei = device.simei
                        )
                    )
                )
            } catch (e: Exception) {
                Log.e("getDetailImei", e.toString())
                return@map device
            }
            //Log.i("getDetailImei", _sid)
            //Log.i("getDetailImei", device.name)
            //Log.i("getDetailImei", device.simei)
            //Log.e("getDetailImei", res.error)
            //Log.i("getDetailImei", res.posString)
            if (res.posString == null)
                return@map device
            val pos = Gson().fromJson(res.posString, PosData::class.java)
            return@map device.copy(
                lat = (pos.lat) / 1e6,
                lng = (pos.lon) / 1e6,
                modelName = res.modelName,
                powerRate = res.powerRate,
                signalRate = res.signalRate,
                speed = pos.speed / 3.6 // Перевод из км/ч в м/с
            )
        }
        repository.updateAllDevices(currentList)
        Log.d("updateLocationDevices", "Update")
    }

    fun checkImei(imei: String) {
        viewModelScope.launch {
            if (_devices.value.find { it.imei == imei && it.isConnected } != null) {
                Log.e("addImei", "IMEI уже привязан: $imei")
                uiState.value = DefaultStates.Error(R.string.device_with_this_imei_already_linked)
                return@launch
            }
            /*
            if (imei.length != 15) {
                Log.e("addImei", "Неверный IMEI: $imei")
                uiState.value = DefaultStates.Error(R.string.imei_input_error)
                return@launch
            }
             */
            try {
                val res = ApiImei.retrofitService.add(
                    sid = _sid,
                    RequestImei(
                        module = "device",
                        func = "Add",
                        params = AddParamsImei(
                            info = listOf(mapOf("imei" to imei.toLong())),
                            sgid = _sgid
                        )
                    )
                )
                if (res.items.isEmpty())
                    throw Exception("Failed attempt to add a device to the server")
                _device.value = Device(imei, "", "", bindingTime = 0, simei = res.items[0].simei, registrationPlate = "", deviceType = selectedType)

            } catch (e: Exception) {
                Log.e("addImei", e.toString())
                uiState.value= DefaultStates.Error(R.string.imei_not_found)
                return@launch
            }
            try {
                val res = ApiImei.retrofitService.getDetail(
                    sid = _sid,
                    RequestImei(
                        module = "device",
                        func = "GetDetail",
                        params = GetDetailParamsImei(
                            simei = _device.value.simei
                        )
                    )
                )
                val pos = Gson().fromJson(res.posString, PosData::class.java)
                pos.lat
                pos.lon
            } catch (e: Exception) {
                Log.e("addImei", e.toString())
                uiState.value = DefaultStates.Error(R.string.imei_not_found)
                return@launch
            }
            uiState.value= DefaultStates.Success
        }
    }

    private fun randomHash(length: Int = 64): String {
        val byteArray = ByteArray(length / 2) // 1 байт = 2 hex-символа
        SecureRandom().nextBytes(byteArray)
        return byteArray.joinToString("") { "%02x".format(it) }
    }

    fun insertDevice(name: String) {
        viewModelScope.launch {
            val crashlytics = Firebase.crashlytics
            crashlytics.log("Попытка привязки IMEI: ${_device.value.imei}. token: $_token. u_hash: $_hash")
            val nowTime = Instant.now().toEpochMilli()
            val id: String
            val foundDevice = _devices.value.find { it.imei == _device.value.imei && !it.isConnected }
            if (foundDevice != null) {
                updateDevice(foundDevice.copy(
                    name = name,
                    bindingTime = nowTime,
                    isConnected = true,
                    deviceType = device.value.deviceType
                ))
                if (uiState.value is DefaultStates.Error)
                    return@launch
                _device.value = foundDevice.copy(
                    name = name,
                    bindingTime = nowTime,
                    isConnected = true,
                    deviceType = device.value.deviceType
                )
            }
            else {
                val registrationPlate = _device.value.imei + randomHash()
                try {
                    val res = Api.retrofitService.addCar(
                        mapOf(
                            "token" to _token,
                            "u_hash" to _hash,
                            "debug" to "",
                            "data" to Gson().toJson(
                                Car(
                                    registrationPlate = registrationPlate,
                                    details = Details(
                                        imei = _device.value.imei,
                                        name = name,
                                        bindingTime = nowTime,
                                        typeName = _device.value.deviceType
                                    )
                                )
                            )
                       )
                    )
                    if (res.code != "200")
                        throw Exception("Code: ${res.code}, message: ${res.message}")
                    id = res.data.createdCar.cId
                } catch (e: Exception) {
                    crashlytics.log("Не удалось привязать IMEI: ${_device.value.imei}. registrationPlate: $registrationPlate. token: $_token. u_hash: $_hash Ошибка: $e")
                    crashlytics.recordException(e)
                    Log.e("addCar", e.toString())
                    uiState.value= DefaultStates.Error(R.string.server_error)
                    return@launch
                }
                val device = Device(
                    _device.value.imei,
                    id = id,
                    name = name,
                    bindingTime = nowTime,
                    simei = _device.value.simei,
                    registrationPlate = registrationPlate,
                    deviceType = selectedType
                )
                _device.value = device
                repository.insertDevice(device)
                launch {
                    _typesSignals.value = repository.getTypeSignal(_device.value.id)
                }
            }
            launch {
                repository.insertSignal(
                    Signal(
                        deviceId = _device.value.id,
                        name = "Устройство привязано",
                        dateTime = nowTime
                    )
                )
                repository.getAllDeviceSignals(_device.value.id)
                    .collect {
                        _signalsDevice.value = it
                    }
            }
            uiState.value= DefaultStates.Success
        }
    }

    /*
    fun clearDevice() {
        viewModelScope.launch {
            // repository.clearDevice()
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
            if (_devices.value.isNotEmpty()) {
                updateDevice(
                    _devices.value[0].copy(
                        typeStatus = Device.TypeStatus.RequiresRepair,
                        breakdownForecast = "После очередного Пробега в 1000 км проверить колеса",
                        maintenanceRecommendations = "Проверить колёса"
                    )
                )
                if (_devices.value.size > 1) {
                    updateDevice(
                        _devices.value[1].copy(
                            typeStatus = Device.TypeStatus.RequiresRepair,
                            breakdownForecast = "У ТС часто короткие аренды",
                            maintenanceRecommendations = "Необходимо проверить ТС"
                        )
                    )
                }
            }
        }
    }
     */

    fun getDevice(imei: String) {
        viewModelScope.launch {
            _devices.value.forEach {
                if (it.imei == imei)
                    _device.value = it
            }
        }
    }

    fun updateDevice(device: Device) {
        viewModelScope.launch {
            repository.updateDevice(device)
            try {
                val res = Api.retrofitService.updateCar(
                    device.id,
                    mapOf(
                        "token" to _token,
                        "u_hash" to _hash,
                        "data" to Gson().toJson(
                            Car(
                                registrationPlate = device.registrationPlate,
                                details = Details(
                                    imei = device.imei,
                                    name = device.name,
                                    isConnected = device.isConnected,
                                    bindingTime = device.bindingTime,
                                    typeName = device.deviceType
                                )
                            )
                        )
                    )
                )
                if (res.code != "200")
                    throw Exception("Code: ${res.code}, message: ${res.message}")
            } catch (e: Exception) {
                Log.e("updateCar", e.toString())
                uiState.value= DefaultStates.Error(R.string.server_error)
                return@launch
            }
            uiState.value= DefaultStates.Success
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
                _typesSignals.value = repository.getTypeSignal(device.id)
            }
            launch {
                repository.getAllDeviceSignals(device.id)
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
            typesSignals.map { if (it.id == typeSignal.id) typeSignal else it }
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

    fun updateUnitsDistance(it: Boolean) {
        viewModelScope.launch {
            _prefs.edit().putBoolean("unitsDistance", it).apply()

            withContext(Dispatchers.Main) {
                unitsDistance.value = it
            }
        }
    }

    fun setType(selected: String) {
        selectedString = selected
        selectedType = getType(selected)

    }

    private fun getType (selected:String) = when(selected){
        "Тип 2" -> "tracker_model2"
        "Тип 4" -> "tracker_model4"

        else -> "tracker_model2"
    }
}