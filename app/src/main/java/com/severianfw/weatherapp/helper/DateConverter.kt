package com.severianfw.weatherapp.helper

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun unixToDate(unix: Int): String {
            val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy")
            val date = Date(unix.toLong() * 1000)

            return simpleDateFormat.format(date)
        }
    }
}