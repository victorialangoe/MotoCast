package com.example.motocast.domain.utils

import com.example.motocast.ui.viewmodel.route_planner.Destination
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class UtilsTest {

    // Test for checkIfAllDestinationsHaveNames
    @Test
    fun checkIfAllDestinationsHaveNames_NoDestinations_ReturnsFalse() {
        val destinations = emptyList<Destination>()

        assertTrue(Utils.checkIfAllDestinationsHaveNames(destinations))
    }

    @Test
    fun checkIfAllDestinationsHaveNames_SingleDestinationHasName_ReturnsTrue() {
        val destinations = listOf(Destination("name", 0.0, 0.0, 0))

        assertTrue(Utils.checkIfAllDestinationsHaveNames(destinations))
    }

    @Test
    fun checkIfAllDestinationsHaveNames_MultipleDestinationsAllHaveNames_ReturnsTrue() {
        val destinations = listOf(
            Destination("name", 0.0, 0.0, 0),
            Destination("name1", 0.0, 0.0, 0),
            Destination("name3", 0.0, 0.0, 0)
        )

        assertTrue(Utils.checkIfAllDestinationsHaveNames(destinations))
    }

    @Test
    fun checkIfAllDestinationsHaveNames_MultipleDestinationsOneWithoutName_ReturnsFalse() {
        val destinations = listOf(
            Destination("name", 0.0, 0.0, 0),
            Destination("", 0.0, 0.0, 0),
            Destination("name2", 0.0, 0.0, 0)
        )

        assertFalse(Utils.checkIfAllDestinationsHaveNames(destinations))
    }

    @Test
    fun checkIfAllDestinationsHaveNames_SingleDestinationWithoutName_ReturnsFalse() {
        val destinations = listOf(Destination("", 0.0, 0.0, 0))

        assertFalse(Utils.checkIfAllDestinationsHaveNames(destinations))
    }

    // Test for checkIfSomeDestinationsHaveNames
    @Test
    fun checkIfSomeDestinationsHaveNames_NoDestinations_ReturnsFalse() {
        val destinations = emptyList<Destination>()

        assertFalse(Utils.checkIfSomeDestinationsHaveNames(destinations))
    }

    @Test
    fun checkIfSomeDestinationsHaveNames_SingleDestinationHasName_ReturnsTrue() {
        val destinations = listOf(Destination("name", 0.0, 0.0, 0))

        assertTrue(Utils.checkIfSomeDestinationsHaveNames(destinations))
    }

    @Test
    fun checkIfSomeDestinationsHaveNames_MultipleDestinationsAllHaveNames_ReturnsTrue() {
        val destinations = listOf(
            Destination("name", 0.0, 0.0, 0),
            Destination("name1", 0.0, 0.0, 0),
            Destination("name3", 0.0, 0.0, 0)
        )

        assertTrue(Utils.checkIfSomeDestinationsHaveNames(destinations))
    }

    @Test
    fun checkIfSomeDestinationsHaveNames_MultipleDestinationsOneHasName_ReturnsTrue() {
        val destinations = listOf(
            Destination("name", 0.0, 0.0, 0),
            Destination("", 0.0, 0.0, 0),
            Destination("", 0.0, 0.0, 0)
        )

        assertTrue(Utils.checkIfSomeDestinationsHaveNames(destinations))
    }

    @Test
    fun checkIfSomeDestinationsHaveNames_MultipleDestinationsNoneHaveName_ReturnsFalse() {
        val destinations = listOf(
            Destination("", 0.0, 0.0, 0),
            Destination("", 0.0, 0.0, 0),
            Destination("", 0.0, 0.0, 0)
        )

        assertFalse(Utils.checkIfSomeDestinationsHaveNames(destinations))
    }

    // Tests for checkIfTimeIsMoreThan8DaysInFuture
    @Test
    fun checkIfTimeIsMoreThan8DaysInFuture_8DaysFuture_ReturnsFalse() {
        val time = Calendar.getInstance()
        time.add(Calendar.DAY_OF_YEAR, 8)

        assertFalse(Utils.checkIfTimeIsMoreThan8DaysInFuture(time))
    }

    @Test
    fun checkIfTimeIsMoreThan8DaysInFuture_9DaysFuture_ReturnsTrue() {
        val time = Calendar.getInstance()
        time.add(Calendar.DAY_OF_YEAR, 9)

        assertTrue(Utils.checkIfTimeIsMoreThan8DaysInFuture(time))
    }

    @Test
    fun checkIfTimeIsMoreThan8DaysInFuture_7DaysFuture_ReturnsFalse() {
        val time = Calendar.getInstance()
        time.add(Calendar.DAY_OF_YEAR, 7)

        assertFalse(Utils.checkIfTimeIsMoreThan8DaysInFuture(time))
    }

    @Test
    fun checkIfTimeIsMoreThan8DaysInFuture_Today_ReturnsFalse() {
        val time = Calendar.getInstance()

        assertFalse(Utils.checkIfTimeIsMoreThan8DaysInFuture(time))
    }

    @Test
    fun checkIfTimeIsMoreThan8DaysInFuture_Past_ReturnsFalse() {
        val time = Calendar.getInstance()
        time.add(Calendar.DAY_OF_YEAR, -1)

        assertFalse(Utils.checkIfTimeIsMoreThan8DaysInFuture(time))
    }

    @Test
    fun formatToISO8601_KnownDateTime_ReturnsExpectedFormat() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(2023, Calendar.MAY, 10, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val expected = "2023-05-10T12:00:00Z"
        assertEquals(expected, Utils.formatToISO8601(calendar))
    }

    // Test for formatToISO8601
    @Test
    fun formatToISO8601_FarFuture_ReturnsExpectedFormat() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(3000, Calendar.JANUARY, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val expected = "3000-01-01T00:00:00Z"
        assertEquals(expected, Utils.formatToISO8601(calendar))
    }

    @Test
    fun getZeroedTimestamp_ExactHour_ReturnsSameTimestamp() {
        val timestamp = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val zeroedTimestamp = Utils.getZeroedTimestamp(timestamp)

        assertEquals(timestamp.timeInMillis, zeroedTimestamp.timeInMillis)
    }

    // Test for getZeroedTimestamp
    @Test
    fun getZeroedTimestamp_MidHour_ReturnsZeroedTimestamp() {
        val timestamp = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val expectedTimestamp = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val zeroedTimestamp = Utils.getZeroedTimestamp(timestamp)

        assertEquals(expectedTimestamp.timeInMillis, zeroedTimestamp.timeInMillis)
    }


}
