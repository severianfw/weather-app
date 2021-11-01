package com.severianfw.weatherapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.gms.location.*
import com.severianfw.weatherapp.R
import com.severianfw.weatherapp.databinding.ActivityMainBinding
import com.severianfw.weatherapp.ui.home.HomeFragment
import com.severianfw.weatherapp.ui.main.MainViewModel.Companion.LATITUDE
import com.severianfw.weatherapp.ui.main.MainViewModel.Companion.LONGITUDE
import com.severianfw.weatherapp.ui.search.SearchFragment
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Disable Dark Theme (Only shows light theme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Get Location
        mainViewModel.fetchLocation(this)

        mainViewModel.latLongBundle.observe(this, {
            val bundle = it
            if (!it.isEmpty){
                latitude = bundle.getDouble(LATITUDE)
                longitude = bundle.getDouble(LONGITUDE)

                supportFragmentManager.commit {
                    replace(binding.fragmentContainer.id, HomeFragment.newInstance(latitude, longitude))
                }
            }
        })

        binding.bottomNav.setOnItemSelectedListener {
            val fragmentCount = supportFragmentManager.backStackEntryCount
            if (fragmentCount > 0) {
                supportFragmentManager.popBackStack()
            }

            var fragment: Fragment? = null
            when (it.itemId) {
                R.id.nav_home -> {
                    fragment = HomeFragment.newInstance(latitude, longitude)
                }
                R.id.nav_search -> {
                    fragment = SearchFragment()
                }
            }

            supportFragmentManager.commit {
                if (fragment != null) {
                    replace(binding.fragmentContainer.id, fragment)
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        val fragmentCount = supportFragmentManager.backStackEntryCount

        if (fragmentCount == 0){
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

}