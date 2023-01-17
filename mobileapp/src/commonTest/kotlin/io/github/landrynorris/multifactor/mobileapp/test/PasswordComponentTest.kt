package io.github.landrynorris.multifactor.mobileapp.test

import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.multifactor.components.PasswordComponent
import io.github.landrynorris.multifactor.components.PasswordLogic
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PasswordComponentTest {

    @Test
    fun testShowAndHideCreatePassword() {
        val component = createComponent()

        assertFalse(component.state.value.showAddPassword)
        component.toggleAddPassword()
        assertTrue(component.state.value.showAddPassword)
        component.toggleAddPassword()
        assertFalse(component.state.value.showAddPassword)
        component.toggleAddPassword()
        assertTrue(component.state.value.showAddPassword)
        component.hideAddPassword()
        assertFalse(component.state.value.showAddPassword)
        component.hideAddPassword()
        assertFalse(component.state.value.showAddPassword)
    }

    private fun createComponent(
        settingsRepository: SettingsRepository = createSettingsRepository(),
        crypto: Crypto = MockCrypto()): PasswordLogic {
        return PasswordComponent(createContext(), crypto,
            createPasswordRepository(), settingsRepository)
    }
}