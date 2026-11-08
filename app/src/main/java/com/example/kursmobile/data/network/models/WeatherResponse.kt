package com.example.kursmobile.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val location: WeatherLocation,
    val forecast: WeatherForecastData
)

@Serializable
data class WeatherLocation(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double
)

@Serializable
data class WeatherForecastData(
    @SerialName("forecastday")
    val forecastDay: List<ForecastDay>
)

@Serializable
data class ForecastDay(
    val date: String,
    val day: DayData
)

@Serializable
data class DayData(
    @SerialName("maxtemp_c")
    val maxTempC: Double,
    @SerialName("mintemp_c")
    val minTempC: Double,
    @SerialName("avgtemp_c")
    val avgTempC: Double
)