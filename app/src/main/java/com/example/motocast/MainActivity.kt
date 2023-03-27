package com.example.motocast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
//import com.example.motocast.data.datasource.MetAlertsDataSource uncheck if you want to test API
import com.example.motocast.ui.theme.MotoCastTheme
import com.example.motocast.ui.view.WordAnimation

//import com.example.motocast.ui.view.MetAlertsScreen


class MainActivity : ComponentActivity() {
    //val viewModel = MetAlertsDataSource()



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            MotoCastTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //MetAlertsScreen(viewModel = viewModel)
                    // if you want to see how the API works just uncheck on line 24 and 39
                    //WordAnimation()
                    // if you want to view the animation uncheck this
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



