package io.github.landrynorris.multifactor.components

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.encryption.SecureCrypto
import io.github.landrynorris.multifactor.models.PasswordModel
import io.github.landrynorris.multifactor.repository.PasswordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface CreatePasswordLogic {
    val state: StateFlow<CreatePasswordState>

    fun nameChanged(name: String) {}
    fun passwordChanged(password: String) {}
    fun confirm(clipboard: ClipboardManager) {}
}

class CreatePasswordComponent(context: ComponentContext,
                              private val passwordRepository: PasswordRepository):
    CreatePasswordLogic, ComponentContext by context {
    override val state = MutableStateFlow(CreatePasswordState())

    override fun nameChanged(name: String) {
        state.update { it.copy(name = name,
            isConfirmEnabled = isConfirmEnabled(name, it.password)) }
    }

    override fun passwordChanged(password: String) {
        state.update { it.copy(password = password,
            isConfirmEnabled = isConfirmEnabled(it.name, password)) }
    }

    override fun confirm(clipboard: ClipboardManager) {
        val current = state.value
        val encrypted = SecureCrypto.encrypt(current.password.encodeToByteArray())

        savePasswordModel(PasswordModel(-1L, current.name, salt = encrypted.iv,
            encryptedValue = encrypted.data))
        //clipboard.setText(buildAnnotatedString { append(current.password) })
    }

    private fun savePasswordModel(passwordModel: PasswordModel) {
        passwordRepository.insertPassword(passwordModel)
    }

    private fun isConfirmEnabled(name: String, password: String) =
        password.isNotBlank() && name.isNotBlank()
}

data class CreatePasswordState(val name: String = "", val password: String = "",
                               val isConfirmEnabled: Boolean = false)
