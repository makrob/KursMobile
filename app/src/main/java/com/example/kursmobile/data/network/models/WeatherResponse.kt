package com.example.kursmobile.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val daily: DailyData
)

@Serializable
data class DailyData(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val temperature_2m_mean: List<Double>
)