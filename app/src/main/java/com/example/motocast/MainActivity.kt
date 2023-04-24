package com.example.motocast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import com.example.motocast.ui.viewmodel.current_weather.CurrentWeatherViewModel
import com.example.motocast.ui.viewmodel.map.MapViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.ui.viewmodel.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /*
    We store the weatherViewModel in a mutableStateOf because we need to be able to
    start and stop the fetching of the now cast data when the app is paused and resumed.
     */
    private val weatherViewModel = mutableStateOf<CurrentWeatherViewModel?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val addressDataViewModel = hiltViewModel<AddressDataViewModel>()
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val routePlannerViewModel = hiltViewModel<RoutePlannerViewModel>()
            val mapViewModel = hiltViewModel<MapViewModel>()

            weatherViewModel.value = hiltViewModel()
            weatherViewModel.value?.startFetchingNowCastData()

            AppNavigation(
                addressDataViewModel = addressDataViewModel,
                settingsViewModel = settingsViewModel,
                weatherViewModel = weatherViewModel.value!!,
                routePlannerViewModel = routePlannerViewModel,
                mapViewModel = mapViewModel,
                context = this,
            )
        }


    }

    override fun onResume() {
        super.onResume()
        weatherViewModel.value?.startFetchingNowCastData()
    }

    override fun onPause() {
        super.onPause()
        weatherViewModel.value?.stopFetchingNowCastData()
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherViewModel.value?.stopFetchingNowCastData()
    }
}
