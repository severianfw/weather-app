package com.severianfw.weatherapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.severianfw.weatherapp.model.HourlyItem
import com.severianfw.weatherapp.model.WeatherResponse
import com.severianfw.weatherapp.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _hourlyList = MutableLiveData<List<HourlyItem>>()
    val hourlyList: LiveData<List<HourlyItem>> = _hourlyList

    companion object {
        private const val TAG = "MainViewModel"
        private const val APP_ID = "f616a171e894a0f88d0588d09b637097"
        var CITY_NAME = "Jakarta"
    }

    fun getHourlyWeather(cityName: String) {
        val client = ApiConfig.getApiService().getWeather(cityName, APP_ID)
        client.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    _hourlyList.value = response.body()?.hourly
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

}