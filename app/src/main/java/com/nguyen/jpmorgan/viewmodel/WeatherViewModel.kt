package com.nguyen.jpmorgan.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nguyen.jpmorgan.model.Record
import com.nguyen.jpmorgan.model.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val PREFERENCE_NAME = "SharedPreferences"
private const val PREFERENCE_KEY = "location"
private const val TAG = "WeatherViewModel"

// 2 params:
// 1. application of type Application and not MyApplication, so ViewModelTest can instantiate a
//    WeatherViewModel with ApplicationProvider.getApplicationContext(). application is necessary
//    for WeatherViewModel to derive from AndroidViewModel and for use in calling
//    getApplication<Application>().applicationContext
// 2. repository
class WeatherViewModel(application: Application, private val repository: Repository) : AndroidViewModel(application) {
    val record = MutableLiveData<Record>()
    val location = fetchLocation()

    fun fetchWeather(location: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.fetchWeather(location)
            withContext(Dispatchers.Main) {
                record.value = result
            }
        }
    }

    fun saveLocation(city: String) {
        val preferences = getApplication<Application>().applicationContext.getSharedPreferences(
            PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        preferences.putString(PREFERENCE_KEY, city)
        preferences.apply()
    }

    private fun fetchLocation(): String? {
        val preferences = getApplication<Application>().applicationContext.getSharedPreferences(
            PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getString(PREFERENCE_KEY, null)
    }
}

class WeatherViewModelFactory(private val application: Application, private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}