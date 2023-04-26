package com.example.motocast.domain.utils

import android.util.Log
import com.example.motocast.data.model.MetAlertsDataModel
import com.example.motocast.data.model.Properties
import com.example.motocast.ui.viewmodel.address.Address
import com.example.motocast.ui.viewmodel.route_planner.Destination
import okhttp3.internal.format
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.math.*

object Utils {

    /**
     * Checks if all of the destinations have a name
     * @param destinations The destinations to check
     * @return [Boolean] True if all of the destinations have a name, false otherwise
     */
    fun checkIfAllDestinationsHaveNames(destinations: List<Destination>): Boolean {
        destinations.forEach {
            if (it.name == null || it.name == "") {
                return false
            }
        }
        return true
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
        destinations.forEach {
            if (it.name != null && it.name != "") {
                return true
            }
        }
        return false
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
        location: android.location.Location?
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
     * @param query The query to filter by
     * @param addresses The addresses to filter
     * @return The filtered and sorted addresses as [List]
     */
    fun filterSearchResults(query: String, addresses: List<Address>): List<Address> {
        if (query.isEmpty()) return emptyList()

        return filterAndSortAddresses(query, addresses)
    }

    /**
     * Filters the search results and sorts them, removes all numbers from the query.
     * It also creates a new address with the municipality as the addressText if the municipality (Makes it possible to search for municipality)
     * @param query The query to filter by
     * @param addresses The addresses to filter
     * @param maxDistance The maximum distance between the query and the address (in Levenshtein distance)
     */
    private fun filterAndSortAddresses(
        userInput: String,
        addresses: List<Address>,
        maxDistance: Int = when (userInput.length) {
            in 0..5 -> 1
            else -> 2
        }
    ): List<Address> {
        val inputLowercase = userInput.lowercase()

        val updatedMunicipalities = mutableListOf<String>()

        var updatedAddresses = mutableListOf<Address>()

        addresses.forEach { it ->
            if(it.municipality != null){
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

        updatedAddresses = updatedAddresses
            .filter {
                levenshteinDistance(
                    it.addressText.lowercase(), inputLowercase
                ) <= maxDistance
            }
            .sortedBy {

                    // if under 1000 m from user, sort by distance
                    if (it.distanceFromUser != null && it.distanceFromUser < 1000) {
                        it.distanceFromUser
                    } else {
                        // if not, sort by levenshtein distance
                        levenshteinDistance(
                            it.addressText.lowercase(), inputLowercase
                        )
                    }

            }
            .toMutableList()

        Log.d("filterAndSortAddresses", "updatedAddresses: $updatedAddresses")
        return updatedAddresses
    }


    /**
     * This algorithm calculates the Levenshtein distance between two strings
     * Basicly it calculates the number of changes needed to change one string into another.
     * Perfect for checking how similar two strings are
     * @param a The first string
     * @param b The second string
     */
    private fun levenshteinDistance(a: String, b: String): Int {
        val aLen = a.length
        val bLen = b.length

        val dp = Array(aLen + 1) { IntArray(bLen + 1) }

        for (i in 0..aLen) dp[i][0] = i
        for (j in 0..bLen) dp[0][j] = j

        for (i in 1..aLen) {
            for (j in 1..bLen) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                dp[i][j] = min(min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost)
            }
        }

        return dp[aLen][bLen]
    }

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
    fun formatDurationAsTimeString(duration: Long): String {
        val durationAsDuration = Duration.ofSeconds(duration)

        val days = durationAsDuration.toDays()
        val hours = durationAsDuration.toHours() % 24
        val minutes = durationAsDuration.toMinutes() % 60

        return if (days > 0) {
            days.toString() + " dag" + (if (days > 1) "er" else "") + " og " +
                    hours.toString() + " time" + (if (hours > 1) "r" else "") + " og " +
                    minutes.toString() + " minutt" + (if (minutes > 1) "er" else "")
        } else {
            if (hours > 0) {
                hours.toString() + " time" + (if (hours > 1) "r" else "") + " og " +
                        minutes.toString() + " minutt" + (if (minutes > 1) "er" else "")
            } else {
                minutes.toString() + " minutt" + (if (minutes > 1) "er" else "")
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
                // Check if the timestamp is between the start and end time interval
                val interval = alert.`when`.interval
                val startTime = stringToCalendar(interval.first())
                val endTime = stringToCalendar(interval.last())
                if (timestamp.before(endTime) && timestamp.after(startTime)) {
                    // The timestamp is not between the start and end time interval

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

                } else {
                    Log.d("WeatherViewModel - Check", "Alert: ${alert.properties.title}")
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

}
