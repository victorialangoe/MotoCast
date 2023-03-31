package com.example.motocast

import HomeBottomScaffoldView
import android.os.Bundle
import android.util.Log
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
import com.example.motocast.data.datasource.MetAlertsDataSource
import com.example.motocast.data.datasource.NowCastDataSource
import com.example.motocast.ui.map.MapWrapper
import com.example.motocast.ui.theme.MotoCastTheme
import com.example.motocast.ui.view.MetAlertsScreen
import com.example.motocast.ui.view.WordAnimation
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel
import com.example.motocast.ui.viewmodel.user.UserViewModel


class MainActivity : ComponentActivity() {

    private lateinit var nowCastViewModel: NowCastViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotoCastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeBottomScaffoldView(content =
                        {MapWrapper()}
                    )
                }
            }
        }


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