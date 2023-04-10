package com.example.motocast.ui.view.route_planner

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.address.Address
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.util.BackButton

@Composable
fun AddDestinationScreen(
    addressDataViewModel: AddressDataViewModel,
    routePlannerViewModel: RoutePlannerViewModel,
    navController: NavController,
) {
    val addressState by addressDataViewModel.uiState.collectAsState()
    val routePlannerUiState by routePlannerViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            BackButton {
                navController.popBackStack()
                addressDataViewModel.clearResults()
                // Minus 1 because of index out of bounds error
                val currentDestinations = routePlannerViewModel.getTotalDestinations() - 1
                routePlannerViewModel.setActiveDestinationIndex(1)
                routePlannerViewModel.removeDestination(currentDestinations)

            }
            Spacer(modifier = Modifier.width(20.dp) )

            OutlinedTextField(
                value = routePlannerUiState.destinations[routePlannerUiState.activeDestinationIndex].name ?: "",
                onValueChange = {
                    val newAddress = Address(
                        addressText = it,
                        municipality = null,
                        latitude = null,
                        longitude = null
                    )

                    routePlannerViewModel.updateDestination(
                        routePlannerUiState.activeDestinationIndex,
                        newAddress
                    )
                    addressDataViewModel.fetchAddressData(it)
                },
                label = { Text("Add Destination") },
                modifier = Modifier
                    .weight(0.8f, fill = true)

            )
        }


        if (addressState.addresses.isNotEmpty()) {
            LazyColumn(
            ) {
                items(addressState.addresses) { address: Address ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                routePlannerViewModel.updateDestination(
                                    routePlannerUiState.activeDestinationIndex,
                                    address
                                )
                                addressDataViewModel.clearResults()
                                navController.popBackStack()
                            }
                    ) {
                        Text(
                            text = address.addressText,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(top = 16.dp, bottom = 16.dp)
                        )
                    }
                }
            }
        }

    }
}
