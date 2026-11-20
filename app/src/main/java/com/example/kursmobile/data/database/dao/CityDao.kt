package com.example.kursmobile.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.kursmobile.data.database.entities.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("""
        SELECT * FROM cities 
        WHERE name LIKE '%' || :query || '%' 
           OR country LIKE '%' || :query || '%'
        ORDER BY 
            CASE 
                WHEN name = :query THEN 1
                WHEN name LIKE :query || '%' THEN 2
                WHEN country LIKE :query || '%' THEN 3
                ELSE 4
            END,
            population DESC
        LIMIT 10
    """)
    fun searchCities(query: String): Flow<List<CityEntity>>

    @Query("SELECT * FROM cities WHERE name = :name LIMIT 1")
    suspend fun getCityByName(name: String): CityEntity?
}