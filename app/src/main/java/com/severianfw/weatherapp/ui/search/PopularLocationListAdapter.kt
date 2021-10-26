package com.severianfw.weatherapp.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.severianfw.weatherapp.databinding.ItemPopularLocationBinding
import com.severianfw.weatherapp.helper.WeatherIconGenerator
import com.severianfw.weatherapp.model.CurrentWeatherResponse

class PopularLocationListAdapter(private val listPopularLocation: MutableList<CurrentWeatherResponse>) :
    RecyclerView.Adapter<PopularLocationListAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val itemPopularLocationBinding: ItemPopularLocationBinding) :
        RecyclerView.ViewHolder(itemPopularLocationBinding.root) {

            fun bind(item: CurrentWeatherResponse) {
                itemPopularLocationBinding.apply {
                    tvLocation.text = item.name
                    tvLocationWeather.text = item.weather?.get(0)?.description
                    tvPopularTemperature.text = item.main?.temp?.toInt().toString()

                    val weatherIcon = WeatherIconGenerator.getWeatherDayIcon(item.id)
                    Glide.with(itemPopularLocationBinding.root)
                        .load(weatherIcon)
                        .into(ivPopularWeather)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemPopularLocationBinding =
            ItemPopularLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemPopularLocationBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listPopularLocation[position])
    }

    override fun getItemCount(): Int = listPopularLocation.size

}