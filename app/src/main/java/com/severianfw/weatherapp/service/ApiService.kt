package com.severianfw.weatherapp.service

import com.severianfw.weatherapp.model.CurrentWeatherResponse
import com.severianfw.weatherapp.model.HourlyWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/forecast")
    fun getHourlyWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("cnt") count: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Call<HourlyWeatherResponse>

    @GET("data/2.5/weather")
    fun getMainWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Call<CurrentWeatherResponse>

}