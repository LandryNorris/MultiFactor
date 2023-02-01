package io.github.landrynorris.multifactor.mobileapp.test

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PasswordEntryTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun testClickAddPassword() {
        composeRule.mainClock.autoAdvance = false
        withApplication(composeRule) {
            passwordButton.assertExists("No password button")
            passwordButton.performClick()
            awaitIdle()

            composeRule.mainClock.autoAdvance = true //we're not on the otp screen anymore

            addPassword.assertExists("No Add password button")
            addPassword.performClick()
            awaitIdle()

            it.withPasswordLogic { assertTrue(state.value.showAddPassword) }

            closePassword.performClick()
            awaitIdle()
            it.withPasswordLogic { assertFalse(state.value.showAddPassword) }
        }
    }

    @Test
    fun testAddPasswordEntry() {
        withApplication(composeRule) {
            mainClock.autoAdvance = false

            passwordButton.assertExists("No password button")
            passwordButton.performClick()
            awaitIdle()

            mainClock.autoAdvance = true //we're not on the otp screen anymore

            addPassword.performClick()
            awaitIdle()

            nameField.assertExists("No Name Field")
            passwordField.assertExists("No Password Field")
            nameField.performTextInput("Entry from Test")
            passwordField.performTextInput("New Password Value")

            confirm.assertExists("No Confirm button")
            confirm.performClick()
            awaitIdle()

            newPasswordTitle.assertExists("Can't find new password entry")
            newPasswordContent.assertDoesNotExist()

            newPasswordTitle.performTouchInput { swipeLeft() }
        }
    }

    private val ComposeTestRule.passwordButton get() = onNodeWithContentDescription("password")
    private val ComposeTestRule.addPassword get() = onNodeWithContentDescription("Add")
    private val ComposeTestRule.closePassword get() = onNodeWithContentDescription("Close")
    private val ComposeTestRule.nameField get() = onNodeWithContentDescription("NameField")
    private val ComposeTestRule.passwordField get() = onNodeWithContentDescription("PasswordField")
    private val ComposeTestRule.confirm get() = onNodeWithContentDescription("Confirm")
    private val ComposeTestRule.newPasswordTitle get() = onNodeWithContentDescription("Entry from Test")
    private val ComposeTestRule.newPasswordContent get() = onNodeWithText("New Password Value")
}