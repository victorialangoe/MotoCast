package com.example.motocast.ui.view.route_planner.add_destinations

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.viewmodel.address.Address

@Composable
fun AddDestinationView(
    clearQuery: () -> Unit,
    clearResults: () -> Unit,
    setActiveDestinationIndex: (Int) -> Unit,
    removeDestination: (Int) -> Unit,
    updateDestination: (Int, Address) -> Unit,
    getTotalDestinations: () -> Int,
    addFormerAddress: (Address) -> Unit,
    formerAddresses: List<Address>,
    addresses: List<Address>,
    query: String,
    isFetching: Boolean,
    activeDestinationIndex: Int,
    popBackStack: () -> Unit,
    fetchAddressData: (String) -> Unit,
) {

    Column(modifier = Modifier.fillMaxSize()) {

        AddDestinationSearchBar(
            query = query,
            onClear = {
                clearQuery()
                clearResults()
            },
            onBack = {
                popBackStack()
                clearResults()
                // Minus 1 before removing the destination, since the active destination is the one we are currently editing
                val currentDestinations = getTotalDestinations() - 1
                setActiveDestinationIndex(1)
                removeDestination(currentDestinations)
            },
            onValueChange = { query, address ->
                updateDestination(activeDestinationIndex, address) // Nødvendig
                fetchAddressData(query)
            }
        )

        Column(
            modifier = Modifier
                .background(Color(0xFFF5F5F5)) // TODO: Move to theme
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize()
        ) {
            // Former searches in the cache
            DestinationResults(
                addresses = formerAddresses,
                title = "Tidligere søk",
                showTitle = false,
                row = true,
                query = query,
                isLoading = false, //This is never loading
                onResultClick = { address ->
                    // 1. Update the destination in the route planner
                    // 2. Clear the search results
                    // 2. Navigate back to the route planner
                    updateDestination(activeDestinationIndex, address)
                    clearResults()
                    clearQuery()
                    popBackStack()
                }
            )
            // This is current search results
            DestinationResults(
                addresses = addresses,
                title = "Søkeresultater",
                query = query,
                maxResults = 200,
                isLoading = isFetching,
                onResultClick = { address ->
                    // 1. Update the destination in the route planner
                    // 2. Add the address to the list of former addresses
                    // 3. Clear the search results
                    // 4. Navigate back to the route planner
                    updateDestination(activeDestinationIndex, address)
                    addFormerAddress(address)
                    clearResults()
                    clearQuery()
                    popBackStack()
                }
            )
        }
    }
}



