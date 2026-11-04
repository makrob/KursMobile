package com.example.kursmobile.data.repository

import com.example.kursmobile.BuildConfig
import com.example.kursmobile.data.database.AppDatabase
import com.example.kursmobile.data.network.api.CityCoordinatesApi
import com.example.kursmobile.data.network.api.WeatherApiService
import com.example.kursmobile.domain.models.CitySuggestion
import com.example.kursmobile.domain.models.DailyForecast
import com.example.kursmobile.domain.models.WeatherForecast
import com.example.kursmobile.domain.repository.WeatherRepository
import com.example.kursmobile.domain.repository.CityCoordinates
import kotlinx.coroutines.flow.first

class WeatherRepositoryImpl(
    private val database: AppDatabase,
    private val cityCoordinatesApi: CityCoordinatesApi,
    private val weatherApi: WeatherApiService
) : WeatherRepository {

    private val cityDao = database.cityDao()

    override suspend fun searchCities(query: String): List<CitySuggestion> {
        if (query.length < 2) return emptyList()

        val localResults = cityDao.searchCities(query).first()
        return localResults.map {
            CitySuggestion(
                name = it.name,
                country = it.country,
                population = it.population,
                isCapital = it.is_capital
            )
        }
    }

    override suspend fun getCityCoordinates(cityName: String): CityCoordinates? {
        return try {
            val response = cityCoordinatesApi.getCityCoordinates(
                apiKey = BuildConfig.NINJAS_API_KEY,
                name = cityName
            )
            android.util.Log.d("WeatherRepo", "API response for '$cityName': $response")
            response.firstOrNull()?.let {
                CityCoordinates(
                    name = it.name,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    country = it.country
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("WeatherRepo", "City coordinates error for '$cityName'", e)
            null
        }
    }

    override suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherForecast {
        val response = weatherApi.getForecast(latitude, longitude)

        val dailyForecasts = response.daily.time.mapIndexed { index, date ->
            DailyForecast(
                date = date,
                maxTemp = response.daily.temperature_2m_max.getOrElse(index) { 0.0 },
                minTemp = response.daily.temperature_2m_min.getOrElse(index) { 0.0 },
                avgTemp = response.daily.temperature_2m_mean.getOrElse(index) { 0.0 }
            )
        }

        return WeatherForecast(
            cityName = "",
            country = "",
            latitude = latitude,
            longitude = longitude,
            dailyForecasts = dailyForecasts
        )
    }
}