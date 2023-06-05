package com.nguyen.jpmorgan.model

interface Repository {
    suspend fun fetchWeather(location: String): Record
}