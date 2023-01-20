package io.github.landrynorris.multifactor.mobileapp.test

import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.multifactor.components.CreatePasswordComponent
import io.github.landrynorris.multifactor.components.CreatePasswordLogic
import io.github.landrynorris.multifactor.repository.PasswordRepository
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class CreatePasswordTest {

    @Test
    fun testTypePassword() {
        val component = createComponent()
        assertEquals("", component.state.value.password)

        component.passwordChanged("A new password")
        assertEquals("A new password", component.state.value.password)
        assertEquals("", component.state.value.domain)
        assertEquals("", component.state.value.name)
    }

    @Test
    fun testTypeDomain() {
        val component = createComponent()
        assertEquals("", component.state.value.domain)

        component.domainChanged("A domain")
        assertEquals("A domain", component.state.value.domain)
        assertEquals("", component.state.value.password)
        assertEquals("", component.state.value.name)
    }

    @Test
    fun testTypeName() {
        val component = createComponent()
        assertEquals("", component.state.value.name)

        component.nameChanged("A name")
        assertEquals("A name", component.state.value.name)
        assertEquals("", component.state.value.domain)
        assertEquals("", component.state.value.password)
    }

    @Test
    fun testGeneratePassword() = runBlocking {
        val settingsRepository = createSettingsRepository()
        settingsRepository.setIncludeDigits(false)

        val component = createComponent(settingsRepository = settingsRepository)

        assertEquals("", component.state.value.password)

        val anyHasDigits = (0 until 10).any {
            component.generateNewPassword()
            val randomPassword = component.state.value.password

            randomPassword.any { it.isDigit() }
        }

        assertFalse(anyHasDigits)
    }

    @Test
    fun testCreatePassword(): Unit = runBlocking {
        val passwordRepository = createPasswordRepository()
        val component = createComponent(passwordRepository = passwordRepository)

        component.passwordChanged("A password")
        component.nameChanged("A name")
        component.domainChanged("A domain")
        component.confirm()

        delay(100)

        val passwords = passwordRepository.getPasswordsFlow().first()
        assertEquals(1, passwords.size)

        val password = passwords.first()

        assertEquals("A name", password.name)
        assertEquals("A domain", password.domain)
        assertNotEquals("A password", password.encryptedValue.decodeToString())
    }

    private fun createComponent(
        crypto: Crypto = MockCrypto(),
        passwordRepository: PasswordRepository = createPasswordRepository(),
        settingsRepository: SettingsRepository = createSettingsRepository()
    ): CreatePasswordLogic {
        return CreatePasswordComponent(createContext(), crypto,
            passwordRepository, settingsRepository)
    }
}