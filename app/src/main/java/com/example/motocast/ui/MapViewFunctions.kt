package com.example.motocast.ui

import android.content.Context
import android.content.pm.PackageManager



import androidx.core.app.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView


object MapViewFunctions : DefaultLifecycleObserver{

    private var mapView: MapView? = null

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
        mapView.controller.setZoom(10.0)
    }

    override fun onResume(owner: LifecycleOwner) {
        mapView?.onResume()
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
}


