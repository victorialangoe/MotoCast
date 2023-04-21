package com.example.motocast.ui.view

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.motocast.ui.theme.AppTheme
import com.example.motocast.ui.view.dynamic_scaffold.DynamicScaffoldView
import com.example.motocast.ui.view.map.MapView
import com.example.motocast.ui.view.route_planner.RoutePlannerView
import com.example.motocast.ui.view.route_planner.add_destinations.AddDestinationView
import com.example.motocast.ui.view.settings.SettingsView
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.ui.viewmodel.settings.SettingsViewModel
import com.example.motocast.ui.viewmodel.weather.RouteWeatherUiState
import com.example.motocast.ui.viewmodel.weather.WeatherUiState
import com.example.motocast.ui.viewmodel.weather.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun AppNavigation(
    mapLocationViewModel: MapLocationViewModel,
    weatherViewModel: WeatherViewModel,
    routePlannerViewModel: RoutePlannerViewModel,
    addressDataViewModel: AddressDataViewModel,
    settingsViewModel: SettingsViewModel,
    context: Context
) {
    val navController = rememberNavController()
    val addressViewModelUiState = addressDataViewModel.uiState.collectAsState()
    val routePlannerViewModelUiState = routePlannerViewModel.uiState.collectAsState()
    val mapLocationViewModelUiState = mapLocationViewModel.uiState.collectAsState()
    val settingsViewModelUiState = settingsViewModel.uiState.collectAsState()
    val weatherUiStateS = weatherViewModel.uiState.collectAsState()

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
                    /*
                    onStartButtonClick = {
                        routePlannerViewModel.start(
                            { navController.navigate("home_screen") },
                            {
                                mapLocationViewModel.fitCameraToRouteAndWaypoints(
                                    routePlannerViewModelUiState.value.destinations
                                )
                            }
                        )
                        mapLocationViewModel.trackUserOnMap(
                            routeExists = true,
                            destinations = routePlannerViewModelUiState.value.destinations,
                            track = false
                        )
                    },
                     */
                    mapLocationViewModel = mapLocationViewModel,
                    routePlannerViewModel = routePlannerViewModel,
                    navigateToSettings = { navController.navigate("settings_screen") },
                    onNavigateToScreen = { navController.navigate("route_planner") },
                    isRouteLoading = routePlannerViewModelUiState.value.isLoading,
                    duration = routePlannerViewModelUiState.value.durationAsString,
                    waypoints = routePlannerViewModelUiState.value.waypoints,
                    content = {
                        MapView(
                            mapView = mapLocationViewModelUiState.value.mapView,
                            drawGeoJson = { geoJsonData ->
                                mapLocationViewModel.drawGeoJson(
                                    geoJsonData
                                )
                            },
                            onInit = {
                                mapLocationViewModel.loadMapView(context)
                            },
                            geoJsonData = routePlannerViewModelUiState.value.geoJsonData,
                            bottomOffset = mapLocationViewModelUiState.value.mapBottomOffset,
                        )
                    },
                )
            }
            composable("route_planner") {
                RoutePlannerView(
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
                                    mapLocationViewModel.fitCameraToRouteAndWaypoints(
                                        routePlannerViewModelUiState.value.destinations
                                    )
                                },
                                getWeatherData = { lat: Double, lon: Double, time: Calendar, callback: (RouteWeatherUiState) -> Unit ->
                                    CoroutineScope(Dispatchers.IO).launch {
                                        weatherViewModel.getWeatherData(
                                            lat,
                                            lon,
                                            time
                                        ) { weatherUiState ->
                                            weatherUiState?.let { callback(it) }
                                        }
                                    }
                                },
                            )
                            mapLocationViewModel.trackUserOnMap(
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
                    destinations = routePlannerViewModelUiState.value.destinations,
                    clearAll = { routePlannerViewModel.clear() },
                    startTime = routePlannerViewModelUiState.value.startTime,
                    context = context,
                    enabledStartRoute = routePlannerViewModel.checkIfAllDestinationsHaveNames(),
                    routesAdded = routePlannerViewModel.checkIfSomeDestinationsHaveNames(),
                    isLoading = routePlannerViewModelUiState.value.isLoading,
                )
            }
            composable("add_destination_screen") {
                AddDestinationView(
                    fetchAddressData = { query ->
                        CoroutineScope(Dispatchers.IO).launch {

                            addressDataViewModel.fetchAddressData(
                                query,
                                getAirDistanceFromLocation = { location ->
                                    mapLocationViewModel.getAirDistanceFromLocation(
                                        location
                                    )
                                }
                            )
                        }
                    },
                    clearQuery = { addressDataViewModel.clearQuery() },
                    clearResults = { addressDataViewModel.clearResults() },
                    addFormerAddress = { address -> addressDataViewModel.addFormerAddress(address) },
                    formerAddresses = addressViewModelUiState.value.formerAddresses,
                    addresses = addressViewModelUiState.value.addresses,
                    query = addressViewModelUiState.value.query,
                    setQuery = { query -> addressDataViewModel.setQuery(query) },
                    isFetching = addressViewModelUiState.value.isLoading,
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
                    getTotalDestinations = { routePlannerViewModel.getTotalDestinations() },
                    activeDestinationIndex = routePlannerViewModelUiState.value.activeDestinationIndex,
                    popBackStack = { navController.popBackStack() },
                    getCurrentLocation = { mapLocationViewModel.getCurrentLocation() },
                    navigateTo = { screen -> navController.navigate(screen) },
                    searchResultsCompareBy ={ string ->
                        addressDataViewModel.searchResultsCompareBy(string)
                    },
                )
            }
            composable("settings_screen") {
                SettingsView(
                    popBackStack = { navController.popBackStack() },
                    setDarkMode = { settingsViewModel.setDarkMode(it) },
                    darkMode = settingsViewModelUiState.value.darkMode
                )
            }
        }
    }
}