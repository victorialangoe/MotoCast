package com.example.motocast

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.motocast.theme.AppTheme
import com.example.motocast.ui.view.dynamic_scaffold.DynamicScaffoldView
import com.example.motocast.ui.view.map.MapView
import com.example.motocast.ui.view.route_planner.RoutePlannerView
import com.example.motocast.ui.view.route_planner.add_destinations.AddDestinationView
import com.example.motocast.ui.view.settings.SettingsView
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import com.example.motocast.ui.viewmodel.map.MapViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.ui.viewmodel.settings.SettingsViewModel
import com.example.motocast.ui.viewmodel.current_weather.CurrentWeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AppNavigation(
    addressDataViewModel: AddressDataViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    weatherViewModel: CurrentWeatherViewModel = hiltViewModel(),
    routePlannerViewModel: RoutePlannerViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel(),
    context: Context
) {
    val navController = rememberNavController()
    val addressViewModelUiState = addressDataViewModel.uiState.collectAsState()
    val routePlannerViewModelUiState = routePlannerViewModel.uiState.collectAsState()
    val mapLocationViewModelUiState = mapViewModel.uiState.collectAsState()
    val settingsViewModelUiState = settingsViewModel.uiState.collectAsState()

    AppTheme(
        darkTheme = settingsViewModelUiState.value.darkMode,
    ) {
        NavHost(navController = navController, startDestination = "home_screen") {
            composable("home_screen") {
                DynamicScaffoldView(
                    context = context,
                    destinations = routePlannerViewModelUiState.value.destinations,
                    isTrackUserActive = mapLocationViewModelUiState.value.trackUserOnMap,
                    weatherViewModel = weatherViewModel,
                    mapViewModel = mapViewModel,
                    routePlannerViewModel = routePlannerViewModel,
                    navigateToSettings = { navController.navigate("settings_screen") },
                    onNavigateToScreen = { navController.navigate("route_planner") },
                    isRouteLoading = routePlannerViewModelUiState.value.isLoading,
                    duration = routePlannerViewModelUiState.value.durationAsString,
                    waypoints = routePlannerViewModelUiState.value.waypoints,
                    userName = settingsViewModelUiState.value.userName,
                    content = {
                        MapView(
                            mapView = mapLocationViewModelUiState.value.mapView,
                            drawGeoJson = { geoJsonData ->
                                mapViewModel.drawGeoJson(
                                    geoJsonData
                                )
                            },
                            onInit = {
                                mapViewModel.loadMapView()
                            },
                            geoJsonData = routePlannerViewModelUiState.value.geoJsonData,
                            bottomOffset = mapLocationViewModelUiState.value.mapBottomOffset,
                            waypoints = routePlannerViewModelUiState.value.waypoints,
                            context = context,
                        )
                    },
                )
            }
            composable("route_planner") {
                RoutePlannerView(
                    destinations = routePlannerViewModelUiState.value.destinations,
                    clearAll = { routePlannerViewModel.clear() },
                    startTime = routePlannerViewModelUiState.value.startTime,
                    context = context,
                    enabledStartRoute = routePlannerViewModel.checkIfAllDestinationsHaveNames(),
                    routesAdded = routePlannerViewModel.checkIfSomeDestinationsHaveNames(),
                    isLoading = routePlannerViewModelUiState.value.isLoading,
                    editDestination = { index ->
                        routePlannerViewModel.editDestination(index) {
                            navController.navigate(
                                "add_destination_screen"
                            )
                        }
                    },
                    addDestination = {
                        routePlannerViewModel.addDestination {
                            navController.navigate(
                                "add_destination_screen"
                            )
                        }
                    },
                    navigateTo = { screen -> navController.navigate(screen) },
                    startRoute = {
                        CoroutineScope(Dispatchers.Main).launch {
                            routePlannerViewModel.start(
                                { navController.navigate("home_screen") },
                                {
                                    mapViewModel.fitCameraToRouteAndWaypoints(
                                        routePlannerViewModelUiState.value.destinations
                                    )
                                }
                            )
                            mapViewModel.trackUserOnMap(
                                routeExists = true,
                                destinations = routePlannerViewModelUiState.value.destinations,
                                track = false
                            )
                        }
                    },
                    removeDestination = { index -> routePlannerViewModel.removeDestination(index) },
                    updateStartTime = { time: Calendar ->
                        routePlannerViewModel.updateStartTime(
                            time
                        )
                    },
                )
            }
            composable("add_destination_screen") {
                AddDestinationView(
                    clearQuery = { addressDataViewModel.clearQuery() },
                    query = addressViewModelUiState.value.query,
                    clearResults = { addressDataViewModel.clearResults() },
                    addFormerAddress = { address -> addressDataViewModel.addFormerAddress(address) },
                    formerAddresses = addressViewModelUiState.value.formerAddresses,
                    addresses = addressViewModelUiState.value.addresses,
                    setQuery = { query -> addressDataViewModel.setQuery(query) },
                    isFetching = addressViewModelUiState.value.isLoading,
                    getTotalDestinations = { routePlannerViewModel.getTotalDestinations() },
                    activeDestinationIndex = routePlannerViewModelUiState.value.activeDestinationIndex,
                    getCurrentLocation = mapViewModel.getCurrentLocation(),
                    navigateTo = { screen -> navController.navigate(screen) },
                    fetchAddressData = { query ->
                        CoroutineScope(Dispatchers.IO).launch {

                            addressDataViewModel.fetchAddressData(
                                query
                            )
                        }
                    },
                    setActiveDestinationIndex = { index ->
                        routePlannerViewModel.setActiveDestinationIndex(
                            index
                        )
                    },
                    removeDestination = { index -> routePlannerViewModel.removeDestination(index) },
                    updateDestination = { index, destination ->
                        routePlannerViewModel.updateDestination(
                            index,
                            destination
                        )
                    },

                )
            }
            composable("settings_screen") {
                SettingsView(
                    userName = settingsViewModelUiState.value.userName,
                    setUserName = { name -> settingsViewModel.setUserName(name) },
                    popBackStack = { navController.popBackStack() },
                    setDarkMode = { settingsViewModel.setDarkMode(it) },
                    darkMode = settingsViewModelUiState.value.darkMode
                )
            }
        }
    }
}