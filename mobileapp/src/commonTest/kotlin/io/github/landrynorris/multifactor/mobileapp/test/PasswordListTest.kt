package io.github.landrynorris.multifactor.mobileapp.test

import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.multifactor.components.PasswordListComponent
import io.github.landrynorris.multifactor.components.PasswordListLogic
import io.github.landrynorris.multifactor.models.PasswordModel
import io.github.landrynorris.multifactor.repository.PasswordRepository
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class PasswordListTest {

    @Test
    fun testListPasswords(): Unit = runBlocking {
        val crypto = MockCrypto()
        val passwordRepository = createPasswordRepository()
        passwordRepository.fillFakePasswords(crypto)

        val component = createComponent(passwordRepository = passwordRepository)

        assertOccursWithin(1000) { component.state.value.passwords.size == 3 }
    }

    @Test
    fun testListPasswordsReactsToChanges(): Unit = runBlocking {
        val crypto = MockCrypto()
        val passwordRepository = createPasswordRepository()
        passwordRepository.fillFakePasswords(crypto)

        val component = createComponent(passwordRepository = passwordRepository)
        passwordRepository.fillFakePasswords(crypto)

        assertOccursWithin(1000) { component.state.value.passwords.size == 6 }
    }

    @Test
    fun testDecryptPasswords(): Unit = runBlocking {
        val crypto = MockCrypto()
        val passwordRepository = createPasswordRepository()
        passwordRepository.fillFakePasswords(crypto)

        val component = createComponent(passwordRepository = passwordRepository, crypto = crypto)

        assertOccursWithin(1000) { component.state.value.passwords.size == 3 }

        component.showHidePressed(component.state.value.passwords.first())

        assertOccursWithin(1000, "password decryption") {
            println("Password is ${component.state.value.passwords.first().password}")
            component.state.value.passwords.first().password == "password"
        }

        component.showHidePressed(component.state.value.passwords.first())

        assertOccursWithin(1000, "password encryption") {
            component.state.value.passwords.first().password == null
        }
    }

    private fun createComponent(crypto: Crypto = MockCrypto(),
                                passwordRepository: PasswordRepository =
                            createPasswordRepository()): PasswordListLogic {
        return PasswordListComponent(createContext(), crypto,
            passwordRepository)
    }

    private fun PasswordRepository.fillFakePasswords(crypto: Crypto) {
        insertPasswords(createPassword(crypto, "pass1", "password"),
            createPassword(crypto, "pass2", "something"),
            createPassword(crypto, "pass3", "multi word"))
    }

    private fun createPassword(crypto: Crypto, name: String, password: String): PasswordModel {
        val encrypted = crypto.encrypt(password.encodeToByteArray())
        return PasswordModel(-1L, name, encrypted.iv, encrypted.data, null, null)
    }
}