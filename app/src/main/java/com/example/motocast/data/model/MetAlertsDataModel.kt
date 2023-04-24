package com.example.motocast.data.model

/**
 * Data model for the Metalerts API.
 * Link to the API documentation: [Metalerts API](https://api.met.no/weatherapi/metalerts/1.1/documentation)
 */
data class MetAlertsDataModel(
    val features: List<Feature>,
    val lang: String,
    val lastChange: String,
    val type: String
)

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String,
    val `when`: When
)

data class Geometry(
    val coordinates: List<List<List<Double>>>,
    val type: String
)

data class Properties(
    val area: String,
    val awarenessResponse: String,
    val awarenessSeriousness: String,
    val awareness_level: String,
    val awareness_type: String,
    val certainty: String,
    val consequences: String,
    val county: List<String>,
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