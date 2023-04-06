package com.example.motocast.data.model

data class Metadata(
    val viserFra: Int,
    val totaltAntallTreff: Int,
    val treffPerSide: Int,
    val viserTil: Int,
    val sokeStreng: String,
    val asciiKompatibel: Boolean,
    val side: Int
)

data class Representasjonspunkt(
    val epsg: String,
    val lat: Double,
    val lon: Double
)

data class Adresse(
    val adressenavn: String,
    val adressetekst: String,
    val adressetilleggsnavn: String?,
    val adressekode: Int,
    val nummer: Int,
    val bokstav: String?,
    val kommunenummer: String,
    val kommunenavn: String,
    val gardsnummer: Int,
    val bruksnummer: Int,
    val festenummer: Int,
    val undernummer: String?,
    val bruksenhetsnummer: List<String>,
    val objtype: String,
    val poststed: String,
    val postnummer: String,
    val adressetekstutenadressetilleggsnavn: String,
    val stedfestingverifisert: Boolean,
    val representasjonspunkt: Representasjonspunkt,
    val oppdateringsdato: String
)

data class AddressSearchResult(
    val metadata: Metadata,
    val adresser: List<Adresse>
)
