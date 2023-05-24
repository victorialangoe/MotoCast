package com.example.motocast.view.settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.motocast.ui.view.settings.TextAndSwitch
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class TestTextAndSwitch {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun textAndSwitch_isDisplayed() {
        val text = "Test Text"
        var checked = false

        composeTestRule.setContent {
            TextAndSwitch(text, checked) { checked = it }
        }

        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    @Test
    fun textAndSwitch_onClick_changesValue() {
        val text = "Test Text"
        var checked = false

        composeTestRule.setContent {
            TextAndSwitch(text, checked) { checked = it }
        }

        composeTestRule.onNodeWithTag("Switch").performClick()

        Assert.assertTrue(checked)
    }
}
