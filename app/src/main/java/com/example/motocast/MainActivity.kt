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
import com.example.motocast.data.api.MetRetrofitHelper
import com.example.motocast.data.datasource.WeatherDataSource
import com.example.motocast.ui.theme.MotoCastTheme
import android.util.Log
import kotlin.math.log


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val weatherDataSource = WeatherDataSource()
        weatherDataSource.getLongTermWeatherData(59.9139, 10.7522, onSuccess = {
            Log.d("WeatherDataSource", "Weather data: $it")
        }, onError = {
            Log.d("WeatherDataSource", "Error: $it")
        })

        setContent {
            MotoCastTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Sander")
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