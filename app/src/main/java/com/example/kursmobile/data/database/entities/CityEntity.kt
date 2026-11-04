package com.example.kursmobile.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val country: String,
    val population: Int,
    val is_capital: Boolean = false
)