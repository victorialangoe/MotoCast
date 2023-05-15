package com.example.motocast

import android.content.Context
import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.motocast.theme.AppTheme
import com.example.motocast.ui.view.dynamic_scaffold.DynamicScaffoldView
import com.example.motocast.ui.view.home.HomeView
import com.example.motocast.ui.view.map.MapView
import com.example.motocast.ui.view.route_planner.RoutePlannerView
import com.example.motocast.ui.view.route_planner.add_destinations.AddDestinationView
import com.example.motocast.ui.view.settings.SettingsView
import com.example.motocast.ui.view.utils.badges.WelcomeBadge
import com.example.motocast.ui.view.utils.components.Header
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import com.example.motocast.ui.viewmodel.current_weather.CurrentWeatherViewModel
import com.example.motocast.ui.viewmodel.map.MapViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.ui.viewmodel.settings.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


/**
 * The [AppNavigation] handles the navigation of the app.
 *
 * @param addressDataViewModel The [AddressDataViewModel] to use for the address data
 * @param settingsViewModel The [SettingsViewModel] to use for the settings
 * @param weatherViewModel The [CurrentWeatherViewModel] to use for the weather
 * @param routePlannerViewModel The [RoutePlannerViewModel] to use for the route planner
 * @param mapViewModel The [MapViewModel] to use for the map
 * @param context The [Context] to use for the app
 *
 */
