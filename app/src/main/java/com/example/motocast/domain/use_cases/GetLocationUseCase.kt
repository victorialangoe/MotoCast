package com.example.motocast.domain.use_cases

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.motocast.MainActivity
import com.example.motocast.domain.repository.MotoCastRepository
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class GetLocationUseCase(
    private val repository: MotoCastRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
): LocationCallback() {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Location? {
        val cancellationTokenSource = CancellationTokenSource()

        if (!checkPermission()) {
            getPermission()
        }

        return suspendCancellableCoroutine { continuation ->
            fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
                .addOnSuccessListener { location ->
                    continuation.resume(location
                    ) { throwable -> throwable is CancellationException }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWith(Result.failure(exception))
                }

            continuation.invokeOnCancellation {
                cancellationTokenSource.cancel()
            }
        }.also {
            cancellationTokenSource.cancel()
        }
    }



    private suspend fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            repository.getAppContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private suspend fun getPermission() {
        ActivityCompat.requestPermissions(
            repository.getAppContext() as MainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            6969
        )
    }

    override fun onLocationResult(result: LocationResult) {}


    override fun onLocationAvailability(availability: LocationAvailability) {}

}