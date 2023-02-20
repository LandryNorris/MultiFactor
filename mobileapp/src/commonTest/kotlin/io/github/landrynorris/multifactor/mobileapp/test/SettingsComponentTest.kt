package io.github.landrynorris.multifactor.mobileapp.test

import io.github.landrynorris.multifactor.components.SettingsComponent
import io.github.landrynorris.multifactor.components.SettingsLogic
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsComponentTest {

    @Test
    fun testSetExcludeSimilar() = runBlocking {
        val settingsRepository = createSettingsRepository()
        val component = createComponent(settingsRepository)

        component.setExcludeSimilar(false)
        delay(50)
        assertEquals(false, component.passwordSettings.first()
            .first { it.name == "Exclude Similar" }.value)

        component.setExcludeSimilar(true)
        delay(50)
        assertEquals(true, component.passwordSettings.first()
            .first { it.name == "Exclude Similar" }.value)
    }

    @Test
    fun testSetIncludeDigits() = runBlocking {
        val settingsRepository = createSettingsRepository()
        val component = createComponent(settingsRepository)

        component.setIncludeDigits(false)
        delay(50)
        assertEquals(false, component.passwordSettings.first()
            .first { it.name == "Include Digits" }.value)

        component.setIncludeDigits(true)
        delay(50)
        assertEquals(true, component.passwordSettings.first()
            .first { it.name == "Include Digits" }.value)
    }

    @Test
    fun testSetPasswordLength() = runBlocking {
        val settingsRepository = createSettingsRepository()
        val component = createComponent(settingsRepository)

        component.setPasswordLength(17)
        delay(50)
        assertEquals(17, component.passwordSettings.first()
            .first { it.name == "Length" }.value)

        component.setPasswordLength(50)
        delay(50)
        assertEquals(50, component.passwordSettings.first()
            .first { it.name == "Length" }.value)
    }

    @Test
    fun testSetIncludeSpecial() = runBlocking {
        val settingsRepository = createSettingsRepository()
        val component = createComponent(settingsRepository)

        component.setIncludeSpecialChars(false)
        delay(50)
        assertEquals(false, component.passwordSettings.first()
            .first { it.name == "Include Special" }.value)

        component.setIncludeSpecialChars(true)
        delay(50)
        assertEquals(true, component.passwordSettings.first()
            .first { it.name == "Include Special" }.value)
    }

    private fun createComponent(settingsRepository: SettingsRepository,
                                openAbout: () -> Unit = {}): SettingsLogic {
        return SettingsComponent(createContext(), settingsRepository, openAbout)
    }
}