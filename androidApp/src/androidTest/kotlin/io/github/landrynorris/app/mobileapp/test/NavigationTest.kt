package io.github.landrynorris.app.mobileapp.test

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import io.github.landrynorris.app.components.OtpLogic
import io.github.landrynorris.app.components.PasswordLogic
import io.github.landrynorris.app.components.SettingsLogic
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertIs

class NavigationTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun testNavigation() {
        withApplication(composeRule) {
            composeRule.mainClock.autoAdvance = false
            awaitIdle()
            otpButton.assertExists("No Otp button")
            passwordButton.assertExists("No password button")
            settingsButton.assertExists("No settings button")

            awaitIdle()
            passwordButton.performClick()
            it.withCurrentLogic { assertIs<PasswordLogic>(this) }

            awaitIdle()
            settingsButton.performClick()
            it.withCurrentLogic { assertIs<SettingsLogic>(this) }

            awaitIdle()
            otpButton.performClick()
            it.withCurrentLogic { assertIs<OtpLogic>(this) }
        }
    }

    private val ComposeTestRule.otpButton get() = onNodeWithTag("otp")
    private val ComposeTestRule.passwordButton get() = onNodeWithTag("password")
    private val ComposeTestRule.settingsButton get() = onNodeWithTag("settings")
}