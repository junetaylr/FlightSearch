package com.junetaylr.flightsearch



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.junetaylr.flightsearch.viewmodel.FlightViewModel

class MainActivity : ComponentActivity() {

    private val flightViewModel: FlightViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FlightSearchScreen(viewModel = flightViewModel)
            }
        }
    }
}

@Composable
fun FlightSearchScreen(viewModel: FlightViewModel) {
    val searchQuery by viewModel.searchQuery.observeAsState("")
    val airports by viewModel.airports.observeAsState(emptyList())
    val favorites by viewModel.favorites.observeAsState(emptyList())
    val flights by viewModel.flights.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search box
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query -> viewModel.searchAirports(query) },
            label = { Text("Search Airport (IATA or name)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (searchQuery.isEmpty()) {
            // Show ALL airports when search is empty
            Text("All Airports", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(airports) { airport ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${airport.iata_code} - ${airport.name}")
                        Button(
                            onClick = { viewModel.loadFlightsFromAirport(airport.iata_code) }
                        ) {
                            Text("Show Flights")
                        }
                    }
                }
            }
        } else {
            // Filtered search results
            Text("Search Results", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(airports) { airport ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${airport.iata_code} - ${airport.name}")
                        Button(
                            onClick = { viewModel.loadFlightsFromAirport(airport.iata_code) }
                        ) {
                            Text("Show Flights")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show flights from selected airport
            Text("Flights", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(flights) { route ->
                    Text(
                        text = "${route.departure_code} → ${route.destination_code}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

    }


            Spacer(modifier = Modifier.height(16.dp))

            // Show flights
            Text("Flights", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(flights) { route ->
                    Text(
                        text = "${route.departure_code} → ${route.destination_code}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
}

