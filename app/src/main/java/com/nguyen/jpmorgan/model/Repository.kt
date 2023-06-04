package com.nguyen.jpmorgan.model

import retrofit2.Call

interface Repository {
    fun fetchWeather(location: String): Call<Record>
}