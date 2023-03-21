package com.example.motocast.navigation

data class FeatureCollection(
    val type: String,
    val features: List<Feature>,
    val bbox: List<Double>,
    val metadata: Metadata
)

data class Feature(
    val type: String,
    val properties: Properties,
    val geometry: Geometry,
    val bbox: List<Double>
)

data class Properties(
    val summary: Summary,
    val segments: List<Segment>,
    val way_points: List<Int>
)

data class Summary(
    val distance: Double,
    val duration: Double
)

data class Segment(
    val distance: Double,
    val duration: Double,
    val steps: List<Step>,
)

data class Step(
    val distance: Double,
    val duration: Double,
    val type: Int,
    val instruction: String,
    val name: String,
    val way_points: List<Int>
)
data class Geometry(
    val type: String,
    val coordinates: List<List<Double>>
)

data class Metadata(
    val attribution: String,
    val service: String,
    val timestamp: Long,
    val query: Query,
    val engine: Engine
)

data class Query(
    val coordinates: List<List<Double>>,
    val profile: String,
    val format: String
)

data class Engine(
    val version: String,
    val build_date: String,
    val graph_date: String
)