@Composable
fun AppNavigation(
    addressDataViewModel: AddressDataViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    weatherViewModel: CurrentWeatherViewModel = hiltViewModel(),
    routePlannerViewModel: RoutePlannerViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel(),
    context: Context,
    location: Location? = null,
    currentScreen: Int = 0,
) {
    val navController = rememberNavController()
    val addressViewModelUiState = addressDataViewModel.uiState.collectAsState()
    val routePlannerViewModelUiState = routePlannerViewModel.uiState.collectAsState()
    val mapLocationViewModelUiState = mapViewModel.uiState.collectAsState()
    val settingsViewModelUiState = settingsViewModel.uiState.collectAsState()
    val currentScreen = remember { mutableStateOf(currentScreen) }
    val snackbarHostState = remember { SnackbarHostState() }
    //shared flow for error messages



    val onLocateUserClick: () -> Unit =
        {
            mapViewModel.trackUserOnMap(
                routeExists = routePlannerViewModel.checkIfAllDestinationsHaveNames(),
                destinations = routePlannerViewModelUiState.value.destinations,
            )
        }


    AppTheme(
        darkTheme = settingsViewModelUiState.value.darkMode,
    ) {

        DynamicScaffoldView(
            context = context,
            isTrackUserActive = mapLocationViewModelUiState.value.trackUserOnMap,
            routePlannerViewModel = routePlannerViewModel,
            navigateToSettings = { navController.navigate("settings_screen") },
            onNavigateToScreen = { navController.navigate("route_planner") },
            isRouteLoading = routePlannerViewModelUiState.value.isLoading,
            duration = routePlannerViewModelUiState.value.durationAsString,
            waypoints = routePlannerViewModelUiState.value.waypoints,
            onLocateUserClick = onLocateUserClick,
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
                    waypoints = routePlannerViewModelUiState.value.waypoints,
                    context = context,
                )

                NavHost(navController = navController, startDestination = "home_screen") {
                    currentScreen.value = 0
                    composable("home_screen") {
                        currentScreen.value = 0
                        HomeView(
                            context = context,
                            weatherViewModel = weatherViewModel,
                            settingsNavigateTo = { navController.navigate("settings_screen") },
                            onCreateNewRouteClick = {
                                routePlannerViewModel.clear()
                                navController.navigate("route_planner")
                            },
                            onLocateUserClick = onLocateUserClick,

                            isTrackUserActive = mapLocationViewModelUiState.value.trackUserOnMap,

                            )
                    }

                    composable("route_planner") {
                        currentScreen.value = 1
                        RoutePlannerView(
                            destinations = routePlannerViewModelUiState.value.destinations,
                            clearAll = { routePlannerViewModel.clear() },
                            startTime = routePlannerViewModelUiState.value.startTime,
                            context = context,
                            enabledStartRoute = routePlannerViewModel.checkIfAllDestinationsHaveNames(),
                            routesAdded = routePlannerViewModel.checkIfSomeDestinationsHaveNames(),
                            isLoading = routePlannerViewModelUiState.value.isLoading,
                            popBackStack = {

                                if (navController.previousBackStackEntry != null) {
                                    navController.popBackStack()
                                } else {
                                    navController.navigate("home_screen")
                                }
                            },
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
                            startRoute = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    routePlannerViewModel.start(
                                        { navController.navigate("route_screen") },
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
                            removeDestination = { index ->
                                routePlannerViewModel.removeDestination(
                                    index
                                )
                            },
                            updateStartTime = { time: Calendar ->
                                routePlannerViewModel.updateStartTime(
                                    time
                                )
                            },
                            navigateTo = { screen -> navController.navigate(screen) },
                        )
                    }
                    composable("add_destination_screen") {
                        currentScreen.value = 2
                        AddDestinationView(
                            clearQuery = { addressDataViewModel.clearQuery() },
                            query = addressViewModelUiState.value.query,
                            clearResults = { addressDataViewModel.clearResults() },
                            addFormerAddress = { address ->
                                addressDataViewModel.addFormerAddress(
                                    address
                                )
                            },
                            formerAddresses = addressViewModelUiState.value.formerAddresses,
                            addresses = addressViewModelUiState.value.addresses,
                            setQuery = { query -> addressDataViewModel.setQuery(query) },
                            isFetching = addressViewModelUiState.value.isLoading,
                            getTotalDestinations = { routePlannerViewModel.getTotalDestinations() },
                            activeDestinationIndex = routePlannerViewModelUiState.value.activeDestinationIndex,
                            location = location,
                            popBackStack = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.popBackStack()
                                } else {
                                    navController.navigate("home_screen")
                                }
                            },
                            fetchAddressData = { query ->
                                CoroutineScope(Dispatchers.Main).launch {

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
                            removeDestination = { index ->
                                routePlannerViewModel.removeDestination(
                                    index
                                )
                            },
                            updateDestination = { index, destination ->
                                routePlannerViewModel.updateDestination(
                                    index,
                                    destination
                                )
                            },

                            )
                    }
                    composable("settings_screen") {
                        currentScreen.value = 3
                        SettingsView(
                            userName = settingsViewModelUiState.value.userName,
                            setUserName = { name -> settingsViewModel.setUserName(name) },
                            popBackStack = {
                                if (navController.previousBackStackEntry != null) {
                                    navController.popBackStack()
                                } else {
                                    navController.navigate("home_screen")
                                }
                            },
                            setDarkMode = { settingsViewModel.setDarkMode(it) },
                            darkMode = settingsViewModelUiState.value.darkMode
                        )
                    }

                    composable("route_screen") {
                        currentScreen.value = 4
                    }
                }


            },
            popBackStack = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                } else {
                    navController.navigate("home_screen")
                }
            },
            minHeight = if (currentScreen.value != 4) 0.dp else 200.dp,
            maxHeight = if (currentScreen.value != 4) 0.dp else 800.dp,
            editRoute = currentScreen.value == 4,
            header = {
                if (currentScreen.value == 4) {
                    Header(
                        modifier = Modifier.padding(16.dp),
                        onClick = {

                        if (navController.previousBackStackEntry != null) {
                            navController.popBackStack()
                        } else {
                            navController.navigate("home_screen")
                        }
                    })

                } else if (currentScreen.value == 0){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        WelcomeBadge(
                            userName = settingsViewModelUiState.value.userName,
                        )
                    }
                }
            }
        )

        val errorMessageId = routePlannerViewModelUiState.value.error
        val errorMessage = if (errorMessageId != null) stringResource(id = errorMessageId) else ""

        LaunchedEffect(errorMessage) {
            if (errorMessage.isNotBlank()){
                snackbarHostState.showSnackbar(errorMessage)
                routePlannerViewModel.clearError()
            }
        }

    }

}


