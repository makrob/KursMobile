package com.example.kursmobile.data.network.api

import com.example.kursmobile.data.network.models.CityCoordinatesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CityCoordinatesApi {
    @GET("v1/city")
    suspend fun getCityCoordinates(
        @Header("X-Api-Key") apiKey: String,
        @Query("name") name: String
    ): List<CityCoordinatesResponse>
}