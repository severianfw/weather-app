package com.severianfw.weatherapp.ui.search

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.severianfw.weatherapp.R
import com.severianfw.weatherapp.databinding.FragmentSearchBinding
import com.severianfw.weatherapp.ui.detail.DetailFragment

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.searchBarCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val fragment = DetailFragment.newInstance(query)
                    closeKeyboard()

                    parentFragmentManager.commit {
                        addToBackStack("main")
                        replace(R.id.fragment_container, fragment)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        return binding.root
    }

    private fun closeKeyboard() {
        val view = view?.rootView?.windowToken

        var imm: InputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view, 0)
    }
}