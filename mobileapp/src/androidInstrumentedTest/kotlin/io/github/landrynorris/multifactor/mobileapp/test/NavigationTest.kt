package io.github.landrynorris.multifactor.mobileapp.test

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Rule
import kotlin.test.Test

class NavigationTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun testNavigation() {
        withApplication(composeRule) {
            awaitIdle()
            otpButton.assertExists("No Otp button")
            passwordButton.assertExists("No password button")
            settingsButton.assertExists("No settings button")

            awaitIdle()
            passwordButton.performClick()
            awaitIdle()
            settingsButton.performClick()
            awaitIdle()
            otpButton.performClick()
        }
    }

    private val ComposeTestRule.otpButton get() = onNodeWithContentDescription("otp")
    private val ComposeTestRule.passwordButton get() = onNodeWithContentDescription("password")
    private val ComposeTestRule.settingsButton get() = onNodeWithContentDescription("settings")
}