package com.example.motocast.ui.view.route_planner

import android.location.Location
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.motocast.R
import com.example.motocast.ui.viewmodel.address.Address
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.util.BackButton

@Composable
fun AddDestinationScreen(
    addressDataViewModel: AddressDataViewModel,
    routePlannerViewModel: RoutePlannerViewModel,
    mapLocationViewModel: MapLocationViewModel,
    navController: NavController,
) {
    val addressState by addressDataViewModel.uiState.collectAsState()
    val routePlannerUiState by routePlannerViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

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
            Spacer(modifier = Modifier.width(20.dp))

            OutlinedTextField(
                value = routePlannerUiState.destinations[routePlannerUiState.activeDestinationIndex].name
                    ?: "",
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
                    addressDataViewModel.fetchAddressData(it, mapLocationViewModel)
                },
                label = { Text("Søk etter adresse") },
                modifier = Modifier
                    .weight(0.8f, fill = true),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    cursorColor = Color.Black,
                    textColor = Color.Black,
                    disabledBorderColor = Color.Black,
                    disabledLabelColor = Color.Black,
                    disabledTextColor = Color.Black,
                    errorBorderColor = Color.Black,
                    errorLabelColor = Color.Black,
                    errorCursorColor = Color.Black,
                    errorLeadingIconColor = Color.Black,
                    errorTrailingIconColor = Color.Black,
                ),
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                        contentDescription = "Search icon",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                    )
                },

                )
        }

        Column(
            modifier = Modifier
                // background is a white ish color
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize()
        ) {

            if (addressState.formerAddresses.isNotEmpty()) {
                Text(
                    text = "Tidligere destinasjoner",
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .align(Alignment.Start)
                )
                LazyColumn {
                    items(addressState.formerAddresses) {

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                            ),
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                                routePlannerViewModel.updateDestination(
                                    routePlannerUiState.activeDestinationIndex,
                                    it
                                )
                                addressDataViewModel.clearResults()
                                navController.popBackStack()
                            }) {

                            Column {
                                Text(
                                    text = it.addressText,
                                    fontSize = 16.sp,

                                    )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = it.municipality ?: "",
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .weight(0.8f, fill = true)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (it.distanceFromUser!! > 1000) {
                                            "${it.distanceFromUser?.div(1000)} km"
                                        } else {
                                            "${it.distanceFromUser} m"
                                        },
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .weight(0.2f, fill = true)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (addressState.addresses.isNotEmpty()) {
                Text(
                    text = "Søkeresultater",
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .align(Alignment.Start)
                )
                LazyColumn {
                    // Search results
                    items(addressState.addresses.sortedBy { it.distanceFromUser }) { address ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                            ),
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                                routePlannerViewModel.updateDestination(
                                    routePlannerUiState.activeDestinationIndex,
                                    address
                                )
                                addressDataViewModel.addFormerAddress(address)
                                addressDataViewModel.clearResults()
                                navController.popBackStack()
                            }) {
                            Column {
                                Text(
                                    text = address.addressText,
                                    fontSize = 16.sp,

                                    )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = address.municipality ?: "",
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .weight(0.8f, fill = true)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (address.distanceFromUser!! > 1000) {
                                            "${address.distanceFromUser?.div(1000)} km"
                                        } else {
                                            "${address.distanceFromUser} m"
                                        },
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .weight(0.2f, fill = true)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



