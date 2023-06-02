package com.nguyen.jpmorgan.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nguyen.jpmorgan.MyApplication
import com.nguyen.jpmorgan.model.Record
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PREFERENCE_NAME = "SharedPreferences"
private const val PREFERENCE_KEY = "location"
private const val TAG = "WeatherViewModel"

class WeatherViewModel(private val application: MyApplication) : AndroidViewModel(application) {
    val record = MutableLiveData<Record>()
    val location = fetchLocation()

    fun fetchWeather(location: String) {
        application.repository.fetchWeather(location).enqueue(object : Callback<Record> {
            override fun onResponse(call: Call<Record>, response: Response<Record>) {
                Log.d(TAG, "onResponse $response")
                if (response.body() == null) {
                    Log.w(TAG, "Invalid response from openweathermap.org")
                } else {
                    record.value = response.body()
                }
            }

            override fun onFailure(call: Call<Record>, t: Throwable) {
                Log.e(TAG, "onFailure $t")
            }
        })
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

class WeatherViewModelFactory(private val application: MyApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}