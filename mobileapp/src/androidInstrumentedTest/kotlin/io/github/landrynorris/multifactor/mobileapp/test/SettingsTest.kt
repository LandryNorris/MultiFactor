package io.github.landrynorris.multifactor.mobileapp.test

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import io.github.landrynorris.multifactor.components.SettingsLogic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

//All tests should undo their changes at the end
class SettingsTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun testSetIncludeDigits() {
        withApplication(composeRule) {
            mainClock.autoAdvance = false
            settingsButton.assertExists("No settings option")
            settingsButton.performClick()
            awaitIdle()

            mainClock.autoAdvance = true

            var currentState = false
            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Include Digits" }.value as Boolean
            }

            val originalState = currentState
            onAllNodesWithContentDescription("switch")[0].performClick()
            awaitIdle()

            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Include Digits" }.value as Boolean
            }

            assertNotEquals(originalState, currentState)

            onAllNodesWithContentDescription("switch")[0].performClick()
            awaitIdle()

            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Include Digits" }.value as Boolean
            }
            assertEquals(originalState, currentState)
        }
    }

    @Test
    fun testSetIncludeSpecial() {
        withApplication(composeRule) {
            mainClock.autoAdvance = false
            settingsButton.assertExists("No settings option")
            settingsButton.performClick()
            awaitIdle()

            mainClock.autoAdvance = true

            var currentState = false
            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Include Special" }.value as Boolean
            }

            val originalState = currentState
            onAllNodesWithContentDescription("switch")[1].performClick()
            awaitIdle()

            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Include Special" }.value as Boolean
            }

            assertNotEquals(originalState, currentState)

            onAllNodesWithContentDescription("switch")[1].performClick()
            awaitIdle()

            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Include Special" }.value as Boolean
            }
            assertEquals(originalState, currentState)
        }
    }

    @Test
    fun testSetExcludeSimilar() {
        withApplication(composeRule) {
            mainClock.autoAdvance = false
            settingsButton.assertExists("No settings option")
            settingsButton.performClick()
            awaitIdle()

            mainClock.autoAdvance = true

            var currentState = false
            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Exclude Similar" }.value as Boolean
            }

            val originalState = currentState
            onAllNodesWithContentDescription("switch")[2].performClick()
            awaitIdle()

            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Exclude Similar" }.value as Boolean
            }

            assertNotEquals(originalState, currentState)

            onAllNodesWithContentDescription("switch")[2].performClick()
            awaitIdle()

            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Exclude Similar" }.value as Boolean
            }
            assertEquals(originalState, currentState)
        }
    }

    @Test
    fun testEnterLength() {
        withApplication(composeRule) {
            mainClock.autoAdvance = false
            settingsButton.assertExists("No settings option")
            settingsButton.performClick()
            awaitIdle()

            mainClock.autoAdvance = true

            var currentState = 0
            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Length" }.value as Int
            }

            val originalState = currentState
            onAllNodesWithContentDescription("IntSetting").onFirst().performTextReplacement("20")
            awaitIdle()

            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Length" }.value as Int
            }
            assertEquals(20, currentState)

            onAllNodesWithContentDescription("IntSetting").onFirst().performTextReplacement(originalState.toString())
            awaitIdle()

            it.withSettingsLogic {
                currentState = currentSettings().first { it.name == "Length" }.value as Int
            }
            assertEquals(originalState, currentState)
        }
    }

    private val ComposeTestRule.settingsButton get() = onNodeWithContentDescription("settings")
    private val ComposeTestRule.includeDigits get() = onNodeWithContentDescription("includeDigits")

    private fun SettingsLogic.currentSettings() = runBlocking { passwordSettings.first() }
}