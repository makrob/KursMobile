package com.example.kursmobile.domain.repository

import com.example.kursmobile.domain.models.CitySuggestion
import com.example.kursmobile.domain.models.WeatherForecast

interface WeatherRepository {
    suspend fun searchCities(query: String): List<CitySuggestion>
    suspend fun getCityCoordinates(cityName: String): CityCoordinates?
    suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherForecast
}

data class CityCoordinates(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String
)