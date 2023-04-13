package com.example.motocast.ui.view

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.motocast.ui.view.dynamicScaffold.DynamicScaffoldView
import com.example.motocast.ui.view.map.MapView
import com.example.motocast.ui.view.route_planner.RoutePlannerView
import com.example.motocast.ui.view.route_planner.add_destinations.AddDestinationView
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel

@Composable
fun AppNavigation(
    mapLocationViewModel: MapLocationViewModel,
    nowCastViewModel: NowCastViewModel,
    routePlannerViewModel: RoutePlannerViewModel,
    addressDataViewModel: AddressDataViewModel,
    context: Context
) {
    val navController = rememberNavController()
    val addressViewModelUiState = addressDataViewModel.uiState.collectAsState()
    val routePlannerViewModelUiState = routePlannerViewModel.uiState.collectAsState()
    val mapLocationViewModelUiState = mapLocationViewModel.uiState.collectAsState()


    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") {
            DynamicScaffoldView(
                context = context,
                nowCastViewModel = nowCastViewModel,
                mapLocationViewModel = mapLocationViewModel,
                routePlannerViewModel = routePlannerViewModel,
                content = {
                    MapView(
                        mapView = mapLocationViewModelUiState.value.mapView,
                        drawGeoJson = { geoJsonData -> mapLocationViewModel.drawGeoJson(geoJsonData) },
                        onInit = {
                            mapLocationViewModel.loadMapView(context)
                            // calls cameraToUserLocation() when the map is loaded, less repetitive code
                            if (!routePlannerViewModel.checkIfAllDestinationsHaveNames()){
                                mapLocationViewModel.cameraToUserLocation()
                            }
                        },
                        geoJsonData = routePlannerViewModelUiState.value.geoJsonData
                    )
                },
                onNavigateToScreen = {
                    navController.navigate("route_planner")
                })
        }
        composable("route_planner") {
            RoutePlannerView(
                editDestination = { index -> routePlannerViewModel.editDestination(index) {
                    navController.navigate(
                        "add_destination_screen"
                    )
                }
                },
                addDestination = { routePlannerViewModel.addDestination {
                    navController.navigate(
                        "add_destination_screen"
                    )
                }
                },
                navigateTo = { screen -> navController.navigate(screen) },
                startRoute = {
                    routePlannerViewModel.start(
                        { navController.navigate("home_screen") },
                        { mapLocationViewModel.fitCameraToRouteAndWaypoints(routePlannerViewModelUiState.value.destinations) }
                    ) },
                removeDestination = { index -> routePlannerViewModel.removeDestination(index) },
                updateDateUiState = { dateUiState -> routePlannerViewModel.updateDateUiState(dateUiState) },
                updateTimeUiState = { timeUiState -> routePlannerViewModel.updateTimeUiState(timeUiState) },
                destinations = routePlannerViewModelUiState.value.destinations,
                clearAll = { routePlannerViewModel.clear() },
                year = routePlannerViewModelUiState.value.startTime.datePickerUiState.year,
                month = routePlannerViewModelUiState.value.startTime.datePickerUiState.month,
                day =  routePlannerViewModelUiState.value.startTime.datePickerUiState.day,
                hour = routePlannerViewModelUiState.value.startTime.timePickerUiState.hour,
                minute = routePlannerViewModelUiState.value.startTime.timePickerUiState.minute,
                context = context,
                enabledStartRoute = routePlannerViewModel.checkIfAllDestinationsHaveNames(),
            )
        }
        composable("add_destination_screen") {
            AddDestinationView(
                fetchAddressData = { query -> addressDataViewModel.fetchAddressData(
                    query,
                    getCurrentLocation = mapLocationViewModel::getCurrentLocation,
                    getAirDistanceFromPosToPos = mapLocationViewModel::getAirDistanceFromPosToPos,
                ) },
                clearQuery = { addressDataViewModel.clearQuery() },
                clearResults = { addressDataViewModel.clearResults() },
                addFormerAddress = { address -> addressDataViewModel.addFormerAddress(address) },
                formerAddresses = addressViewModelUiState.value.formerAddresses,
                addresses = addressViewModelUiState.value.addresses,
                query = addressViewModelUiState.value.query,
                isFetching = addressViewModelUiState.value.isLoading,
                setActiveDestinationIndex = { index -> routePlannerViewModel.setActiveDestinationIndex(index) },
                removeDestination = { index -> routePlannerViewModel.removeDestination(index) },
                updateDestination = { index, destination -> routePlannerViewModel.updateDestination(index, destination) },
                getTotalDestinations = { routePlannerViewModel.getTotalDestinations() },
                activeDestinationIndex = routePlannerViewModelUiState.value.activeDestinationIndex,
                popBackStack = { navController.popBackStack() },
            )
        }
    }
}