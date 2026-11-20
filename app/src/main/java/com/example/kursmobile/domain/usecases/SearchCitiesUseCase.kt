package com.example.kursmobile.domain.usecases

import com.example.kursmobile.domain.models.CitySuggestion
import com.example.kursmobile.domain.repository.WeatherRepository

class SearchCitiesUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(query: String): List<CitySuggestion> {
        return if (query.length >= 2) {
            repository.searchCities(query)
        } else {
            emptyList()
        }
    }
}