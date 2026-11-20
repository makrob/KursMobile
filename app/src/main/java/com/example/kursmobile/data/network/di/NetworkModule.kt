package com.example.kursmobile.data.network.di

import com.example.kursmobile.data.network.api.CityCoordinatesApi
import com.example.kursmobile.data.network.api.WeatherApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val CITY_API_BASE_URL = "https://api.api-ninjas.com/"
    private const val WEATHER_API_BASE_URL = "https://api.weatherapi.com/"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun provideCityApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(CITY_API_BASE_URL)
            .client(provideOkHttpClient())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private fun provideWeatherApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_API_BASE_URL)
            .client(provideOkHttpClient())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    fun provideCityApiService(): CityCoordinatesApi {
        return provideCityApiRetrofit().create(CityCoordinatesApi::class.java)
    }

    fun provideWeatherApiService(): WeatherApiService {
        return provideWeatherApiRetrofit().create(WeatherApiService::class.java)
    }
}