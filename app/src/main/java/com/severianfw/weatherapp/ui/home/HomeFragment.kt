package com.severianfw.weatherapp.ui.home

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.severianfw.weatherapp.databinding.FragmentHomeBinding
import com.severianfw.weatherapp.helper.DateConverter
import com.severianfw.weatherapp.helper.WeatherIconGenerator
import kotlin.properties.Delegates

private const val LATITUDE = "latitude"
private const val LONGITUDE = "longitude"

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()

    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble(LATITUDE)
            longitude = it.getDouble(LONGITUDE)
        }
    }

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
                tvDate.text = DateConverter.unixToDate(it.dt)
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

    companion object {
        @JvmStatic
        fun newInstance(latitude: Double, longitude: Double) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putDouble(LATITUDE, latitude)
                    putDouble(LONGITUDE, longitude)
                }
            }
    }

}