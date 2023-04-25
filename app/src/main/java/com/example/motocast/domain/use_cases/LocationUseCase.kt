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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Fetches location from the repository. THIS DOES NOT WORK YET (Hopefully it will in the future)
 *
 * @param repository The repository to fetch the location from, as a [MotoCastRepository]
 * @fuseLocationProviderClient The [fusedLocationProviderClient] to get the location from
 * @return location as a [Location] or null
 */
class LocationUseCase(
    private val repository: MotoCastRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    ) : LiveData<Location>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { continuation ->
        if (ActivityCompat.checkSelfPermission(
                repository.appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                repository.appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("LocationUseCase", "getCurrentLocation: no permission")
            continuation.resume(null, null)
            return@suspendCancellableCoroutine
        }


        fusedLocationProviderClient.lastLocation.addOnCompleteListener() { task ->
            if (task.isSuccessful && task.result != null) {
                Log.d("LocationUseCase", "getCurrentLocation: ${task.result}")
                continuation.resume(task.result, null)
            } else {
                Log.d("LocationUseCase", "getCurrentLocation: ${task.exception}")
                continuation.resume(null, null)
            }
        }
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
                Log.d("LocationUseCase", "onActive: $it")
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
        Log.d("LocationUseCase", "startLocationUpdates: ")
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun setLocationData(location: Location?) {
        location?.let {
            Log.d("LocationUseCase", "setLocationData: $it")
            value = it
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult ?: return
            for (location in locationResult.locations) {
                Log.d("LocationUseCase", "onLocationResult: $location")
                setLocationData(location)
            }
        }
    }

    companion object {
        private const val timeInterval = 1000L // 1 second
        private const val minimalDistance = 0f

        val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, timeInterval).apply {
            setMinUpdateDistanceMeters(minimalDistance)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()
    }
}
