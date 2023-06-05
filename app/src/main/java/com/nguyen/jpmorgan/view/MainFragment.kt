package com.nguyen.jpmorgan.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyen.jpmorgan.*
import com.nguyen.jpmorgan.databinding.FragmentMainBinding
import com.nguyen.jpmorgan.model.Day
import com.nguyen.jpmorgan.model.ServiceLocator
import com.nguyen.jpmorgan.viewmodel.DaysAdapter
import com.nguyen.jpmorgan.viewmodel.WeatherViewModel
import com.nguyen.jpmorgan.viewmodel.WeatherViewModelFactory
import kotlin.math.roundToInt

class MainFragment: Fragment(R.layout.fragment_main) {
    private val viewModel by viewModels<WeatherViewModel> {
        WeatherViewModelFactory(requireActivity().application, ServiceLocator.provideRepository())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)

        val days = mutableListOf<Day>()
        val adapter = DaysAdapter(days)
        binding.recycler.adapter = adapter
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.recycler.layoutManager = layoutManager

        if (viewModel.location != null) {
            viewModel.fetchWeather(viewModel.location!!)
        } else {
            binding.root.visibility = View.INVISIBLE
            AlertDialog.Builder(context)
                .setTitle("Enter location")
                .setMessage("Please use the menu search button to enter a location")
                .setPositiveButton("OK", null)
                .show()
        }

        viewModel.record.observe(viewLifecycleOwner) { record ->
            binding.root.visibility = View.VISIBLE
            if (record == null) {
                Toast.makeText(context, "Please enter a well-known location, e.g. London", Toast.LENGTH_LONG).show()
            } else {
                // display the temperature of today
                binding.location.text = record.city.name
                val today = record.list[0]
                binding.condition.text = today.weather[0].main
                binding.temperature.text = java.lang.String.valueOf(today.temp.day.roundToInt())
                val max = today.temp.max.roundToInt()
                val min = today.temp.min.roundToInt()
                binding.lowHigh.text = "Low $min\u00B0 - High $max\u00B0"

                // display the temperature of the next 16 days in a RecyclerView
                days.clear()
                days.addAll(record.list)
                adapter.notifyDataSetChanged()

                // save the queried city
                viewModel.saveLocation(record.city.name)
            }
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // get a hold of the search menu item
                menuInflater.inflate(R.menu.menu_main, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                // listen for the search query
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(location: String): Boolean {
                        // reset the search menu item
                        searchView.clearFocus()
                        searchView.setQuery("", false)
                        searchView.isIconified = true
                        searchItem.collapseActionView()
                        viewModel.fetchWeather(location)
                        hideKeyboard()
                        return true
                    }

                    override fun onQueryTextChange(s: String) = false
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem) = false
        })
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}