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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.motocast.ui.theme.MotoCastTheme
import com.example.motocast.ui.view.WordAnimation
import com.example.motocast.ui.view.rememberAnimationState
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
                val animationState = rememberAnimationState()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    AnimatedVisibility(
                        visible = !animationState.value,
                        exit = fadeOut(animationSpec = tween(durationMillis = 500))
                    ) {
                        WordAnimation()
                    }
                    Crossfade(targetState = animationState.value, animationSpec = tween(durationMillis = 500)) { isAnimationComplete ->
                        if (isAnimationComplete) {
                            AppNavigation(
                                mapLocationViewModel = mapLocationViewModel,
                                nowCastViewModel = nowCastViewModel,
                                routePlannerViewModel = routePlannerViewModel,
                                addressDataViewModel = addressDataViewModel,
                                context = this
                            )
                        }
                    }
                }
            }
        }


        addressDataViewModel = AddressDataViewModel()
        routePlannerViewModel = RoutePlannerViewModel()
        nowCastViewModel = NowCastViewModel()
        mapLocationViewModel = MapLocationViewModel(activity = this)
        mapLocationViewModel.startLocationTracking()
        nowCastViewModel.startFetchingNowCastData { mapLocationViewModel.getCurrentLocation() }

    }

    override fun onResume() {
        super.onResume()
        // Start fetching data when the user resumes the activity
        nowCastViewModel.startFetchingNowCastData { mapLocationViewModel.getCurrentLocation() }
        mapLocationViewModel.startLocationTracking()
    }

    override fun onPause() {
        super.onPause()
        // Stop fetching data when the user pauses the activity
        nowCastViewModel.stopFetchingNowCastData()
        mapLocationViewModel.stopLocationTracking()
    }
}
