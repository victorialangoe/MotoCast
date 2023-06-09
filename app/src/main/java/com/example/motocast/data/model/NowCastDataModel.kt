package com.example.motocast.data.model

/**
 * Data model for the Nowcast API.
 * Link to the API documentation: [Nowcast API](https://api.met.no/weatherapi/nowcast/2.0/documentation)
 */
data class NowCastDataModel(
    val type: String,
    val geometry: GeometryNowCast,
    val properties: PropertiesNowCast
)

data class GeometryNowCast(
    val type: String,
    val coordinates: List<Double>
)

data class PropertiesNowCast(
    val meta: Meta,
    val timeseries: List<Timeseries>
)

data class Meta(
    val updated_at: String,
    val units: Units,
    val radar_coverage: String
)

data class Units(
    val air_temperature: String,
    val precipitation_amount: String,
    val precipitation_rate: String,
    val relative_humidity: String,
    val wind_from_direction: String,
    val wind_speed: String,
    val wind_speed_of_gust: String
)

data class Timeseries(
    val time: String,
    val data: Data
)

data class Data(
    val instant: Instant,
    val next_1_hours: NextOneHours
)

data class Instant(
    val details: Details
)

data class Details(
    val air_temperature: Double,
    val relative_humidity: Double,
    val wind_from_direction: Double,
    val wind_speed: Double,
    val wind_speed_of_gust: Double
)

data class NextOneHours(
    val summary: Summary,
    val details: Details2
)

data class Summary(
    val symbol_code: String
)

data class Details2(
    val precipitation_amount: Double
)