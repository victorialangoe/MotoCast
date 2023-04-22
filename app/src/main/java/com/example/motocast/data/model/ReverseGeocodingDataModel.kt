import com.google.gson.annotations.SerializedName

data class ReverseGeocodingDataModel(
    val type: String,
    val query: List<Double>,
    val features: List<ReverseGeocodingFeature>,
    val attribution: String
)

data class ReverseGeocodingFeature(
    val id: String,
    val type: String,
    @SerializedName("place_type")
    val placeType: List<String>,
    val relevance: Double,
    val properties: ReverseGeocodingProperties,
    val text: String,
    @SerializedName("place_name")
    val placeName: String,
    val center: List<Double>,
    val geometry: ReverseGeocodingGeometry,
    val address: String,
    val context: List<ReverseGeocodingContext>
)

data class ReverseGeocodingProperties(
    val accuracy: String,
    @SerializedName("mapbox_id")
    val mapboxId: String
)

data class ReverseGeocodingGeometry(
    val type: String,
    val coordinates: List<Double>
)

data class ReverseGeocodingContext(
    val id: String,
    val mapboxId: String? = null,
    val shortCode: String? = null,
    val wikidata: String? = null,
    val text: String
)
