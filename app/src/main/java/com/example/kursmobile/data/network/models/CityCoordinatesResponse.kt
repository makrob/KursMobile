package com.example.kursmobile.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class CityCoordinatesResponse(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val population: Int,
    val is_capital: Boolean
)