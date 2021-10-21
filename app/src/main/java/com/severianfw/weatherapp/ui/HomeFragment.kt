package com.severianfw.weatherapp.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.severianfw.weatherapp.databinding.FragmentHomeBinding
import com.severianfw.weatherapp.helper.WeatherIconGenerator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainViewModel: MainViewModel

    var latitude by Delegates.notNull<Double>()
    var longitude by Delegates.notNull<Double>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        mainViewModel = MainViewModel()
        binding.rvHourlyWeather.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Get City Name
        mainViewModel.getCityName(requireActivity(), latitude, longitude)
        mainViewModel.getCountryName(requireActivity(), latitude, longitude)

        // Set city & country name
        mainViewModel.cityName.observe(viewLifecycleOwner, {
            binding.tvLocationCity.text = "$it, "
        })
        mainViewModel.countryName.observe(viewLifecycleOwner, {
            binding.tvLocationCountry.text = it
        })

        // Get hourly & main weather
        mainViewModel.getHourlyWeather(latitude.toString(), longitude.toString())
        mainViewModel.getMainWeather(latitude.toString(), longitude.toString())

        // Set weather data to view
        mainViewModel.currentWeather.observe(viewLifecycleOwner, {
            val weatherIcon = WeatherIconGenerator.getWeatherDayIcon(it.weather?.get(0)?.id)

            binding.apply {
                tvTemperature.text = it.main?.temp?.toInt().toString()
                tvMainWeather.text = it.weather?.get(0)?.description
                icMainWeather.setImageResource(weatherIcon)
                tvDate.text = getDate(it.dt)
            }
        })
        mainViewModel.hourlyList.observe(viewLifecycleOwner, {
            binding.rvHourlyWeather.adapter = HourlyListAdapter(it)
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val nightModeFlags = context?.resources?.configuration?.uiMode

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