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
import com.example.motocast.data.datasource.MetAlertsDataSource
import com.example.motocast.data.datasource.NowCastDataSource
import com.example.motocast.ui.map.MapWrapper
import com.example.motocast.ui.theme.MotoCastTheme
import com.example.motocast.ui.view.MetAlertsScreen
import com.example.motocast.ui.view.WordAnimation


class MainActivity : ComponentActivity() {
    val viewModel = MetAlertsDataSource()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val NowCastDataSource = NowCastDataSource()
        NowCastDataSource.getNowCastData(
            latitude = 59.9333,
            longitude = 10.7166,
            onSuccess = {
                Log.d("NowCast", it.toString())
            },
            onError = {
                Log.d("NowCast Error", it.toString())
            }
        )

        setContent {
            MotoCastTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MetAlertsScreen(viewModel = viewModel)
                    //Remove check on line 33 and 19 to see preview.
                    //WordAnimation()
                    HomeBottomScaffoldView(content = {
                        MapWrapper()
                    })

                }
            }
        }
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