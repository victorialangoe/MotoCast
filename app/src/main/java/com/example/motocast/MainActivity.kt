package com.example.motocast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.motocast.ui.theme.MotoCastTheme
import com.example.motocast.ui.view.home_bottom_scaffold.HomeBottomScaffoldView
import com.example.motocast.ui.view.map.MapView
import com.example.motocast.ui.view.route_planner.RoutePlannerView
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

                    HomeBottomScaffoldView(context = this.applicationContext,
                        nowCastViewModel = nowCastViewModel,
                        mapLocationViewModel = mapLocationViewModel,
                        content = {
                            MapView(
                                mapLocationViewModel = mapLocationViewModel,
                                activity = this,
                            )
                        })

                }
            }
        }

        addressDataViewModel = AddressDataViewModel()


        nowCastViewModel = NowCastViewModel()
        routePlannerViewModel = RoutePlannerViewModel()
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
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MotoCastTheme {
        Greeting("Android")
    }
}