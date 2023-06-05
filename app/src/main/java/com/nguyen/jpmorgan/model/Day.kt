package com.nguyen.jpmorgan.model

data class Day(
    val dt: Long = 0L,
    val sunrise: Long = 0L,
    val sunset: Long = 0L,
    val temp: Temperature,
    val pressure: Int = 0,
    val humidity: Int = 0,
    val weather: List<Weather>,
    val speed: Float = 0F
): java.io.Serializable
