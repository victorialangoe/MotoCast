package com.example.motocast.util

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.motocast.ui.view.utils.badges.WelcomeBadge
import org.junit.Rule
import org.junit.Test
import java.util.*

class TestWelcomeBadge {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun welcomeBadge_displaysCorrectGreeting() {
        val userName = "TestUser"

        val expectedGreeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "God morgen, $userName"
            in 12..17 -> "God ettermiddag, $userName"
            else -> "God kveld, $userName"
        }

        composeTestRule.setContent {
            WelcomeBadge(userName = userName)
        }

        composeTestRule.onNodeWithText(expectedGreeting).assertIsDisplayed()
    }

}