package com.example.motocast.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data model for the Mapbox API for directions data.
 * Link to the API documentation: [Mapbox Directions API](https://docs.mapbox.com/api/navigation/#directions)
 */
data class DirectionsDataModel(
    val routes: List<Route>,
    val waypoints: List<Waypoint>,
    val code: String,
    val uuid: String,
    val message: String? = null,
)

data class Route(
    @SerializedName("weight_name") val weightName: String,
    val weight: Double,
    val duration: Double,
    val distance: Double,
    val legs: List<Leg>,
    val geometry: RouteGeometry
)

data class Leg(
    @SerializedName("via_waypoints") val viaWaypoints: List<Any>,
    val admins: List<Admin>,
    val weight: Double,
    val duration: Double,
    val steps: List<Step>,
    val distance: Double,
    val summary: String
)

data class Admin(
    @SerializedName("iso_3166_1_alpha3") val iso3166_1Alpha3: String,
    @SerializedName("iso_3166_1") val iso3166_1: String
)

data class Step(
    val intersections: List<Intersection>,
    val maneuver: Maneuver,
    val name: String,
    val duration: Double,
    val distance: Double,
    @SerializedName("driving_side") val drivingSide: String,
    val weight: Double,
    val mode: String,
    val geometry: StepGeometry
)

data class Intersection(
    val bearings: List<Int>,
    val entry: List<Boolean>,
    @SerializedName("mapbox_streets_v8") val mapboxStreetsV8: Map<String, String>,
    @SerializedName("is_urban") val isUrban: Boolean,
    @SerializedName("admin_index") val adminIndex: Int,
    val out: Int,
    @SerializedName("geometry_index") val geometryIndex: Int,
    val location: List<Double>,
    val duration: Double?,
    @SerializedName("turn_weight") val turnWeight: Double?,
    @SerializedName("turn_duration") val turnDuration: Double?
)

data class Maneuver(
    val type: String,
    val instruction: String,
    @SerializedName("bearing_after") val bearingAfter: Int,
    @SerializedName("bearing_before") val bearingBefore: Int,
    val location: List<Double>,
    val modifier: String?
)

data class RouteGeometry(
    @SerializedName("coordinates") val coordinates: List<List<Double>>,
    val type: String
)

data class StepGeometry(
    @SerializedName("coordinates") val coordinates: List<List<Double>>,
    val type: String
)

data class Waypoint(
    val distance: Double,
    val name: String,
    val location: List<Double>
)

