package com.example.geoblinker.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class LocationHelper(
    private val context: Context,
    private val onLocationUpdate: (LatLng) -> Unit
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun getLastLocation() {
        if (checkPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        onLocationUpdate(latLng)
                    }
                }
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}