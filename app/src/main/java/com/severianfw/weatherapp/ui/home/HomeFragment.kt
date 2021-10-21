package com.severianfw.weatherapp.ui.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.severianfw.weatherapp.databinding.FragmentHomeBinding
import com.severianfw.weatherapp.helper.WeatherIconGenerator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()


    var latitude by Delegates.notNull<Double>()
    var longitude by Delegates.notNull<Double>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.rvHourlyWeather.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Get City Name
        homeViewModel.getCityName(requireActivity(), latitude, longitude)
        homeViewModel.getCountryName(requireActivity(), latitude, longitude)

        // Set city & country name
        homeViewModel.cityName.observe(viewLifecycleOwner, {
            binding.tvLocationCity.text = "$it, "
        })
        homeViewModel.countryName.observe(viewLifecycleOwner, {
            binding.tvLocationCountry.text = it
        })

        // Get hourly & main weather
        homeViewModel.getHourlyWeather(latitude.toString(), longitude.toString())
        homeViewModel.getMainWeather(latitude.toString(), longitude.toString())

        // Set weather data to view
        homeViewModel.currentWeather.observe(viewLifecycleOwner, {
            val weatherIcon = WeatherIconGenerator.getWeatherDayIcon(it.weather?.get(0)?.id)

            binding.apply {
                tvTemperature.text = it.main?.temp?.toInt().toString()
                tvMainWeather.text = it.weather?.get(0)?.description
                icMainWeather.setImageResource(weatherIcon)
                tvDate.text = getDate(it.dt)
            }
        })
        homeViewModel.hourlyList.observe(viewLifecycleOwner, {
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