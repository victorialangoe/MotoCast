package com.example.motocast.ui.view

import com.example.motocast.R

fun getWeatherIcon(event: String, awarenessLevel: String): Int {
    val eventIconMap = mapOf(
        "blowingSnow" to "icon_warning_snow_",
        "avalances" to "icon_warning_avalanches_",
        "drivingConditions" to "icon_warning_drivingconditions_",
        "flood" to "icon_warning_flood_",
        "forestFire" to "icon_warning_forestfire_",
        "gale" to "icon_warning_wind_",
        "ice" to "icon_warning_ice_",
        "icing" to "icon_warning_generic_",
        "landslide" to "icon_warning_landslide_",
        "polarLow" to "icon_warning_polarlow_",
        "rain" to "icon_warning_rain_",
        "rainFlood" to "icon_warning_rainflood_",
        "snow" to "icon_warning_snow_",
        "stormSurge" to "icon_warning_stormsurge_",
        "lightning" to "icon_warning_lightning_",
        "wind" to "icon_warning_wind_",
        "unknown" to "icon_warning_generic_"
    )

    val awarenessLevelIconMap = mapOf(
        "2; yellow; Moderate" to "yellow",
        "3; orange; Severe" to "orange"
    )

    val eventIcon = eventIconMap[event] ?: "icon_warning_generic_"
    val awarenessLevelIcon = awarenessLevelIconMap[awarenessLevel] ?: ""

    // Choose how to combine the event and awareness level icons, if needed
    val resourceName = "$eventIcon$awarenessLevelIcon"

    return try {
        val resourceId = R.drawable::class.java.getField(resourceName).getInt(null)
        resourceId
    } catch (e: Exception) {
        // Return default icon resource ID if there's an issue finding the resource
        throw Exception("Resource not found: $resourceName")
    }
}
