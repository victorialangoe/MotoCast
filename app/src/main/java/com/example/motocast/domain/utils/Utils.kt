package com.example.motocast.domain.utils

import android.content.res.Resources
import android.location.Location
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Shape
import com.example.motocast.R
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.Properties
import com.example.motocast.ui.viewmodel.address.Address
import com.example.motocast.ui.viewmodel.route_planner.Destination
import me.xdrop.fuzzywuzzy.FuzzySearch
import okhttp3.internal.format
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object Utils {

    /**
     * Checks if all of the destinations have a name
     * @param destinations The destinations to check
     * @return [Boolean] True if all of the destinations have a name, false otherwise
     */
    fun checkIfAllDestinationsHaveNames(destinations: List<Destination>): Boolean {
        return destinations.all { it.name != null && it.name.isNotEmpty() }
    }

    /**
     * Checks if the time is more than 8 days in the future (the LocationForecast API only supports
     * 8/9 days in the future)
     * @param time The time to check
     * @return [Boolean] True if the time is more than 8 days in the future, false otherwise
     */
    fun checkIfTimeIsMoreThan8DaysInFuture(time: Calendar): Boolean {
        val now = Calendar.getInstance()
        val timeInFuture = now.clone() as Calendar
        timeInFuture.add(Calendar.DAY_OF_YEAR, 8)
        return time.after(timeInFuture)
    }

    /**
     * Checks if any of the destinations have a name
     * @param destinations The destinations to check
     * @return [Boolean] True if any of the destinations have a name, false otherwise
     */
    fun checkIfSomeDestinationsHaveNames(destinations: List<Destination>): Boolean {
        return destinations.any { it.name != null && it.name != "" }
    }

    /**
     * Calculates the distance between two locations in meters
     * @param latitude Latitude of the first location
     * @param longitude Longitude of the first location
     * @param location Location of the second location
     * @return [Int] The distance between the two locations in meters
     */
    fun getAirDistanceFromLocation(
        latitude: Double?,
        longitude: Double?,
        location: Location?
    ): Int? {

        if (latitude == null || longitude == null || location == null) return null

        val lat1 = location.latitude
        val lon1 = location.longitude

        val earthRadius = 6371 // kilometers
        val dLat = Math.toRadians(latitude - lat1)
        val dLon = Math.toRadians(longitude - lon1)
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(latitude)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                sin(dLon / 2) * sin(dLon / 2) * cos(lat1Rad) * cos(lat2Rad)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = earthRadius * c * 1000 // meters
        return distance.toInt()
    }

    /**
     * Formats a calendar to ISO8601 format
     * @param calendar The calendar to format
     * @return [String] The calendar formatted to ISO8601 format
     */
    fun formatToISO8601(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(calendar.time)
    }

    /**
     * Zeroes the timestamp to the nearest hour
     * @param timestamp The timestamp to zero
     * @return The timestamp zeroed to the nearest hour
     */
    fun getZeroedTimestamp(timestamp: Calendar): Calendar {
        val zeroedTimestamp = timestamp.clone() as Calendar
        zeroedTimestamp.set(Calendar.DAY_OF_MONTH, timestamp.get(Calendar.DAY_OF_MONTH))
        zeroedTimestamp.set(Calendar.YEAR, timestamp.get(Calendar.YEAR))
        zeroedTimestamp.set(Calendar.HOUR_OF_DAY, timestamp.get(Calendar.HOUR_OF_DAY))
        zeroedTimestamp.set(Calendar.MINUTE, 0)
        zeroedTimestamp.set(Calendar.SECOND, 0)
        zeroedTimestamp.set(Calendar.MILLISECOND, 0)
        return zeroedTimestamp
    }

    /**
     * Filters the search results and sorts them
     * @param userQuery The query to filter by
     * @param allAddresses The addresses to filter
     * @return The filtered and sorted addresses as [List]
     */
    fun filterSearchResults(
        userQuery: String,
        allAddresses: List<Address>,
        minimumMatchScore: Int = 90,
    ): List<Address> {
        if (userQuery.isEmpty()) return emptyList()

        val (municipalQuery, addressQuery) = splitUserQuery(userQuery, allAddresses)

        val updatedMunicipalities = mutableListOf<String>()

        val updatedAddresses = mutableListOf<Address>()

        allAddresses.forEach { it ->
            if (it.municipality != null) {
                if (!updatedMunicipalities.contains(it.municipality.lowercase())) {
                    updatedAddresses.add(
                        Address(
                            it.municipality.lowercase().replaceFirstChar { it.uppercase() },
                            null,
                            it.latitude,
                            it.longitude,
                            it.distanceFromUser
                        )
                    )
                    updatedMunicipalities.add(it.municipality.lowercase())
                }
            }
            updatedAddresses.add(it)

        }

        return updatedAddresses
            .map { address ->
                Pair(
                    address,
                    calculateMatchScore(municipalQuery, addressQuery, address, minimumMatchScore)
                )
            }
            .filter { (_, matchScore) -> matchScore > 0 }
            .sortedByDescending { (_, matchScore) -> matchScore }
            .map { (address, _) -> address }

    }

    /**
     * Splits the user query into a municipal query and an address query
     *
     * @param userQuery The query to split
     * @param allAddresses The addresses to compare the query to
     *
     * @return A [Pair] containing the municipal query and the address query
     */
    private fun splitUserQuery(
        userQuery: String,
        allAddresses: List<Address>
    ): Pair<String, String> {
        val queryParts = userQuery.split(" ", ",", ignoreCase = true)
            .filter { it.isNotEmpty() }
            .map { it.trim() }
        var municipalQuery = ""
        var addressQuery = ""

        allAddresses.forEach { address ->
            queryParts.forEach { part ->
                if (part.matches(Regex("\\d+[A-Z]*"))) {
                    addressQuery += " $part"
                } else if (FuzzySearch.partialRatio(
                        address.municipality,
                        part
                    ) > FuzzySearch.partialRatio(address.addressText, part)
                ) {
                    municipalQuery += " $part"
                } else {
                    addressQuery += " $part"
                }
            }
        }

        return Pair(municipalQuery.trim(), addressQuery.trim())
    }

    /**
     * Calculates the match score for an address, which is the sum of the match scores for the
     * municipal query and the address query. A match score is described as the ratio of the
     * similarity between the query and the address, using the fuzzy search algorithm (Based on Levenshtein distance)
     *
     * @param municipalQuery The municipal query
     * @param addressQuery The address query
     * @param address The address to calculate the match score for
     * @param minimumMatchScore This is the minimum match score for the municipal query and the address query
     */
    private fun calculateMatchScore(
        municipalQuery: String,
        addressQuery: String,
        address: Address,
        minimumMatchScore: Int
    ): Int {
        val municipalMatchScore = fuzzyMatchScore(municipalQuery, address.municipality ?: "")
        val addressMatchScore = fuzzyMatchScore(addressQuery, address.addressText)

        return when {
            bothPartsMatch(minimumMatchScore, municipalMatchScore, addressMatchScore) -> {
                // High bonus for addresses where both parts match
                municipalMatchScore + addressMatchScore + 100
            }
            else -> {
                municipalMatchScore + addressMatchScore
            }
        }
    }

    /**
     * Checks if both the municipal query and the address query match (above the minimum match score)
     *
     * @param minimumMatchScore This is the minimum match score for the municipal query and the address query
     * @param municipalMatchScore The match score for the municipal query
     * @param addressMatchScore The match score for the address query
     */
    private fun bothPartsMatch(
        minimumMatchScore: Int,
        municipalMatchScore: Int,
        addressMatchScore: Int
    ) =
        municipalMatchScore >= minimumMatchScore && addressMatchScore >= minimumMatchScore

    private fun fuzzyMatchScore(queryPart: String, addressPart: String) =
        if (queryPart.isNotEmpty()) FuzzySearch.partialRatio(addressPart, queryPart) else 0


    /**
     * Formats a calendar to a date string
     * @param startTime The calendar to format
     * @return The calendar formatted to a date string
     */
    fun formatDate(startTime: Calendar): String {
        return (
                startTime.get(Calendar.DAY_OF_MONTH).toString() + " " +
                        startTime.getDisplayName(
                            Calendar.MONTH,
                            Calendar.LONG,
                            Locale.getDefault()
                        )
                )
    }

    /**
     * Formats a calendar to a time string
     * @param startTime The calendar to format
     * @return The calendar formatted to a time string
     */
    fun formatTime(startTime: Calendar): String {
        val hour = startTime.get(Calendar.HOUR_OF_DAY)
        val minute = startTime.get(Calendar.MINUTE)

        return format("%02d:%02d", hour, minute)
    }


    /**
     * Formats a duration in seconds to a string
     * @param duration The duration to format
     * @return The duration formatted to a string in the format "1 dag og 2 timer, 3 minutter og 4 sekunder"
     */
    fun formatDurationAsTimeString(duration: Long, resources: Resources): String {
        val durationAsDuration = Duration.ofSeconds(duration)

        val days = durationAsDuration.toDays()
        val hours = durationAsDuration.toHours() % 24
        val minutes = durationAsDuration.toMinutes() % 60

        return when {
            days > 0 -> {
                val daysString = resources.getQuantityString(R.plurals.days, days.toInt(), days)
                val hoursString = resources.getQuantityString(R.plurals.hours, hours.toInt(), hours)
                val minutesString =
                    resources.getQuantityString(R.plurals.minutes, minutes.toInt(), minutes)

                "$daysString og $hoursString og $minutesString"
            }
            hours > 0 -> {
                val hoursString = resources.getQuantityString(R.plurals.hours, hours.toInt(), hours)
                val minutesString =
                    resources.getQuantityString(R.plurals.minutes, minutes.toInt(), minutes)

                "$hoursString og $minutesString"
            }
            else -> {
                resources.getQuantityString(R.plurals.minutes, minutes.toInt(), minutes)
            }
        }
    }


    /**
     * This gets the correct alerts from the alerts
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     * @param timestamp The timestamp to check
     * @param alerts The alerts to check
     */
    fun getCorrectAlertsFromAlerts(
        latitude: Double,
        longitude: Double,
        timestamp: Calendar,
        alerts: MetAlertsDataModel?
    ): List<Properties> {
        val alertsFound = mutableListOf<Properties>()
        if (alerts != null) {
            for (alert in alerts.features) {

                val interval = alert.`when`.interval
                val startTime = stringToCalendar(interval.first())
                val endTime = stringToCalendar(interval.last())

                if (timestamp.before(endTime) && timestamp.after(startTime)) {

                    alert.geometry.coordinates.map { coordinates ->
                        val jtsCoordinates = coordinates.map { coordinate ->
                            Coordinate(coordinate[0], coordinate[1])
                        }.toTypedArray()

                        val geometryFactory = GeometryFactory()
                        val polygon = geometryFactory.createPolygon(jtsCoordinates)
                        val point = geometryFactory.createPoint(Coordinate(longitude, latitude))

                        if (point.within(polygon)) {
                            alertsFound.add(alert.properties)
                        }
                    }

                }
            }
        }
        return alertsFound
    }


    /**
     * Converts a string on format "yyyy-MM-dd'T'HH:mm:ssXXX" to a calendar
     * @param inputString The string to convert
     * @return The calendar
     */
    private fun stringToCalendar(inputString: String): Calendar {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val date = format.parse(inputString) ?: return Calendar.getInstance()
        return Calendar.getInstance().apply {
            time = date
        }
    }

    /**
     * Creates a triangle shape, which is used in the weather cards on the route screen.
     *
     * @return The triangle shape as a [Shape]
     */
    fun createReverseTriangleShape(): Shape {
        return GenericShape { size, _ ->
            moveTo(0f, 0f)
            lineTo(size.width / 2f, size.height)
            lineTo(size.width, 0f)
        }
    }

    fun getWeatherIcon(event: String, awarenessLevel: String): Int {
        val eventIconMap = mapOf(
            "blowingSnow" to "icon_warning_snow_",
            "avalances" to "icon_warning_avalanches_",
            "drivingConditions" to "icon_warning_drivingconditions_",
            "flood" to "icon_warning_flood_",
            "forestFire" to "icon_warning_forestfire_",
            "gale" to "icon_warning_wind_",
            "ice" to "icon_warning_ice_",
            "icing" to "icon_warning_generic_",
            "landslide" to "icon_warning_landslide_",
            "polarLow" to "icon_warning_polarlow_",
            "rain" to "icon_warning_rain_",
            "rainFlood" to "icon_warning_rainflood_",
            "snow" to "icon_warning_snow_",
            "stormSurge" to "icon_warning_stormsurge_",
            "lightning" to "icon_warning_lightning_",
            "wind" to "icon_warning_wind_",
            "unknown" to "icon_warning_generic_"
        )

        val awarenessLevelIconMap = mapOf(
            "2; yellow; Moderate" to "yellow",
            "3; orange; Severe" to "orange"
        )

        val eventIcon = eventIconMap[event] ?: "icon_warning_generic_"
        val awarenessLevelIcon = awarenessLevelIconMap[awarenessLevel] ?: ""

        val resourceName = "$eventIcon$awarenessLevelIcon"

        return try {
            val resourceId = R.drawable::class.java.getField(resourceName).getInt(null)
            resourceId
        } catch (e: Exception) {
            throw Exception("Resource not found: $resourceName")
        }
    }


}
