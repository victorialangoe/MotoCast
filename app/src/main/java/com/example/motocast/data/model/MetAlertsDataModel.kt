package com.example.motocast.data.model

data class MetAlertsDataModel(
    val features: List<Feature>,
    val lang: String,
    val lastChange: String,
    val type: String
)

data class Geometry(
    val coordinates: List<List<List<Double>>>,
    val type: String
)

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String,
    val `when`: When
)

data class Properties(
    val area: String,
    val awarenessResponse: String,
    val awarenessSeriousness: String,
    val awareness_level: String,
    val awareness_type: String,
    val certainty: String,
    val consequences: String,
    val county: List<Any>,
    val description: String,
    val event: String,
    val eventAwarenessName: String,
    val eventEndingTime: String,
    val geographicDomain: String,
    val id: String,
    val instruction: String,
    val resources: List<Resource>,
    val severity: String,
    val title: String,
    val triggerLevel: String,
    val type: String
)

data class Resource(
    val description: String,
    val mimeType: String,
    val uri: String
)

data class When(
    val interval: List<String>
)