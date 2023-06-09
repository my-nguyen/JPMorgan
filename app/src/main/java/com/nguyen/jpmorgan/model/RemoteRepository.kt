package com.nguyen.jpmorgan.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/forecast/"
private const val API_KEY = "a0e4b2727858f8dc3bdc0428ef7e3712"
private const val IMPERIAL_UNITS = "imperial"
private const val TAG = "RemoteRepository"

// new way of creating a Repository, by using lazy
class RemoteRepository : Repository {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val service: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }

    override suspend fun fetchWeather(location: String) = service.fetchWeather(location, IMPERIAL_UNITS, 16, API_KEY)
}