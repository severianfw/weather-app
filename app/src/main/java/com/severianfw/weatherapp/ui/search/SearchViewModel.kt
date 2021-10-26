package com.severianfw.weatherapp.ui.search

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.severianfw.weatherapp.model.CurrentWeatherResponse
import com.severianfw.weatherapp.service.ApiConfig
import retrofit2.*

class SearchViewModel : ViewModel() {

    private val _listLocationWeather = MutableLiveData<MutableList<CurrentWeatherResponse>>()
    val listLocationWeather: LiveData<MutableList<CurrentWeatherResponse>> = _listLocationWeather

    companion object {
        private const val TAG = "SearchViewModel"
        private const val APP_ID = "f616a171e894a0f88d0588d09b637097"
        private const val UNITS = "metric"

        // Remote Config keys
        private const val CITY_NAME_1 = "city_name_1"
        private const val CITY_NAME_2 = "city_name_2"
        private const val CITY_NAME_3 = "city_name_3"
        private const val CITY_NAME_4 = "city_name_4"
        private const val CITY_NAME_5 = "city_name_5"
        private const val CITY_NAME_6 = "city_name_6"

        private val DEFAULTS: HashMap<String, Any> =
            hashMapOf(
                CITY_NAME_1 to "Paris",
                CITY_NAME_2 to "London",
                CITY_NAME_3 to "Madrid",
                CITY_NAME_4 to "Rio",
                CITY_NAME_5 to "Manchester",
                CITY_NAME_6 to "London",
            )
    }

    fun getPopularLocation(context: Context): FirebaseRemoteConfig {
        val remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }


        remoteConfig.apply {
            setDefaultsAsync(DEFAULTS)
            setConfigSettingsAsync(configSettings)
            fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Fetch and activate succeeded!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Fetch failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return remoteConfig
    }

    fun getWeather(remoteConfig: FirebaseRemoteConfig) = liveData {
        val result = getLocationWeatherList(remoteConfig)
        emit(result)
    }

    private suspend fun getLocationWeatherList(remoteConfig: FirebaseRemoteConfig): MutableList<CurrentWeatherResponse> {
        val locationList = mutableListOf<CurrentWeatherResponse>()

        for (item in remoteConfig.all) {
            getLocationWeather(item.value.asString())?.let { locationList.add(it) }
        }

        return locationList
    }

    private suspend fun getLocationWeather(cityName: String): CurrentWeatherResponse? {
        val client = ApiConfig.getApiService().getWeatherByCityName(cityName, UNITS, APP_ID)
        var locationResponse: CurrentWeatherResponse? = null

        val response = client.awaitResponse()
        if (response.isSuccessful) {
            locationResponse = response.body()
        }
        return locationResponse
    }


}