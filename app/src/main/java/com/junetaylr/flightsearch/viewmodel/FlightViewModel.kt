package com.junetaylr.flightsearch.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.junetaylr.flightsearch.data.Favorite
import com.junetaylr.flightsearch.data.FlightDao
import com.junetaylr.flightsearch.data.FlightDatabase
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FlightViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: FlightDao = FlightDatabase.getDatabase(application).flightDao()

    private val _airports = MutableLiveData<List<com.junetaylr.flightsearch.data.Airport>>()
    val airports: LiveData<List<com.junetaylr.flightsearch.data.Airport>> = _airports

    private val _favorites = MutableLiveData<List<Favorite>>()
    val favorites: LiveData<List<Favorite>> = _favorites

    private val _flights = MutableLiveData<List<com.junetaylr.flightsearch.data.Favorite>>() // routes
    val flights: LiveData<List<com.junetaylr.flightsearch.data.Favorite>> = _flights

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    // 🔎 Search airports
    fun searchAirports(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            val results = dao.searchAirports(query)
            _airports.value = results
        }
    }

    // ⭐ Load favorites
    fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = dao.getAllFavorites()
        }
    }

    fun addFavorite(departure: String, destination: String) {
        viewModelScope.launch {
            dao.insertFavorite(Favorite(departure_code = departure, destination_code = destination))
            loadFavorites()
        }
    }

    // ✈️ Load flights from departure
    fun loadFlightsFromAirport(departureCode: String) {
        viewModelScope.launch {
            val results = dao.getFlightsFromAirport(departureCode)
            // ✅ Remove duplicates by destination
            _flights.value = results.distinctBy { it.destination_code }
        }
    }
}
