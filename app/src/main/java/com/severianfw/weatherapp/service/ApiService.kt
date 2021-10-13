package com.severianfw.weatherapp.service

import com.severianfw.weatherapp.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/forecast")
    fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") appId: String
    ): Call<WeatherResponse>

}