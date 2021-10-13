package com.severianfw.weatherapp.helper

import android.util.Log
import com.severianfw.weatherapp.R

class WeatherIconGenerator {
    companion object {
        fun getWeatherDayIcon(weatherID: Int?): Int {
            if (weatherID != null) {
                val str = weatherID.toString()
                Log.e("WEATHER", str)
                when {
                    str.startsWith('2') -> {
                        return R.drawable.thunderstorm
                    }
                    str.startsWith('3') && weatherID == 300 -> {
                        return R.drawable.rainy_day
                    }
                    str.startsWith('3') -> {
                        return R.drawable.rain
                    }
                    str.startsWith('5') -> {
                        return R.drawable.rainy_day
                    }
                    str.startsWith('5') && weatherID >= 504 -> {
                        return R.drawable.heavy_rain
                    }
                    str.startsWith('7') -> {

                    }
                    str.startsWith('8') && weatherID == 800 -> {
                        return R.drawable.sun
                    }
                    str.startsWith('8') -> {
                        return R.drawable.mostly_cloud
                    }
                }
            }
            return R.drawable.mostly_cloudy
        }
    }
}