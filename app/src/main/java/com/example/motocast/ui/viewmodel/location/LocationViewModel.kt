package com.example.motocast.ui.viewmodel.location

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.motocast.ui.viewmodel.map.MapViewModel
import com.google.android.gms.location.*

/*
It provides a simple API for getting locations with high, medium, and low accuracy.
It also optimizes the device’s use of battery power. So we should prefer this method.
 */

class LocationViewModel(
    private val activity: Activity,
    private var timeInterval: Long,
    private var minimalDistance: Float,
    private val mapViewModel: MapViewModel

) : LocationCallback() {

    private val locationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(activity.applicationContext)
    }
    private val request: LocationRequest by lazy {
        createRequest()
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            6969
        )
    }


    private fun createRequest(): LocationRequest =
        // New builder
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
            setMinUpdateDistanceMeters(minimalDistance)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

    fun startLocationTracking() {
        if (checkPermission()) {
            locationClient.requestLocationUpdates(request, this, Looper.getMainLooper())
        } else {
            getPermission()
        }
    }


    fun stopLocationTracking() {
        locationClient.flushLocations()
        locationClient.removeLocationUpdates(this)
    }

    fun getCurrentLocation(
        onSuccess: (location: Location) -> Unit,
        onError: (exception: Exception) -> Unit
    ) {
        if (checkPermission()) {
            locationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        onSuccess(it)
                    } ?: run {
                        onError(Exception("No location found"))
                    }
                }
                .addOnFailureListener { exception ->
                    onError(exception)
                }
        } else {
            getPermission()
        }
    }

    override fun onLocationResult(result: LocationResult) {
        super.onLocationResult(result)
        Log.d("LocationViewModel", "onLocationResult: ${result.lastLocation}")
        mapViewModel.cameraToUserLocation(this)
    }

    override fun onLocationAvailability(availability: LocationAvailability) {
        if (!checkPermission()) {
            locationClient.requestLocationUpdates(request, this, Looper.getMainLooper())
        }
    }

}
