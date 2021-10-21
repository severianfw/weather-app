package com.severianfw.weatherapp.ui.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import com.google.android.gms.location.*
import com.severianfw.weatherapp.R
import com.severianfw.weatherapp.databinding.ActivityMainBinding
import com.severianfw.weatherapp.ui.home.HomeFragment
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Get Location
        mainViewModel.fetchLocation(this)
        mainViewModel.longitude.observe(this, {
            longitude = it
        })
        mainViewModel.latitude.observe(this, {
            latitude = it
        })

        binding.bottomNav.setOnItemSelectedListener {
            var fragment: Fragment? = null
            when (it.itemId) {
                R.id.nav_home -> {
                    fragment = HomeFragment()
                    fragment.latitude = latitude
                    fragment.longitude = longitude
                }
            }

            supportFragmentManager.commitNow {
                if (fragment != null) {
                    replace(binding.fragmentContainer.id, fragment)
                }
            }
            return@setOnItemSelectedListener true
        }
    }

}