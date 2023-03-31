package com.example.motocast

import com.example.motocast.ui.view.home_bottom_scaffold.HomeBottomScaffoldView
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
import com.example.motocast.ui.map.MapWrapper
import com.example.motocast.ui.theme.MotoCastTheme
import com.example.motocast.ui.viewmodel.nowcast.NowCastViewModel


class MainActivity : ComponentActivity() {
    private lateinit var nowCastViewModel: NowCastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotoCastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeBottomScaffoldView(
                        this.applicationContext,
                        nowCastViewModel = nowCastViewModel,
                        content =
                        {MapWrapper()}
                    )
                }
            }
        }

        nowCastViewModel = ViewModelProvider(this)[NowCastViewModel::class.java]
        nowCastViewModel.startFetchingNowCastData(this)
    }

    override fun onResume() {
        super.onResume()

        nowCastViewModel.startFetchingNowCastData(this)

    }

    override fun onPause() {
        super.onPause()

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