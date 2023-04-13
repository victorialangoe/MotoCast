package com.example.motocast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.motocast.ui.theme.MotoCastTheme
import com.example.motocast.ui.view.AppNavigation
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel
import com.example.motocast.ui.viewmodel.route_planner.RoutePlannerViewModel


class MainActivity : ComponentActivity() {

    private lateinit var nowCastViewModel: NowCastViewModel
    private lateinit var mapLocationViewModel: MapLocationViewModel
    private lateinit var routePlannerViewModel: RoutePlannerViewModel
    private lateinit var addressDataViewModel: AddressDataViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotoCastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        mapLocationViewModel = mapLocationViewModel,
                        nowCastViewModel = nowCastViewModel,
                        routePlannerViewModel = routePlannerViewModel,
                        addressDataViewModel = addressDataViewModel,
                        activity = this,
                        context = this
                        )
                }
            }
        }

        addressDataViewModel = AddressDataViewModel()
        routePlannerViewModel = RoutePlannerViewModel()
        nowCastViewModel = NowCastViewModel()
        mapLocationViewModel = MapLocationViewModel(
            activity = this,
            timeInterval = 5000, // 5 seconds
            minimalDistance = 100f, // 100 m
            bigDistanceChange = 100_000f, // 100 km
            nowCastViewModel = nowCastViewModel
        )
        mapLocationViewModel.startLocationTracking()
        nowCastViewModel.startFetchingNowCastData(mapLocationViewModel)

    }

    override fun onResume() {
        super.onResume()
        // Start fetching data when the user resumes the activity
        nowCastViewModel.startFetchingNowCastData(mapLocationViewModel)
        mapLocationViewModel.startLocationTracking()
    }

    override fun onPause() {
        super.onPause()
        // Stop fetching data when the user pauses the activity
        nowCastViewModel.stopFetchingNowCastData()
        mapLocationViewModel.stopLocationTracking()
    }
}

