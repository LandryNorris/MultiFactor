package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.encryption.Encryption
import io.github.landrynorris.encryption.KeyStore
import io.github.landrynorris.encryption.SaltGenerator
import io.github.landrynorris.multifactor.models.PasswordModel
import io.github.landrynorris.multifactor.repository.PasswordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface CreatePasswordLogic {
    val state: Flow<CreatePasswordState>

    fun nameChanged(name: String) {}
    fun passwordChanged(password: String) {}
    fun confirm() {}
}

class CreatePasswordComponent(context: ComponentContext,
                              private val passwordRepository: PasswordRepository):
    CreatePasswordLogic, ComponentContext by context {
    override val state = MutableStateFlow(CreatePasswordState())

    override fun nameChanged(name: String) {
        state.update { it.copy(name = name) }
    }

    override fun passwordChanged(password: String) {
        state.update { it.copy(password = password) }
    }

    override fun confirm() {
        val current = state.value
        val salt = SaltGenerator.generateSalt(32)
        val key = KeyStore.getKey()
        val encryptedPassword = Encryption.encrypt(
            state.value.password.encodeToByteArray(), salt = salt,
            key = key
        )
        savePasswordModel(PasswordModel(-1L, current.name, salt = salt,
            encryptedValue = encryptedPassword))
    }

    private fun savePasswordModel(passwordModel: PasswordModel) {
        passwordRepository.insertPassword(passwordModel)
    }
}

data class CreatePasswordState(val name: String = "", val password: String = "")
