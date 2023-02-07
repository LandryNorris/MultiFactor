package io.github.landrynorris.multifactor.mobileapp.test

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import io.github.landrynorris.multifactor.components.OtpLogic
import io.github.landrynorris.multifactor.components.PasswordLogic
import io.github.landrynorris.multifactor.components.SettingsLogic
import org.junit.Rule
import kotlin.test.Test
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

    private val ComposeTestRule.otpButton get() = onNodeWithContentDescription("otp")
    private val ComposeTestRule.passwordButton get() = onNodeWithContentDescription("password")
    private val ComposeTestRule.settingsButton get() = onNodeWithContentDescription("settings")
}