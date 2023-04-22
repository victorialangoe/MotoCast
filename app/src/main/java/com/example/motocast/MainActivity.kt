package com.example.motocast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.example.motocast.ui.view.AppNavigation
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel
import com.example.motocast.ui.viewmodel.settings.SettingsViewModel
import com.example.motocast.ui.viewmodel.weather.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var mapLocationViewModel: MapLocationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppNavigation(
                context = this,
            )
        }


       // weatherViewModel = WeatherViewModel()

       // weatherViewModel.startFetchingNowCastData { mapLocationViewModel.getCurrentLocation() }

    }

    override fun onResume() {
        super.onResume()
        // Start fetching data when the user resumes the activity
       // weatherViewModel.startFetchingNowCastData { mapLocationViewModel.getCurrentLocation() }

    }

    override fun onPause() {
        super.onPause()
        // Stop fetching data when the user pauses the activity
       // weatherViewModel.stopFetchingNowCastData()
    }
}
