package com.example.motocast

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.motocast.domain.use_cases.LocationUseCase
import com.example.motocast.ui.viewmodel.current_weather.CurrentWeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var locationUseCase: LocationUseCase
    private val weatherViewModel = mutableStateOf<CurrentWeatherViewModel?>(null)
    private fun requestSinglePermissionLauncher() =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("MainActivity", "Location permission granted")
                requestLocationUpdates()
            } else {
                Log.d("MainActivity", "Location permission not granted")
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepLocationUpdates()

        setContent {
            val location by locationUseCase.observeAsState()

            weatherViewModel.value = hiltViewModel()
            AppNavigation(
                weatherViewModel = weatherViewModel.value!!,
                context = this,
                location = location
            )
        }
    }

    override fun onResume() {
        super.onResume()
        weatherViewModel.value?.startFetchingNowCastData()
    }

    override fun onPause() {
        super.onPause()
        weatherViewModel.value?.stopFetchingNowCastData()
    }

    override fun onDestroy() {
        super.onDestroy()
        weatherViewModel.value?.stopFetchingNowCastData()
    }

    private fun requestLocationUpdates() {
        Log.d("MainActivity", "Requesting location updates")
        locationUseCase.startLocationUpdates()
        Log.d("Location", "Location: $locationUseCase")
    }

    private fun prepLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("MainActivity", "Location permission granted")
            requestLocationUpdates()
        } else {
            Log.d("MainActivity", "Location permission not granted")
            requestSinglePermissionLauncher().launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}


