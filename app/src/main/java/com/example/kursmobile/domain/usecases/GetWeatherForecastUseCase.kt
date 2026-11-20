package com.example.kursmobile.domain.usecases

import com.example.kursmobile.domain.models.WeatherForecast
import com.example.kursmobile.domain.repository.WeatherRepository

class GetWeatherForecastUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): WeatherForecast {
        return repository.getWeatherForecast(latitude, longitude)
    }
}