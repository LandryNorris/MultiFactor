package io.github.landrynorris.multifactor.mobileapp.test

import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.multifactor.components.PasswordComponent
import io.github.landrynorris.multifactor.components.PasswordLogic
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlin.test.Test

class PasswordComponentTest {

    @Test
    fun testTypePassword() {
        val component = createComponent()

        component.createPasswordLogic
    }

    private fun createComponent(
        settingsRepository: SettingsRepository = createSettingsRepository(),
        crypto: Crypto = MockCrypto()): PasswordLogic {
        return PasswordComponent(createContext(), crypto,
            createPasswordRepository(), settingsRepository)
    }
}