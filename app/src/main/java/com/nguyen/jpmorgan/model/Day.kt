package com.nguyen.jpmorgan.model

data class Day(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Temperature,
    val pressure: Int,
    val humidity: Int,
    val weather: List<Weather>,
    val speed: Float
): java.io.Serializable
