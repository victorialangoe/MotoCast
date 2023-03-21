package com.example.motocast.navigation
import org.osmdroid.util.GeoPoint


fun createGeoPoints(featureCollection: FeatureCollection): List<GeoPoint> {
    val geoPoints = mutableListOf<GeoPoint>()

    for (feature in featureCollection.features) {
        val geometry = feature.geometry
        if (geometry.type == "LineString") {
            for (coordinate in geometry.coordinates) {
                val geoPoint = GeoPoint(coordinate[1], coordinate[0]) // geopoints takes lat, lon but coordinates are lon, lat
                geoPoints.add(geoPoint)
            }
        }
    }

    return geoPoints
}
