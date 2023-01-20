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
        val passwordRepository = createPasswordRepository()
        passwordRepository.fillFakePasswords()

        val component = createComponent(passwordRepository = passwordRepository)

        assertOccursWithin(1000) { component.state.value.passwords.size == 3 }
    }

    @Test
    fun testListPasswordsReactsToChanges(): Unit = runBlocking {
        val passwordRepository = createPasswordRepository()
        passwordRepository.fillFakePasswords()

        val component = createComponent(passwordRepository = passwordRepository)
        passwordRepository.fillFakePasswords()

        assertOccursWithin(1000) { component.state.value.passwords.size == 6 }
    }

    @Test
    fun testDecryptPasswords(): Unit = runBlocking {
        val passwordRepository = createPasswordRepository()
        passwordRepository.fillFakePasswords()

        val component = createComponent(passwordRepository = passwordRepository)

        assertOccursWithin(1000) { component.state.value.passwords.size == 3 }

        component.showHidePressed(component.state.value.passwords.first())

        assertOccursWithin(1000, "password decryption") {
            component.state.value.passwords.first().password != null
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

    private fun PasswordRepository.fillFakePasswords() {
        insertPasswords(createPassword("pass1", "password"),
            createPassword("pass2", "something"),
            createPassword("pass3", "multi word"))
    }

    private fun createPassword(name: String, password: String): PasswordModel {
        val encrypted = MockCrypto().encrypt(password.encodeToByteArray())
        return PasswordModel(-1L, name, encrypted.iv, encrypted.data, null, null)
    }
}