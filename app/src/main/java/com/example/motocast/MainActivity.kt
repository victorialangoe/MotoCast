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
import com.example.motocast.ui.view.map.MapView
import com.example.motocast.ui.view.route_scaffold.RouteScaffoldView
import com.example.motocast.ui.viewmodel.map.MapViewModel
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel
import com.example.motocast.ui.viewmodel.user.UserViewModel


class MainActivity : ComponentActivity() {

    private lateinit var nowCastViewModel: NowCastViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var mapViewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotoCastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //HomeBottomScaffoldView(content = {
                    //                        MapView(
                    //                            viewModel = mapViewModel, activity = this
                    //                        )
                    //                    })

                    RouteScaffoldView(content = {
                        MapView(
                            viewModel = mapViewModel, activity = this
                        )
                    })


                }
            }
        }

        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]

        // Initialize the UserViewModel and start fetching data
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.startFetchingUserData(this)

        // Initialize the NowCastViewModel and start fetching data
        nowCastViewModel = NowCastViewModel(userViewModel = userViewModel)
        nowCastViewModel.startFetchingNowCastData()


    }

    override fun onResume() {
        super.onResume()

        // Start fetching data when the user resumes the activity
        userViewModel.startFetchingUserData(this)
        nowCastViewModel.startFetchingNowCastData()

    }

    override fun onPause() {
        super.onPause()

        // Stop fetching data when the user pauses the activity
        userViewModel.stopFetchingUserData()
        nowCastViewModel.stopFetchingNowCastData()
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