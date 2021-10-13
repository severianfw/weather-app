package com.severianfw.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.severianfw.weatherapp.R
import com.severianfw.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.rvHourlyWeather.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

        binding.apply {
            tvDate.text = "22 Oct 2021"
            tvLocationCountry.text = "Indonesia"
            tvLocationCity.text = "Jakarta, "
            tvMainWeather.text = "Rainy Day"
            tvTemperature.text = "30"
        }

        mainViewModel.getHourlyWeather("Jakarta")

        mainViewModel.hourlyList.observe(this, {
            binding.rvHourlyWeather.adapter = HourlyListAdapter(it)
        })
    }
}