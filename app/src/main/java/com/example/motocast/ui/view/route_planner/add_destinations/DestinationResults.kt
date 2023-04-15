package com.example.motocast.ui.view.route_planner.add_destinations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.motocast.ui.viewmodel.address.Address

/**
 * This is the search results for the destination search
 *
 * @param addresses The list of addresses that matches the search query
 * @param title The title of the search results
 * @param query The search query
 * @param showTitle Whether or not to show the title of the search results
 * @param maxResults The maximum number of results to show (default is 5)
 * @param isLoading Whether or not the search is still loading
 * @param onResultClick The function to call when a search result is clicked
 */
@Composable
fun DestinationResults(
    addresses: List<Address>,
    title: String,
    query: String,
    row: Boolean = false,
    showTitle: Boolean = true,
    maxResults: Int = 5,
    isLoading: Boolean,
    onResultClick: (address: Address) -> Unit,
) {
    // Only show the first 5 results (If maxResults is not specified)
    addresses.take(maxResults)

    val content: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (row) {
                LazyRow {
                    // Search results
                    items(addresses.sortedWith(compareBy(
                        // Sort first by the address that matches the query, then by distance
                        { if (it.addressText.lowercase() == query.lowercase()) 0 else 1 },
                        { it.distanceFromUser }
                    ))) {
                        AddressResult(it, onResultClick)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

            } else {
                LazyColumn {
                    // Search results
                    items(addresses.sortedWith(compareBy(
                        // Sort first by the address that matches the query, then by distance
                        { if (it.addressText.lowercase() == query.lowercase()) 0 else 1 },
                        { it.distanceFromUser }
                    ))) {
                        Spacer(modifier = Modifier.height(8.dp))
                        AddressResult(it, onResultClick)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // This is the title of the search results
        if (showTitle || addresses.isNotEmpty()) {
            Text(
                text = when {
                    isLoading -> "Laster..."
                    addresses.isEmpty() -> "Ingen treff"
                    else -> title
                },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(vertical = 8.dp)
            )
        }

        // This is where all the search results will be shown (if any)
        if (addresses.isNotEmpty() && !isLoading) {
            content()
        }
    }
}