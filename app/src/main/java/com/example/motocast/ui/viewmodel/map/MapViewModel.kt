package com.example.motocast.ui.viewmodel.map

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.motocast.util.getCurrentLocation
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.easeTo
import com.mapbox.maps.plugin.animation.flyTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.min


@SuppressLint("StaticFieldLeak")
class MapViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState

    fun updateIsLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    fun addMapView(mapView: MapView) {
        _uiState.value = _uiState.value.copy(mapView = mapView)
    }

    fun cameraToUserLocation(activity: Activity) {
        if (_uiState.value.mapView != null) {
            val mapboxMap = _uiState.value.mapView!!.getMapboxMap()
            Log.d("MapActivity", "Camera to user location")

            CoroutineScope(Dispatchers.IO).launch {
                Log.d("MapActivity", "CorutineScope")
                // Get the current location using the getCurrentLocation function
                getCurrentLocation(
                    activity = activity,
                    context = activity.applicationContext,
                    onSuccess = { location ->
                        Log.d("MapViewModel", "Location: ehiehieheih")
                        // Create a CameraOptions object with the user's location as the center
                        val currentCameraPosition = mapboxMap.cameraState
                        val targetCameraPosition = CameraOptions.Builder()
                            .center(Point.fromLngLat(location.longitude, location.latitude))
                            .zoom(15.0)
                            .build()

                        val results = FloatArray(1)

                        Location.distanceBetween(
                            currentCameraPosition.center.latitude(),
                            currentCameraPosition.center.longitude(),
                            targetCameraPosition.center?.latitude() ?: 0.0,
                            targetCameraPosition.center?.longitude() ?: 0.0,
                            results // distanceBetween needs a float array to store the result
                        )
                        val distance = results[0].toDouble()

                        val duration = min(distance / 1000.0, 10.0).toLong() * 1000L // max duration of 10 seconds

                        val cameraOptions = CameraOptions.Builder()
                            .center(targetCameraPosition.center)
                            .zoom(targetCameraPosition.zoom)
                            .build()

                        val mapAnimationOptions = MapAnimationOptions.Builder()
                            .duration(duration)
                            .build()

                        /// stars an animation to smoothly go to the user, easeTo is way to laggy

                        mapboxMap.flyTo(cameraOptions, mapAnimationOptions)


                    },
                    onError = { exception ->
                        // Handle the error, e.g., show a message to the user or log the error
                        Log.d("MapViewModel", "Error: ${exception.localizedMessage}")
                    }
                )
                Log.d("MapViewModel", "ferdigs")
            }
        }
    }
}