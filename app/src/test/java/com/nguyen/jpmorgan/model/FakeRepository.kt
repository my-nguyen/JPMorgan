package com.nguyen.jpmorgan.model

import retrofit2.Call
import retrofit2.Callback

class FakeRepository: Repository {
    override fun fetchWeather(location: String): Call<Record> {
        val city = City("London")
        val day1 = Day(temp = Temperature(63.5F, 48.49F, 69.6F), weather = listOf(Weather(main = "Clouds", description = "broken clouds")))
        val day2 = Day(temp = Temperature(58.84F, 48.94F, 68.4F), weather = listOf(Weather(main = "Clouds", description = "overcast clouds")))
        val day3 = Day(temp = Temperature(67.62F, 47.16F, 70.07F), weather = listOf(Weather(main = "Clear", description = "sky is clear")))
        val day4 = Day(temp = Temperature(69.42F, 48.04F, 69.42F), weather = listOf(Weather(main = "Clear", description = "sky is clear")))
        val day5 = Day(temp = Temperature(72.07F, 51.06F, 72.07F), weather = listOf(Weather(main = "Rain", description = "light rain")))
        val day6 = Day(temp = Temperature(81.28F, 59.88F, 81.55F), weather = listOf(Weather(main = "Rain", description = "light rain")))
        val day7 = Day(temp = Temperature(81.28F, 63.5F, 81.28F), weather = listOf(Weather(main = "Rain", description = "light rain")))
        val days = listOf(day1, day2, day3, day4, day5, day6, day7)
        return Callback<Record(city, days)>()
    }
}