package io.github.landrynorris.multifactor.components

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.multifactor.models.PasswordModel
import io.github.landrynorris.multifactor.repository.PasswordRepository
import io.github.landrynorris.multifactor.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface PasswordLogic {
    val state: StateFlow<PasswordState>
    val passwordListLogic: PasswordListLogic
    val createPasswordLogic: CreatePasswordLogic

    fun toggleAddPassword()
    fun hideAddPassword()
    fun copyPasswordClicked(clipboardManager: ClipboardManager?, password: String)
    fun deletePassword(model: PasswordModel)
}

class PasswordComponent(
    private val context: ComponentContext,
    crypto: Crypto,
    private val passwordRepository: PasswordRepository,
    settingsRepository: SettingsRepository
    ): PasswordLogic, ComponentContext by context {
    override val state = MutableStateFlow(PasswordState())
    override val passwordListLogic = PasswordListComponent(childContext("PasswordListLogic"),
        crypto, passwordRepository)
    override val createPasswordLogic =
        CreatePasswordComponent(childContext("CreatePasswordLogic"),
            crypto, passwordRepository, settingsRepository)

    override fun hideAddPassword() {
        state.update { it.copy(showAddPassword = false) }
    }

    override fun toggleAddPassword() {
        state.update { it.copy(showAddPassword = !it.showAddPassword) }
    }

    override fun copyPasswordClicked(clipboardManager: ClipboardManager?, password: String) {
        val annotatedString = buildAnnotatedString { append(password) }
        clipboardManager?.setText(annotatedString)
    }

    override fun deletePassword(model: PasswordModel) {
        passwordRepository.deletePassword(model)
    }
}

data class PasswordState(val showAddPassword: Boolean = false)
