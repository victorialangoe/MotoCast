package com.example.motocast.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat

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
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Check if the user has granted location permission
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // If not, request the permission
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 6969)
        return
    }

    // Define a LocationListener to receive location updates
    val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Remove the listener to save battery and not receive more updates than needed (we only need one)
            locationManager.removeUpdates(this)
            onSuccess(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}

    }

    // Request location updates, run on the UI thread to avoid errors
    activity.runOnUiThread {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L,
                0f,
                locationListener
            )
        } catch (exception: SecurityException) {
            onError(exception)
        }
    }
}
