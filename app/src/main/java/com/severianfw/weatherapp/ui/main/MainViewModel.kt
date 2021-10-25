package com.severianfw.weatherapp.ui.main

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainViewModel : ViewModel() {

    private var _latLongBundle = MutableLiveData<Bundle>()
    val latLongBundle: LiveData<Bundle> = _latLongBundle

    companion object {
        private const val TAG = "MainViewModel"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
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
//                Log.e(TAG, "${it.longitude} ${it.latitude}")

                val newBundle = Bundle()
                newBundle.putDouble(LATITUDE, it.latitude)
                newBundle.putDouble(LONGITUDE, it.longitude)

                _latLongBundle.value = newBundle
            } else {
                Toast.makeText(context, "Unable to find location!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}