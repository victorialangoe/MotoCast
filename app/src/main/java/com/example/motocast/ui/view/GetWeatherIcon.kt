package com.example.motocast.ui.view

fun getWeatherIcon(event: String, awarenessLevel: String): String {
    val eventIconMap = mapOf(
        "blowingSnow" to "icon_warning_snow_",
        "avalances" to "icon-warning-avalanches_",
        "drivingConditions" to "icon-warning-drivingconditions_",
        "flood" to "icon-warning-flood_",
        "forestFire" to "icon-warning-forestfire_",
        "gale" to "icon-warning-wind_",
        "ice" to "icon-warning-ice_",
        "icing" to "icon-warning-generic_",
        "landslide" to "icon-warning-landslide_",
        "polarLow" to "icon-warning-polarlow_",
        "rain" to "icon-warning-rain_",
        "rainFlood" to "icon-warning-rainflood_",
        "snow" to "icon-warning-snow_",
        "stormSurge" to "icon-warning-stormsurge_",
        "lightning" to "icon-warning-lightning_",
        "wind" to "icon-warning-wind_",
        "unknown" to "icon-warning-generic_"
    )

    val awarenessLevelIconMap = mapOf(
        "2; yellow; Moderate" to "yellow",
        "3; orange; Severe" to "orange",
    )

    val eventIcon = eventIconMap[event] ?: null
    val awarenessLevelIcon = awarenessLevelIconMap[awarenessLevel] ?: null


    // Choose how to combine the event and awareness level icons, if needed
    return "R.drawable.$eventIcon$awarenessLevelIcon"
}