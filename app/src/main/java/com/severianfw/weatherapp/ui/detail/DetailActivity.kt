package com.severianfw.weatherapp.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.severianfw.weatherapp.databinding.ActivityDetailBinding
import com.severianfw.weatherapp.helper.WeatherIconGenerator
import com.severianfw.weatherapp.model.CurrentWeatherResponse
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private lateinit var weatherResponse: CurrentWeatherResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val cityName = intent.getStringExtra("CITY")

        if (cityName != null) {
            detailViewModel.getCityWeather(this, cityName)
        }

        detailViewModel.cityWeather.observe(this, {
            weatherResponse = it
            binding.apply {
                tvLocationCountry.text = weatherResponse.name
                tvTime.text = getDate(weatherResponse.dt)
                icMainWeather.setImageResource(
                    WeatherIconGenerator.getWeatherDayIcon(
                        weatherResponse.weather?.get(0)?.id
                    )
                )
                tvMainWeather.text = weatherResponse.weather?.get(0)?.description
                tvTemperature.text = weatherResponse.main?.temp?.toInt().toString()
            }
        })

    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(unix: Int): String {
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy")
        val date = Date(unix.toLong() * 1000)

        return simpleDateFormat.format(date)
    }
}