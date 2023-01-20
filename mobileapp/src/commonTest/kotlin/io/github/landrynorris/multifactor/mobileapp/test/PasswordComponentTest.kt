package io.github.landrynorris.multifactor.mobileapp.test

import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.multifactor.components.PasswordComponent
import io.github.landrynorris.multifactor.components.PasswordLogic
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
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

    @Test
    fun testConnectedChildren() = runBlocking {
        val component = createComponent()

        assertEquals(0, component.passwordListLogic.state.value.passwords.size)

        component.createPasswordLogic.passwordChanged("A password")
        component.createPasswordLogic.nameChanged("A name")
        component.createPasswordLogic.confirm()

        assertOccursWithin(1000) {
            component.passwordListLogic.state.value.passwords.size == 1
        }
    }

    private fun createComponent(
        settingsRepository: SettingsRepository = createSettingsRepository(),
        crypto: Crypto = MockCrypto()): PasswordLogic {
        return PasswordComponent(createContext(), crypto,
            createPasswordRepository(), settingsRepository)
    }
}