package com.severianfw.weatherapp.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.severianfw.weatherapp.databinding.ActivityMainBinding
import com.severianfw.weatherapp.helper.WeatherIconGenerator
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    private var latitude: Double = 0.1
    private var longitude: Double = 0.1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.rvHourlyWeather.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

        // Get Location
        mainViewModel.fetchLocation(this)
        mainViewModel.longitude.observe(this, {
            latitude = it
        })
        mainViewModel.latitude.observe(this, {
            longitude = it
        })

        // Set city & country name
        mainViewModel.cityName.observe(this, {
            binding.tvLocationCity.text = "$it, "
        })
        mainViewModel.countryName.observe(this, {
            binding.tvLocationCountry.text = it
        })

        // Get hourly & main weather
        mainViewModel.getHourlyWeather(latitude.toString(), longitude.toString())
        mainViewModel.getMainWeather(latitude.toString(), longitude.toString())

        // Set weather data to view
        mainViewModel.currentWeather.observe(this, {
            val weatherIcon = WeatherIconGenerator.getWeatherDayIcon(it.weather?.get(0)?.id)

            binding.apply {
                tvTemperature.text = it.main?.temp?.toInt().toString()
                tvMainWeather.text = it.weather?.get(0)?.description
                icMainWeather.setImageResource(weatherIcon)
                tvDate.text = getDate(it.dt)
            }
        })
        mainViewModel.hourlyList.observe(this, {
            binding.rvHourlyWeather.adapter = HourlyListAdapter(it)
        })
    }

    override fun onStart() {
        super.onStart()
        val nightModeFlags = baseContext.resources.configuration.uiMode

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES + 1) {
            binding.icLocation.setImageResource(com.severianfw.weatherapp.R.drawable.ic_location_white)
        } else {
            binding.icLocation.setImageResource(com.severianfw.weatherapp.R.drawable.ic_location_black)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(unix: Int): String {
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy")
        val date = Date(unix.toLong() * 1000)

        return simpleDateFormat.format(date)
    }
}