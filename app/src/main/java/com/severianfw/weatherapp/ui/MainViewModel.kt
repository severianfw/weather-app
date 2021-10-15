package com.severianfw.weatherapp.ui

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
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

    fun fetchLocation(activity: Activity) {
        // Check location permission
        if (ActivityCompat.checkSelfPermission(
                activity.baseContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                activity.baseContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }

        // Get Location
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity.baseContext)
        val task = fusedLocationProviderClient.lastLocation

        task.addOnSuccessListener {
            if (it != null) {
                Toast.makeText(activity, "${it.longitude} ${it.latitude}", Toast.LENGTH_SHORT)
                    .show()
                _latitude.value = it.latitude
                _longitude.value = it.longitude

                getCityName(activity, it.latitude, it.longitude)
                getCountryName(activity, it.latitude, it.longitude)
            } else {
                Toast.makeText(activity, "Unable to find location!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getCityName(activity: Activity, latitude: Double, longitude: Double) {
        val geocoder = Geocoder(activity, Locale.getDefault())
        val address = geocoder.getFromLocation(latitude, longitude, 1)

        _cityName.value = address[0].locality
    }

    private fun getCountryName(activity: Activity, latitude: Double, longitude: Double) {
        val geocoder = Geocoder(activity, Locale.getDefault())
        val address = geocoder.getFromLocation(latitude, longitude, 1)

        _countryName.value = address[0].countryName
    }

}