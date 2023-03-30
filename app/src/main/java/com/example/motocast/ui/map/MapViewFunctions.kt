package com.example.motocast.ui.map

import androidx.lifecycle.LifecycleOwner

object MapViewFunctions : DefaultLifecycleObserver{




    fun initMapView(context: Context, mapView: MapView) {
        val sharedPref = context.getSharedPreferences(
            "${context.packageName}_preferences",
            Context.MODE_PRIVATE
        )

    }

    override fun onResume(owner: LifecycleOwner) {
        mapView?.onResume()

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