package com.example.motocast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.motocast.ui.theme.AppTheme
import com.example.motocast.ui.view.WordAnimation
import com.example.motocast.ui.view.rememberAnimationState
import com.example.motocast.ui.view.AppNavigation
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import com.example.motocast.ui.viewmodel.weather.WeatherViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.ui.viewmodel.settings.SettingsViewModel


class MainActivity : ComponentActivity() {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var mapLocationViewModel: MapLocationViewModel
    private lateinit var routePlannerViewModel: RoutePlannerViewModel
    private lateinit var addressDataViewModel: AddressDataViewModel
    private lateinit var settingsViewModel: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                val animationState = rememberAnimationState()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /*
                    AnimatedVisibility(
                        visible = !animationState.value,
                        exit = fadeOut(animationSpec = tween(durationMillis = 500))
                    ) {
                        WordAnimation()
                    }

                     */
                    Crossfade(targetState = animationState.value, animationSpec = tween(durationMillis = 500)) { isAnimationComplete ->
                        if (isAnimationComplete) {
                            AppNavigation(
                                mapLocationViewModel = mapLocationViewModel,
                                weatherViewModel = weatherViewModel,
                                routePlannerViewModel = routePlannerViewModel,
                                addressDataViewModel = addressDataViewModel,
                                settingsViewModel = settingsViewModel,
                                context = this,
                            )
                        }
                    }
                }

        }

        settingsViewModel = SettingsViewModel(this.applicationContext)
        addressDataViewModel = AddressDataViewModel()
        routePlannerViewModel = RoutePlannerViewModel()
        weatherViewModel = WeatherViewModel()
        mapLocationViewModel = MapLocationViewModel(activity = this)
        mapLocationViewModel.startLocationTracking()
        weatherViewModel.startFetchingNowCastData { mapLocationViewModel.getCurrentLocation() }

    }

    override fun onResume() {
        super.onResume()
        // Start fetching data when the user resumes the activity
        weatherViewModel.startFetchingNowCastData { mapLocationViewModel.getCurrentLocation() }
        mapLocationViewModel.startLocationTracking()

    }

    override fun onPause() {
        super.onPause()
        // Stop fetching data when the user pauses the activity
        weatherViewModel.stopFetchingNowCastData()
        mapLocationViewModel.stopLocationTracking()
    }
}
