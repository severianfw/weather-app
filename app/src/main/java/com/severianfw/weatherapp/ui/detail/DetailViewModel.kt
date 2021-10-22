package com.severianfw.weatherapp.ui.detail

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.severianfw.weatherapp.model.CurrentWeatherResponse
import com.severianfw.weatherapp.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _cityWeather = MutableLiveData<CurrentWeatherResponse>()
    var cityWeather: LiveData<CurrentWeatherResponse> = _cityWeather

    companion object {
        private const val TAG = "SearchViewModel"
        private const val APP_ID = "f616a171e894a0f88d0588d09b637097"
        private const val UNITS = "metric"
    }

    fun getCityWeather(context: Context, cityName: String) {
        val client = ApiConfig.getApiService().getWeatherByCityName(cityName, UNITS, APP_ID)
        client.enqueue(object : Callback<CurrentWeatherResponse> {
            override fun onResponse(
                call: Call<CurrentWeatherResponse>,
                response: Response<CurrentWeatherResponse>
            ) {
                if (response.isSuccessful) {
                    _cityWeather.value = response.body()
                } else {
                    Toast.makeText(context, "${response.message()}!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                Toast.makeText(context, "onFailure: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

}