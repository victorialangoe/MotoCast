package com.example.motocast

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.motocast.ui.theme.MotoCastTheme
import com.example.motocast.ui.view.home_bottom_scaffold.HomeBottomScaffoldView
import com.example.motocast.ui.view.inputs.InputScreen
import com.example.motocast.ui.view.map.MapView
import com.example.motocast.ui.view.route_scaffold.RouteScaffoldView
import com.example.motocast.ui.viewmodel.address.AddressDataViewModel
import com.example.motocast.ui.viewmodel.inputs.InputViewModel
import com.example.motocast.ui.viewmodel.mapLocationViewModel.MapLocationViewModel
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel


class MainActivity : ComponentActivity() {

    private lateinit var nowCastViewModel: NowCastViewModel
    private lateinit var mapLocationViewModel: MapLocationViewModel
    private lateinit var inputViewModel: InputViewModel
    private lateinit var addressDataViewModel: AddressDataViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotoCastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MotoCastApp(
                        mapLocationViewModel = mapLocationViewModel,
                        nowCastViewModel = nowCastViewModel,
                        inputViewModel = inputViewModel,
                        activity = this,
                        context = this.applicationContext
                    )

                }
            }
        }

        addressDataViewModel = AddressDataViewModel()
        inputViewModel = InputViewModel()

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

@Composable
fun MotoCastApp(
    mapLocationViewModel: MapLocationViewModel,
    nowCastViewModel: NowCastViewModel,
    inputViewModel: InputViewModel,
    activity: MainActivity,
    context: Context
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home_bottom_scaffold_view") {
        composable("home_bottom_scaffold_view") {
            HomeBottomScaffoldView(
                context = context,
                nowCastViewModel = nowCastViewModel,
                mapLocationViewModel = mapLocationViewModel,
                content = {
                    MapView(
                        mapLocationViewModel = mapLocationViewModel,
                        activity = activity
                    )
                },
                onNavigateToScreen = {
                    navController.navigate("input_screen")
                })        }
        composable("input_screen") {
            InputScreen(
                inputViewModel = inputViewModel,
                onNavigateToScreen = {
                    navController.navigate("route_scaffold")
                }
            )
        }
        composable("route_scaffold") {
            RouteScaffoldView(
                content = {
                    MapView(
                        mapLocationViewModel = mapLocationViewModel,
                        activity = activity
                    )
                },
                onNavigateToScreen = {
                    navController.navigate("input_screen")
                }
            )
        }
    }
}