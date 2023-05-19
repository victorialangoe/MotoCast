package com.example.motocast.domain.use_cases

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.example.motocast.data.repository.MotoCastRepository
import com.google.android.gms.location.*

/**
 * Fetches location from the repository.
 *
 * @param repository The repository to fetch the location from, as a [MotoCastRepository]
 * @fuseLocationProviderClient The [fusedLocationProviderClient] to get the location from
 * @return location as a [Location] or null
 */

class LocationUseCase(
    private val repository: MotoCastRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    ) : LiveData<Location>() {

    fun getCurrentLocation(): Location? {
        return value
    }

    override fun onActive() {
        super.onActive()
        if (ActivityCompat.checkSelfPermission(
                repository.appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                repository.appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("LocationUseCase", "onActive: no permission")
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location.also {
                setLocationData(it)
            }
        }

        startLocationUpdates()
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    fun startLocationUpdates(){

        if (ActivityCompat.checkSelfPermission(
                repository.appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                repository.appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("LocationUseCase", "startLocationUpdates: no permission")
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun setLocationData(location: Location?) {
        location?.let {
            value = it
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    companion object {
        private const val timeInterval = 1000L
        private const val minimalDistance = 0f

        val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
            setMinUpdateDistanceMeters(minimalDistance)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()
    }
}
