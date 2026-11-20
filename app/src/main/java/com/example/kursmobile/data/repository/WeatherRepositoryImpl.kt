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
        val query = "$latitude,$longitude"
        val response = weatherApi.getForecast(
            apiKey = BuildConfig.WEATHER_API_KEY,
            query = query,
            days = 7
        )

        val dailyForecasts = response.forecast.forecastDay.map { day ->
            DailyForecast(
                date = day.date,
                maxTemp = day.day.maxTempC,
                minTemp = day.day.minTempC,
                avgTemp = day.day.avgTempC
            )
        }

        return WeatherForecast(
            cityName = response.location.name,
            country = response.location.country,
            latitude = response.location.lat,
            longitude = response.location.lon,
            dailyForecasts = dailyForecasts
        )
    }
}