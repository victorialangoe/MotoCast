package com.example.motocast

import android.content.Context
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.motocast.ui.theme.MotoCastTheme
import com.example.motocast.ui.view.home_bottom_scaffold.HomeBottomScaffoldView
import com.example.motocast.ui.view.inputs.InputScreen
import com.example.motocast.ui.view.map.MapView
import com.example.motocast.ui.view.route_planner.RoutePlannerView
import com.example.motocast.ui.view.route_scaffold.RouteScaffoldView
import com.example.motocast.ui.viewmodel.inputs.InputViewModel
import com.example.motocast.ui.viewmodel.map.MapViewModel
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel


class MainActivity : ComponentActivity() {

    private lateinit var nowCastViewModel: NowCastViewModel
    private lateinit var mapViewModel: MapViewModel
    private lateinit var inputViewModel: InputViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotoCastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val activity = this
                    //HomeBottomScaffoldView(content = {
                    //                        MapView(
                    //                            viewModel = mapViewModel, activity = this
                    //                        )
                    //                    })
                    /*
                    RouteScaffoldView(content = {
                        MapView(
                            viewModel = mapViewModel, activity = this
                        )
                    })


                    InputScreen(inputViewModel = inputViewModel)
                    */

                    MotoCastApp(
                        mapViewModel = mapViewModel,
                        nowCastViewModel = nowCastViewModel,
                        inputViewModel = inputViewModel,
                        activity = this
                    )

                }
            }
        }

        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]

        inputViewModel = ViewModelProvider(this)[InputViewModel::class.java]

        // Initialize the NowCastViewModel and start fetching data
        nowCastViewModel = ViewModelProvider(this)[NowCastViewModel::class.java]
        nowCastViewModel.startFetchingNowCastData(this)


    }

    override fun onResume() {
        super.onResume()

        // Start fetching data when the user resumes the activity
        //nowCastViewModel.startFetchingNowCastData(this)

    }

    override fun onPause() {
        super.onPause()

        // Stop fetching data when the user pauses the activity
        //nowCastViewModel.stopFetchingNowCastData()
    }
}

@Composable
fun MotoCastApp(
    mapViewModel: MapViewModel,
    nowCastViewModel: NowCastViewModel,
    inputViewModel: InputViewModel,
    activity: MainActivity,
    context: Context = androidx.compose.ui.platform.LocalContext.current
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home_bottom_scaffold_view") {
        composable("home_bottom_scaffold_view") {
            HomeBottomScaffoldView(
                context = context,
                nowCastViewModel = nowCastViewModel,
                content = { MapView(viewModel = mapViewModel, activity = activity)},
                onNavigateToScreen = {
                    navController.navigate("input_screen")
                }
            )
        }
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
                        viewModel = mapViewModel, activity = activity
                    )
                },
                onNavigateToScreen = {
                    navController.navigate("input_screen")
                }
            )
        }
    }
}