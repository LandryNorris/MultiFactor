package io.github.landrynorris.app.components

import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.app.commonModule
import io.github.landrynorris.app.export.PasswordExporter
import io.github.landrynorris.app.repository.PasswordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.KoinApplication

interface PasswordExportLogic {
    val state: StateFlow<PasswordExportData>

    fun setExportPassword(password: String)
    suspend fun export()
    fun show()
    fun dismiss()
}

class PasswordExportComponent(
    val context: ComponentContext,
    val exporter: PasswordExporter,
): ComponentContext by context, PasswordExportLogic {
    override val state = MutableStateFlow(PasswordExportData())

    override fun setExportPassword(password: String) {
        state.update { it.copy(encryptionPassword = password) }
    }

    override suspend fun export() {
        val password = state.value.encryptionPassword

        val fileContents = exporter
            .createExportFileContents(password.encodeToByteArray())

        saveExportFile(fileContents)
        state.update { PasswordExportData() }
    }

    override fun show() {
        state.update { it.copy(isShowing = true) }
    }

    override fun dismiss() {
        state.update { it.copy(isShowing = false) }
    }

    private fun saveExportFile(contents: String) {

    }
}

data class PasswordExportData(
    val encryptionPassword: String = "",
    val isShowing: Boolean = false,
)
