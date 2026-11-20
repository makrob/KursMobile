package com.example.kursmobile.domain.usecases

import com.example.kursmobile.domain.repository.WeatherRepository
import com.example.kursmobile.domain.repository.CityCoordinates

class GetCityCoordinatesUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityName: String): CityCoordinates? {
        return repository.getCityCoordinates(cityName)
    }
}