package com.severianfw.weatherapp.ui.home

import android.util.Log
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
            // Take time (HH.MM) from YYYY-10-13 HH:MM:SS
            var time = item.dtTxt
            time = time?.substring(11, 16)
            time = time?.replace(':', '.')

            Log.e("TESTING", item.temperatureDetail.toString())
            itemHourForecastBinding.tvHourTemperature.text = item.temperatureDetail?.temp?.toInt().toString()
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
        Log.e("POSITION", position.toString())
    }

    override fun getItemCount(): Int = listHourlyItem.size

}