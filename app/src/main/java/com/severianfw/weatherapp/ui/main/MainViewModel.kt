package com.severianfw.weatherapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainViewModel : ViewModel() {

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double> = _longitude

    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> = _latitude

    private var testLat: Double = 0.1
    private var testLong: Double = 0.1

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun fetchLocation(context: Context) {
        // Check location permission
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        // Get Location
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        val task = fusedLocationProviderClient.lastLocation

        task.addOnSuccessListener {
            if (it != null) {
                Toast.makeText(context, "${it.longitude} ${it.latitude}", Toast.LENGTH_SHORT)
                    .show()
                Log.e(TAG, "${it.longitude} ${it.latitude}")

                _latitude.value = it.latitude
                _longitude.value = it.longitude
            } else {
                Toast.makeText(context, "Unable to find location!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}