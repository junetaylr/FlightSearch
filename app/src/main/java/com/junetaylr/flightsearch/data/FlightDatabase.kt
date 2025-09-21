package com.junetaylr.flightsearch.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Airport::class, Favorite::class], version = 1, exportSchema = false)
abstract class FlightDatabase : RoomDatabase() {
    abstract fun flightDao(): FlightDao

    companion object {
        @Volatile
        private var INSTANCE: FlightDatabase? = null

        fun getDatabase(context: Context): FlightDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlightDatabase::class.java,
                    "flights.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(SeedCallback(context)) // ⬅️ seed every open
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SeedCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)

            CoroutineScope(Dispatchers.IO).launch {
                val dao = getDatabase(context).flightDao()
                dao.insertAirports(
                    listOf(
                        Airport(iata_code = "ATL", name = "Atlanta Hartsfield-Jackson", passengers = 110_000_000),
                        Airport(iata_code = "LAX", name = "Los Angeles International", passengers = 88_000_000),
                        Airport(iata_code = "JFK", name = "John F. Kennedy International", passengers = 62_000_000),
                        Airport(iata_code = "ORD", name = "Chicago O'Hare", passengers = 84_000_000),
                        Airport(iata_code = "DFW", name = "Dallas/Fort Worth International", passengers = 75_000_000)
                    )
                )
            }
        }
    }
}

