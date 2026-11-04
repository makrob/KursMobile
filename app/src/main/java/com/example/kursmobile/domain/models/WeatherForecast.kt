package com.example.kursmobile.domain.models

data class WeatherForecast(
    val cityName: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val dailyForecasts: List<DailyForecast>
)

data class DailyForecast(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val avgTemp: Double
)