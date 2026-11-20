package com.example.kursmobile.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursmobile.R
import com.example.kursmobile.data.datastore.PreferencesDataStore
import com.example.kursmobile.domain.models.CitySuggestion
import com.example.kursmobile.domain.models.WeatherForecast
import com.example.kursmobile.domain.repository.CityCoordinates
import com.example.kursmobile.domain.usecases.GetCityCoordinatesUseCase
import com.example.kursmobile.domain.usecases.GetWeatherForecastUseCase
import com.example.kursmobile.domain.usecases.SearchCitiesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Suggestions(val cities: List<CitySuggestion>) : WeatherUiState()
    data class Weather(val forecast: WeatherForecast) : WeatherUiState()
    data class Error(@StringRes val messageRes: Int) : WeatherUiState()
    object Empty : WeatherUiState()
}

class WeatherViewModel(
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val getCityCoordinatesUseCase: GetCityCoordinatesUseCase,
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    private val preferencesDataStore: PreferencesDataStore
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Empty)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadLastCity()
    }

    private fun loadLastCity() {
        viewModelScope.launch {
            preferencesDataStore.lastCity.collect { cityName ->
                if (cityName.isNotEmpty()) {
                    _searchQuery.value = cityName
                    fetchWeatherForCity(cityName)
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query

        if (query.isEmpty()) {
            _uiState.value = WeatherUiState.Empty
            searchJob?.cancel()
        } else if (query.length >= 2) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(500)
                try {
                    val cities = searchCitiesUseCase(query)
                    if (cities.isNotEmpty()) {
                        _uiState.value = WeatherUiState.Suggestions(cities)
                    } else {
                        _uiState.value = WeatherUiState.Empty
                    }
                } catch (e: Exception) {
                    _uiState.value = WeatherUiState.Error(R.string.error_search_failed)}
            }
        }
    }

    fun selectCity(city: CitySuggestion) {
        _searchQuery.value = city.name
        viewModelScope.launch {
            preferencesDataStore.saveLastCity(city.name)
            fetchWeatherForCity(city.name)
        }
    }

    private fun fetchWeatherForCity(cityName: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val coordinates = getCityCoordinatesUseCase(cityName)
                if (coordinates != null) {
                    getWeather(coordinates.latitude, coordinates.longitude, coordinates.name, coordinates.country)
                } else {
                    _uiState.value = WeatherUiState.Error(R.string.error_city_not_found)}
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(R.string.error_coordinates_failed)}
        }
    }

    private suspend fun getWeather(latitude: Double, longitude: Double, cityName: String, country: String) {
        try {
            val forecast = getWeatherForecastUseCase(latitude, longitude)
            val updatedForecast = forecast.copy(
                cityName = cityName,
                country = country
            )
            _uiState.value = WeatherUiState.Weather(updatedForecast)
        } catch (e: Exception) {
            _uiState.value = WeatherUiState.Error(R.string.error_weather_failed)}
    }
}