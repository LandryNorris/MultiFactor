package io.github.landrynorris.multifactor.mobileapp.test

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import io.github.landrynorris.otp.Hotp
import io.github.landrynorris.otp.Totp
import kotlinx.coroutines.delay
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OtpEntryTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun testClickAddOtp() {
        composeRule.mainClock.autoAdvance = false
        withApplication(composeRule) {
            otpButton.assertExists("No otp navigation button")
            otpButton.performClick()

            addOtp.assertExists("No button to add otp")
            addOtp.performClick()

            it.withOtpLogic { assertTrue(state.value.isAdding) }

            addOtp.performClick()

            it.withOtpLogic { assertFalse(state.value.isAdding) }
        }
    }

    @Test
    fun testAddHotp() {
        composeRule.mainClock.autoAdvance = false
        withApplication(composeRule) {
            otpButton.performClick()
            addOtp.performClick()

            composeRule.mainClock.advanceTimeBy(1000)

            awaitIdle()
            nameField.assertExists("No name field")
            nameField.performTextInput("Entry from test")

            composeRule.mainClock.advanceTimeBy(500)

            secretField.assertExists("No secret field")
            secretField.performTextInput("abcdefg")
            hotp.performClick()

            composeRule.mainClock.advanceTimeBy(100)

            awaitIdle()
            confirm.assertExists("No Confirm button")
            confirm.performClick()
            awaitIdle()

            composeRule.mainClock.advanceTimeBy(100)

            it.withOtpLogic {
                val anyMatch = state.value.otpList.any { otp ->
                    otp.name == "Entry from test" && otp.model.otp is Hotp
                }

                assertTrue(anyMatch)
            }

            newEntry.assertExists("No new entry added")
            newEntry.performTouchInput { swipeLeft() }

            it.withOtpLogic {
                val anyMatch = state.value.otpList.any { otp ->
                    otp.name == "Entry from test" && otp.model.otp is Hotp
                }

                assertFalse(anyMatch)
            }
        }
    }

    @Test
    fun testAddTotp() {
        composeRule.mainClock.autoAdvance = false
        withApplication(composeRule) {
            otpButton.performClick()
            addOtp.performClick()

            composeRule.mainClock.advanceTimeBy(1000)

            awaitIdle()
            nameField.assertExists("No name field")
            nameField.performTextInput("Entry from test")

            composeRule.mainClock.advanceTimeBy(500)

            secretField.assertExists("No secret field")
            secretField.performTextInput("abcdefg")
            totp.performClick()

            composeRule.mainClock.advanceTimeBy(100)

            awaitIdle()
            confirm.assertExists("No Confirm button")
            confirm.performClick()
            awaitIdle()

            composeRule.mainClock.advanceTimeBy(100)

            it.withOtpLogic {
                val anyMatch = state.value.otpList.any { otp ->
                    otp.name == "Entry from test" && otp.model.otp is Totp
                }

                assertTrue(anyMatch)
            }

            newEntry.assertExists("No new entry added")
            newEntry.performTouchInput { swipeLeft() }

            it.withOtpLogic {
                val anyMatch = state.value.otpList.any { otp ->
                    otp.name == "Entry from test" && otp.model.otp is Totp
                }

                assertFalse(anyMatch)
            }
        }
    }

    private val ComposeTestRule.otpButton get() = onNodeWithContentDescription("otp")
    private val ComposeTestRule.addOtp get() = onNodeWithContentDescription("Add")
    private val ComposeTestRule.nameField get() = onNodeWithContentDescription("NameField")
    private val ComposeTestRule.secretField get() = onNodeWithContentDescription("SecretField")
    private val ComposeTestRule.confirm get() = onNodeWithContentDescription("Confirm")
    private val ComposeTestRule.newEntry get() = onNodeWithContentDescription("Entry from test")
    private val ComposeTestRule.hotp get() = onNodeWithContentDescription("Hotp")
    private val ComposeTestRule.totp get() = onNodeWithContentDescription("Totp")
}