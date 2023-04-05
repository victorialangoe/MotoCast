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
import androidx.lifecycle.ViewModelProvider
import com.example.motocast.ui.theme.MotoCastTheme
import com.example.motocast.ui.view.home_bottom_scaffold.HomeBottomScaffoldView
import com.example.motocast.ui.view.map.MapView
import com.example.motocast.ui.viewmodel.location.LocationViewModel
import com.example.motocast.ui.viewmodel.map.MapViewModel
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel


class MainActivity : ComponentActivity() {

    private lateinit var nowCastViewModel: NowCastViewModel
    private lateinit var mapViewModel: MapViewModel
    private lateinit var locationViewModel: LocationViewModel

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
                        mapViewModel = mapViewModel,
                        locationViewModel = locationViewModel,
                        content = {
                            MapView(
                                viewModel = mapViewModel,
                                locationViewModel = locationViewModel,
                                activity = this,
                            )
                        })
                }
            }
        }



        // Initialize the map
        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]

        // Initialize the LocationManager
        locationViewModel = LocationViewModel(
            activity = this,
            timeInterval = 5000,
            minimalDistance = 0f,
            mapViewModel = mapViewModel
        )
        locationViewModel.startLocationTracking()

        // Initialize the NowCastViewModel and start fetching data
        nowCastViewModel = ViewModelProvider(this)[NowCastViewModel::class.java]
        nowCastViewModel.startFetchingNowCastData(this)


    }

    override fun onResume() {
        super.onResume()

        // Start fetching data when the user resumes the activity
        nowCastViewModel.startFetchingNowCastData(this)

        locationViewModel.startLocationTracking()

    }

    override fun onPause() {
        super.onPause()

        // Stop fetching data when the user pauses the activity
        nowCastViewModel.stopFetchingNowCastData()
        locationViewModel.stopLocationTracking()
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