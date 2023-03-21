package com.example.motocast.ui

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.util.Log


import androidx.core.app.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.motocast.api.RetrofitHelper
import com.example.motocast.navigation.createGeoPoints
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline


object MapViewFunctions : DefaultLifecycleObserver{

    private var mapView: MapView? = null
    val testPoints = listOf(
        GeoPoint(59.943709982525775, 10.718339250322309),
        GeoPoint(59.943124473928115, 10.716092471366107),
        GeoPoint(59.94738871931273, 10.71856273091623),
        GeoPoint(59.9473772811635, 10.720215413624377),
        GeoPoint(59.94869783761205, 10.723426888704854),
        GeoPoint(59.94834431238808, 10.723614621991718)
    )


    fun initMapView(context: Context, mapView: MapView) {
        val sharedPref = context.getSharedPreferences(
            "${context.packageName}_preferences",
            Context.MODE_PRIVATE
        )


        // Handle permissions first, before map is created

        // Load/initialize the osmdroid configuration
        Configuration.getInstance().load(context, sharedPref)
        // Set the HTTP User Agent to your application's package name
        // to avoid being banned from osm tile servers
        Configuration.getInstance().userAgentValue = context.packageName

        // Set the tile source and zoom level
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.controller.setZoom(15.0)
        val startPoint = GeoPoint(59.943709982525775, 10.718339250322309) // Oslo, OJD
        mapView.controller.setCenter(startPoint)
    }

    override fun onResume(owner: LifecycleOwner) {
        mapView?.onResume()
        val retrofitHelper = RetrofitHelper()
        val directionApi = retrofitHelper.getDirectionApi()

        owner.lifecycleScope.launch() {
            try {
                // get list of GeoPoints
                val points = directionApi.getDirection(
                    "",
                    "10.717283277305567,59.942850342734545",
                    "10.733564274268417,59.9137501230344,"
                )
                val geoList = createGeoPoints(points)

                // display polyline on the map
                displayPolyline(geoList)

            } catch (e: Exception) {
                Log.e("MapViewFunctions", "Error getting directions", e)
            }
        }
}

    override fun onPause(owner: LifecycleOwner) {
        mapView?.onPause()
    }

    fun bindMapView(mapView: MapView, lifecycleOwner: LifecycleOwner) {
        this.mapView = mapView
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun unbindMapView(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.removeObserver(this)
        mapView = null
    }

    fun requestPermissionsIfNecessary(activity: ComponentActivity, permissions: Array<String>, requestCode: Int) {
        val permissionsToRequest = ArrayList<String>()
        for (permission in permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.isNotEmpty()) {
            activity.requestPermissions(permissionsToRequest.toTypedArray(), requestCode)
        }
    }

    fun handlePermissionsResult(grantResults: IntArray): Boolean {
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun displayPolyline(points: List<GeoPoint>){
        val polyline = Polyline().apply{
            points.forEach{
                addPoint(it)
            }
            outlinePaint.color = Color.RED
            outlinePaint.strokeWidth = 5f
        }
        mapView?.overlayManager?.add(polyline)
        mapView?.invalidate()
    }
}


