package com.severianfw.weatherapp.ui

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.severianfw.weatherapp.model.CurrentWeatherResponse
import com.severianfw.weatherapp.model.HourlyItem
import com.severianfw.weatherapp.model.HourlyWeatherResponse
import com.severianfw.weatherapp.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainViewModel : ViewModel() {
    private val _hourlyList = MutableLiveData<List<HourlyItem>>()
    val hourlyList: LiveData<List<HourlyItem>> = _hourlyList

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double> = _longitude

    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> = _latitude

    private var testLat: Double = 0.1
    private var testLong: Double = 0.1

    private val _cityName = MutableLiveData<String>()
    val cityName: LiveData<String> = _cityName

    private val _countryName = MutableLiveData<String>()
    val countryName: LiveData<String> = _countryName

    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
    val currentWeather: LiveData<CurrentWeatherResponse> = _currentWeather

    companion object {
        private const val TAG = "MainViewModel"
        private const val APP_ID = "f616a171e894a0f88d0588d09b637097"
        private const val UNITS = "metric"
    }

    fun getHourlyWeather(latitude: String, longitude: String) {
        val client =
            ApiConfig.getApiService().getHourlyWeather(latitude, longitude, "7", UNITS, APP_ID)
        client.enqueue(object : Callback<HourlyWeatherResponse> {
            override fun onResponse(
                call: Call<HourlyWeatherResponse>,
                responseHourly: Response<HourlyWeatherResponse>
            ) {
                if (responseHourly.isSuccessful) {
                    _hourlyList.value = responseHourly.body()?.hourly
                } else {
                    Log.e(TAG, "onFailure: ${responseHourly.message()}")
                }
            }

            override fun onFailure(call: Call<HourlyWeatherResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getMainWeather(latitude: String, longitude: String) {
        val client = ApiConfig.getApiService().getMainWeather(latitude, longitude, UNITS, APP_ID)
        client.enqueue(object : Callback<CurrentWeatherResponse> {
            override fun onResponse(
                call: Call<CurrentWeatherResponse>,
                response: Response<CurrentWeatherResponse>
            ) {
                if (response.isSuccessful) {
                    _currentWeather.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
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
                Log.e("TESTING", "${it.longitude} ${it.latitude}")

                _latitude.value = it.latitude
                _longitude.value = it.longitude
            } else {
                Toast.makeText(context, "Unable to find location!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun getCityName(context: FragmentActivity, latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            val address = geocoder.getFromLocation(latitude, longitude, 1)
            _cityName.value = address[0].locality
        } catch (e: Exception) {
            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
        }

    }

    fun getCountryName(context: FragmentActivity, latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            val address = geocoder.getFromLocation(latitude, longitude, 1)
            _countryName.value = address[0].countryName
        } catch (e: Exception) {
            Toast.makeText(context, "$e", Toast.LENGTH_SHORT).show()
        }
    }

}