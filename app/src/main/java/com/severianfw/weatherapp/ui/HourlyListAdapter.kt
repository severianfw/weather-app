package com.severianfw.weatherapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.severianfw.weatherapp.databinding.ItemHourForecastBinding
import com.severianfw.weatherapp.helper.WeatherIconGenerator
import com.severianfw.weatherapp.model.HourlyItem

class HourlyListAdapter(private val listHourlyItem: List<HourlyItem>) :
    RecyclerView.Adapter<HourlyListAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val itemHourForecastBinding: ItemHourForecastBinding) :
        RecyclerView.ViewHolder(itemHourForecastBinding.root) {

        fun bind(item: HourlyItem) {
            // Covert temperature from kelvin to Celsius
            var temp = item.temperatureDetail?.temp
            temp = temp?.minus(273.15)

            // Take time (HH.MM) from YYYY-10-13 HH:MM:SS
            var time = item.dtTxt
            time = time?.substring(11, 16)
            time = time?.replace(':', '.')

            itemHourForecastBinding.tvHourTemperature.text = temp?.toInt().toString()
            itemHourForecastBinding.tvTime.text = time.toString()

            val weatherIcon = WeatherIconGenerator.getWeatherDayIcon(item.weather?.get(0)?.id)
            Glide.with(itemHourForecastBinding.root)
                .load(weatherIcon)
                .into(itemHourForecastBinding.icHourWeather)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemHourForecastBinding =
            ItemHourForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemHourForecastBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listHourlyItem[position])
    }

    override fun getItemCount(): Int = listHourlyItem.size

}