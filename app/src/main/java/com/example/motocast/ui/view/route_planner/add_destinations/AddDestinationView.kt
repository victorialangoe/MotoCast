package com.example.motocast.ui.view.route_planner.add_destinations

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.motocast.R
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
    setQuery: (String) -> Unit,
    query: String,
    popBackStack: () -> Unit,
    isFetching: Boolean,
    activeDestinationIndex: Int,
    fetchAddressData: (String) -> Unit,
    location: Location?,
) {
    val myLocationString = stringResource(R.string.my_location)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        AddDestinationSearchBar(
            onClear = {
                clearQuery()
                clearResults()
            },
            query = query,
            onBack = {
                popBackStack()
                clearResults()
                // Minus 1 before removing the destination, since the active destination is the one we are currently editing
                val currentDestinations = getTotalDestinations() - 1
                setActiveDestinationIndex(1)
                // If no edits have been made, remove the destination
                if (currentDestinations == getTotalDestinations()) {
                    removeDestination(activeDestinationIndex)
                }
            },
            onValueChange = { query, address ->
                updateDestination(activeDestinationIndex, address)
                fetchAddressData(query)
                setQuery(query)
            },
            onSetCurrentLocation = {
                location?.let {
                    val address = Address(
                        addressText = myLocationString,
                        municipality = null,
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    updateDestination(activeDestinationIndex, address)
                    clearResults()
                    clearQuery()
                    popBackStack()
                }
            }
        )

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize()
        ) {
            // Former searches in the cache
            DestinationResults(
                addresses = formerAddresses,
                title = stringResource(R.string.former_searches),
                showTitle = false,
                row = true,
                isLoading = false, //This is never loading
                onResultClick = { address ->
                    // 1. Update the destination in the route planner
                    // 2. Clear the search results
                    // 2. Navigate back to the route planner
                    updateDestination(activeDestinationIndex, address)
                    clearResults()
                    clearQuery()
                    popBackStack()

                },
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            // This is current search results
            DestinationResults(
                addresses = addresses,
                title = stringResource(R.string.search_results),
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
                },
            )
        }
    }
}



