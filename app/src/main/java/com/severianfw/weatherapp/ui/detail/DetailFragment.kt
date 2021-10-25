package com.severianfw.weatherapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.severianfw.weatherapp.databinding.FragmentDetailBinding
import com.severianfw.weatherapp.helper.DateConverter
import com.severianfw.weatherapp.helper.WeatherIconGenerator
import com.severianfw.weatherapp.model.CurrentWeatherResponse

private const val CITY_NAME = "city_name"

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private lateinit var weatherResponse: CurrentWeatherResponse

    private var cityName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cityName = it.getString(CITY_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        if (cityName != null) {
            detailViewModel.getCityWeather(requireContext(), cityName!!)
        }

        detailViewModel.cityWeather.observe(viewLifecycleOwner, {
            weatherResponse = it
            binding.apply {
                tvLocationCountry.text = weatherResponse.name
                tvTime.text = DateConverter.unixToDate(weatherResponse.dt)
                icMainWeather.setImageResource(
                    WeatherIconGenerator.getWeatherDayIcon(
                        weatherResponse.weather?.get(0)?.id
                    )
                )
                tvMainWeather.text = weatherResponse.weather?.get(0)?.description
                tvTemperature.text = weatherResponse.main?.temp?.toInt().toString()
            }
        })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(cityName: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(CITY_NAME, cityName)
                }
            }
    }
}