package com.junetaylr.flightsearch.datastore


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance
val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val SEARCH_QUERY = stringPreferencesKey("search_query")
    }

    // Save search query
    suspend fun saveSearchQuery(query: String) {
        context.dataStore.edit { prefs ->
            prefs[SEARCH_QUERY] = query
        }
    }

    // Get saved search query as a Flow
    val searchQuery: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[SEARCH_QUERY] ?: ""
    }
}
