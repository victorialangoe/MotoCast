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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object Utils {

    /**
     * Checks if all of the destinations have a name
     * @param destinations The destinations to check
     * @return True if all of the destinations have a name, false otherwise
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
     * Checks if any of the destinations have a name
     * @param destinations The destinations to check
     * @return True if any of the destinations have a name, false otherwise
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
     * @return The distance between the two locations in meters or null if any of the parameters are null
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
     * @return The calendar formatted to ISO8601 format
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
     * @return The filtered and sorted addresses
     */
    fun filterSearchResults(query: String, addresses: List<Address>): List<Address> {
        if (query.isEmpty()) return emptyList()

        return addresses.sortedWith(searchResultsCompareBy(query))
    }

    /**
     * Compares two addresses by how well they match the query.
     * First by how well the address matches the query, then by how well the municipality matches the query,
     * then by the distance from the user
     * @param query The query to compare by
     */
    private fun searchResultsCompareBy(query: String): Comparator<Address> {
        return compareBy<Address> {
            if (it.addressText == query) 0 else 1
        }.thenBy {
            if (it.municipality == query) 0 else 1
        }.thenBy {
            it.distanceFromUser
        }
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
