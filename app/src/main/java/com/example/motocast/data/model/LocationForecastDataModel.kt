/**
 * Data class for the location forecast data from the Met API.
 * This data class is used to parse the JSON data from the Met API.
 */
data class LongTermWeatherData(
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)

data class Geometry(
    val type: String,
    val coordinates: List<Double>
)

data class Properties(
    val meta: Meta,
    val timeseries: List<TimeSeries>
)

data class Meta(
    val updated_at: String,
    val units: Units
)

data class Units(
    val air_pressure_at_sea_level: String,
    val air_temperature: String,
    val cloud_area_fraction: String,
    val precipitation_amount: String,
    val relative_humidity: String,
    val wind_from_direction: String,
    val wind_speed: String
)

data class TimeSeries(
    val time: String,
    val data: Data
)

data class Data(
    val instant: Instant,
    val next_12_hours: Summary,
    val next_1_hours: SummaryWithDetails?,
    val next_6_hours: SummaryWithDetails
)

data class Instant(
    val details: Details
)

data class Details(
    val air_pressure_at_sea_level: Double,
    val air_temperature: Double,
    val cloud_area_fraction: Double,
    val relative_humidity: Double,
    val wind_from_direction: Double,
    val wind_speed: Double
)

data class Summary(
    val symbol_code: String
)

data class SummaryWithDetails(
    val summary: Summary,
    val details: DetailsForNextHours
)

data class DetailsForNextHours(
    val precipitation_amount: Double
)