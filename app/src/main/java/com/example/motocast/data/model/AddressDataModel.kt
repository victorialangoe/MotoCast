package com.example.motocast.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data model for the MetAlerts API.
 * Link to the API documentation: [Metalerts API](https://api.met.no/weatherapi/metalerts/1.1/documentation)
 */
data class AddressDataModel(
    @SerializedName("metadata")
    val metadata: AddressMetadata,

    @SerializedName("adresser")
    val addresses: List<Address>
)
data class AddressMetadata(
    @SerializedName("side")
    val page: Int,

    @SerializedName("treffPerSide")
    val hitsPerPage: Int,

    @SerializedName("totaltAntallTreff")
    val totalHits: Int,

    @SerializedName("viserTil")
    val showingTo: Int,

    @SerializedName("sokeStreng")
    val searchString: String,

    @SerializedName("asciiKompatibel")
    val asciiCompatible: Boolean,

    @SerializedName("viserFra")
    val showingFrom: Int
)

data class Address(
    @SerializedName("adressenavn")
    val addressName: String,

    @SerializedName("adressetekst")
    val addressText: String,

    @SerializedName("adressetilleggsnavn")
    val addressAdditionalName: String,

    @SerializedName("adressekode")
    val addressCode: Int,

    @SerializedName("nummer")
    val number: Int,

    @SerializedName("bokstav")
    val letter: String,

    @SerializedName("kommunenummer")
    val municipalityNumber: String,

    @SerializedName("kommunenavn")
    val municipalityName: String,

    @SerializedName("gardsnummer")
    val farmNumber: Int,

    @SerializedName("bruksnummer")
    val usageNumber: Int,

    @SerializedName("festenummer")
    val partyNumber: Int,

    @SerializedName("undernummer")
    val subNumber: Int,

    @SerializedName("bruksenhetsnummer")
    val unitNumber: List<String>,

    @SerializedName("objtype")
    val objectType: String,

    @SerializedName("poststed")
    val postPlace: String,

    @SerializedName("postnummer")
    val postalCode: String,

    @SerializedName("adressetekstutenadressetilleggsnavn")
    val addressTextWithoutAdditionalName: String,

    @SerializedName("stedfestingverifisert")
    val locationVerified: Boolean,

    @SerializedName("representasjonspunkt")
    val representationPoint: RepresentationPoint,

    @SerializedName("oppdateringsdato")
    val updateDate: String
)

data class RepresentationPoint(
    @SerializedName("epsg")
    val epsg: String,

    @SerializedName("lat")
    val latitude: Double,

    @SerializedName("lon")
    val longitude: Double
)
