package com.example.motocast.ui.view

import junit.framework.TestCase.assertTrue
import org.junit.Test

class TestGetWeatherIcon {

    @Test
    fun testGetWeatherIcon() {
        val result = getWeatherIcon("forestFire", "2; yellow; Moderate")
        assertTrue(result == "R.drawable.icon-warning-forestfire_yellow")
    }
}