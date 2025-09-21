package com.junetaylr.flightsearch.data

import androidx.room.*

@Dao
interface FlightDao {
    // Search airports by IATA code or name (autocomplete)
    @Query("SELECT * FROM airport WHERE LOWER(iata_code) LIKE LOWER(:query) || '%' OR LOWER(name) LIKE LOWER(:query) || '%'")
    suspend fun searchAirports(query: String): List<Airport>

    @Query("SELECT * FROM favorite WHERE departure_code = :departureCode")
    suspend fun getFlightsFromAirport(departureCode: String): List<Favorite>


    // Get all favorites
    @Query("SELECT * FROM favorite")
    suspend fun getAllFavorites(): List<Favorite>

    // Insert a favorite route
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirports(airports: List<Airport>)
}