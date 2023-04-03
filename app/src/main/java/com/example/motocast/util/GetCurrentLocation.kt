package com.example.motocast.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

/**
 * Get the current location of the user
 *
 * @param activity The activity that is requesting the location
 * @param context The context of the activity
 * @param onSuccess A callback function that is called when the location is received
 * @param onError A callback function that is called when an error occurs
 */
fun getCurrentLocation(
    activity: Activity,
    context: Context,
    onSuccess: (location: Location) -> Unit,
    onError: (exception: Exception) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 6969)
        return
    }

    fusedLocationClient.lastLocation
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
}
