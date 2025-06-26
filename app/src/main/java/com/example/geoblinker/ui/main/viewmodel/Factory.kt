package com.example.geoblinker.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geoblinker.data.Repository

class DeviceViewModelFactory(
    private val repository: Repository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DeviceViewModel(repository, application) as T
    }
}