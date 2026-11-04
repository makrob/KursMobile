package com.example.kursmobile.domain.models

data class CitySuggestion(
    val name: String,
    val country: String,
    val population: Int,
    val isCapital: Boolean
)