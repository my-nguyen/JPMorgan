package com.nguyen.jpmorgan.model

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("daily")
    suspend fun fetchWeather(@Query("q") zipcode: String, @Query("units") units: String, @Query("cnt") numDays: Int, @Query("appid") apiKey: String): Record
}