package com.example.motocast.util

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.example.motocast.ui.view.utils.buttons.BackButton
import org.junit.Rule
import org.junit.Test


class TestBackButton {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun backButton_isDisplayed() {
        composeTestRule.setContent {
            BackButton(onClick = {})
        }

        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    @Test
    fun backButton_onClick_isInvoked() {
        var clicked = false

        composeTestRule.setContent {
            BackButton(onClick = { clicked = true })
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        assert(clicked)
    }
}

